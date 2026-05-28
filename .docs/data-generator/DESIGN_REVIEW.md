# Data Generator — Design Review & Improvement Plan

## Current Architecture

```
GeneratorStore         — central store, regex-based parsing, mutable state
├── DataGenerator      — FunctionalInterface: Map<String,String> → Try<String>
├── InlineGenerator    — FunctionalInterface: () → Try<String>
├── ParameterParsingFunctions — parses "key: val, key2: val2" into Map
├── GeneratorStoreFunctions   — parsing/execution/assignment logic
└── GeneratorScanner          — @Generator/@InlineGen annotation scanning
```

Consumer projects typically add:

```
World.replaceShortVariable — converts $PARAM → ${PARAM}
DataGenerators.mapDefaultValue — chains: replaceShortVariable → generateData → LiftTry
```

---

## Issues Found

### 1. Dual `$PARAM` resolution (confusing)

Two independent mechanisms resolve parameter references:

| Mechanism | Location | Regex | Behavior |
|---|---|---|---|
| `World.replaceShortVariable` | Consumer project | `$([A-Z_]+)` | Converts `$DATE` → `${DATE}` (text substitution only) |
| `GeneratorStore.resolveShortParamReferences` | thekla4j library | `$([A-Za-z0-9_]+)` | Resolves `$DATE` → stored value (actual resolution) |

They don't conflict in practice (different regex scope, complementary behavior), but the **conceptual overlap is confusing**. A developer seeing `$DATE` has to know which mechanism handles it depending on where it appears.

**Suggestion**: Remove `replaceShortVariable` from the consumer project. Rely entirely on the built-in GeneratorStore resolution. Then `mapDefaultValue` simplifies from:

```java
.mapValues(world::replaceShortVariable)
.mapValues(world::generateData)
```

to:

```java
.mapValues(world::generateData)
```

### 2. `mapDefaultValue` helper is duplicated across consumer modules

Consumer projects that implement generators typically need a helper like this to resolve parameter values:

```java
private Map<String, String> mapDefaultValue(Map<String, String> maps, String defaultValue) {
    return (maps.containsKey("default") ? maps.put(defaultValue, maps.get("default").get()) : maps)
        .mapValues(world::replaceShortVariable)
        .mapValues(world::generateData)
        .transform(LiftTry.fromMap())
        .getOrElseThrow(x -> new IllegalArgumentException(x.getMessage()));
}
```

Every DataGenerators class that implements generators with parameter resolution ends up re-implementing this same pattern. Each copy must remember to call `replaceShortVariable` and handle errors consistently.

**Suggestion**: Move this into `GeneratorStore` or `Thekla4jWorld` as a shared utility method. The "default key rename" + "resolve params" + "generate data" chain should be a single reusable function.

### 3. Regex-based parsing is fragile

The generator matching regex uses a character whitelist:

```
[A-Za-z0-9\-\+\_\.;\=\$\:\,\s\"]
```

Problems:
- Must be extended **every time** a new character appears in parameter values (we just had to add `"`)
- `matches()` requires the generator call to start at position 0 — no embedded generators in arbitrary strings
- Nested generators like `formatDate{date: data{today}, format: "..."}` are impossible because `{}` aren't in the whitelist

**Suggestion**: Replace the regex approach with a simple **state machine parser** that:
1. Scans for `name{` openings (alphanumeric name followed by `{`)
2. Counts brace depth to find the matching `}`
3. Handles quoted strings inside braces (ignores `{` and `}` inside quotes)
4. No character whitelist needed — anything inside balanced `{}` is valid parameter content

### 4. Silent failure when generator not found

```java
// GeneratorStoreFunctions.java:
if (filteredGenerator.isEmpty()) {
    log.debug("No generator found for input: {}", generatorInput);
    return Try.success(generatorInput);  // ← silently returns input unchanged!
}
```

A typo like `randomStrng{16}` (missing 'i') passes through silently. The input string is returned unchanged. You only discover the problem later when the test fails for an unrelated reason.

However, there are valid cases where a string matching the generator pattern is **intentional test data** (e.g. `"Enter myField{value} into the form"`). Failing would break these cases.

**Suggestion**: Log a **warning** (not an error/failure) when the input matches the general generator pattern but no registered generator handles it. Include the list of available generators in the warning message so typos are easy to spot in logs, but don't break the flow.

### 5. Pattern recompilation on every call

```java
// GeneratorStore.java line 161:
if (Pattern.compile(REGEX_GENERAL_GENERATOR_PATTERN).matcher(generatorInput).matches()) {
```

This compiles the same regex every time `parseAndExecute` is called.

**Suggestion**: Pre-compile to a static `Pattern` field.

### 6. No parameter validation on generators

Generators don't declare what parameters they expect. A misspelled key (`fromat` instead of `format`) passes through silently — the generator only fails when it calls `map.get("format")` and gets `None`.

**Suggestion (low priority)**: Add an optional `Set<String> expectedParams()` default method to `DataGenerator`. If present, validate parameter names before calling `run()` and produce a helpful error: "Unknown parameter 'fromat'. Did you mean 'format'?".

### 7. No generator composition / nesting

Currently requires two separate calls with variable assignment:

```
data{today} => ${DATE}
formatDate{date: $DATE, format: "EEEE, d. MMMM yyyy"} => ${FORMATTED}
```

Cannot write: `formatDate{date: data{today}, format: "..."}`

**Assessment**: Low priority. The explicit variable-assignment approach is readable and debuggable. If the parser switches to brace-counting (#3), nesting becomes feasible, but the added complexity may not be worth it.

---

## Prioritized Improvements

| Priority | Issue | Impact | Effort |
|----------|-------|--------|--------|
| **High** | #4 Warn on unknown generators | Surfaces typos without breaking valid test data | Small |
| **High** | #1 Unify `$PARAM` resolution | Removes confusion, simplifies consumer code | Small |
| **High** | #5 Pre-compile static `Pattern` | Minor perf, cleaner code | Trivial |
| **Medium** | #2 Extract `mapDefaultValue` utility | DRY, consistent behavior | Small |
| **Medium** | #3 State machine parser | No more whitelist maintenance | Medium |
| **Low** | #7 Generator nesting | Convenience | Large (depends on #3) |
| **Low** | #6 Parameter validation | Better error messages | Small |

---

## Recommended Approach

### Phase 1 — Quick wins
- Pre-compile the general generator pattern (#5)
- Log a **warning** when a string matches the generator pattern but no generator is registered (#4)
- Include available generator names in the warning so typos are easy to spot in test logs

### Phase 2 — Unify resolution
- Make GeneratorStore the single authority for `$PARAM` resolution (#1)
- Remove `replaceShortVariable` from consumer project
- Extract `mapDefaultValue` into `GeneratorStore` or `Thekla4jWorld` (#2)

### Phase 3 — Parser rewrite (if needed)
- Replace regex + character whitelist with state machine parser (#3)
- Eliminates ongoing regex maintenance
- Opens the door for generator nesting (#7) if desired later
