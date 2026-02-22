---
title: Cucumber - Data Generator
parent: Utilities
layout: default
has_children: false
nav_order: 100
---

# Data Generator

The Data Generator is a utility for the Cucumber integration that allows generating dynamic test data at step level.
Instead of using hardcoded test data in your feature files, you can reference generator functions that produce data at
runtime – e.g. by calling an API, reading from a database, or performing calculations.

---

## Overview

A `GeneratorStore` holds all registered generators and is the central entry point for parsing and executing them.

```java
GeneratorStore generatorStore = GeneratorStore.create();
```

`GeneratorStore.create()` pre-registers the following built-in generators:

| Generator      | Description                                                   | Example                              |
|----------------|---------------------------------------------------------------|--------------------------------------|
| `randomString` | Generates a random alphanumeric string of a given length      | `randomString{16}`                   |
|                | Supports optional `prefix` and `specialChars` parameters      | `randomString{length: 16, prefix: user_}` |

---

## Generator Syntax

Generators are invoked using the following syntax inside Cucumber step strings:

```
generatorName{param1: value1, param2: value2}
```

The result can optionally be stored in a named parameter for later retrieval:

```
generatorName{param1: value1} => ${MY_PARAM}
```

Stored parameters can be referenced anywhere using `${MY_PARAM}` or as part of a string:

```
Hello ${MY_PARAM}!
```

JSON results can be accessed via dot notation:

```
${MY_PARAM.name}
${MY_PARAM.address.city}
```

---

## Registering Generators – New Form (Annotation-based)

The recommended way to register generators is via the `@Generator` annotation. Annotate fields or methods in a
provider class and register the entire provider with `registerGenerators()`.

### On Fields

The annotated field must be of type `DataGenerator`. The `name` attribute is optional – if omitted, the field name
is used as the generator name.

```java
public class MyGenerators {

    @Generator(name = "createUser", description = "Creates a user via the API and returns the user ID")
    public final DataGenerator createUser = (data) -> {
        String role = data.get("role").getOrElse("viewer");
        // call your API here ...
        return Try.of(() -> "user-id-12345");
    };

    @Generator   // generator name will be "randomEmail"
    public final DataGenerator randomEmail = (data) -> {
        String domain = data.get("domain").getOrElse("example.com");
        return Try.of(() -> UUID.randomUUID() + "@" + domain);
    };
}
```

Register the provider with the store:

```java
GeneratorStore generatorStore = GeneratorStore.create()
    .registerGenerators(new MyGenerators());
```

Multiple providers can be passed at once:

```java
GeneratorStore generatorStore = GeneratorStore.create()
    .registerGenerators(new MyGenerators(), new AnotherGeneratorClass());
```

### On Methods

For generators that need **configuration parameters** (e.g. a base URL or a database connection), you can annotate a
factory method. The method parameters are automatically populated from the generator call string. The method must
return a `DataGenerator`.

```java
public class MyGenerators {

    @Generator(name = "createContract")
    public DataGenerator createContract(String templateId, String ownerId) {
        return (data) -> {
            // templateId and ownerId are injected from the generator call string
            // e.g. createContract{templateId: tmpl-001, ownerId: usr-123}
            String title = data.get("title").getOrElse("Default Contract");
            // call your API here ...
            return Try.of(() -> "contract-id-99");
        };
    }
}
```

Called in a Cucumber step like this:

```
createContract{templateId: tmpl-001, ownerId: usr-123} => ${CONTRACT_ID}
```

Supported method parameter types: `String`, `int`, `long`, `boolean`, `double`.

---

## Registering Generators – Legacy Form

{: .deprecated }
> The `addGenerator()` method is deprecated and will be removed in a future version.
> Please migrate to the annotation-based approach described above.

Generators can still be added individually via `addGenerator()`:

```java
public final DataGenerator createUser = (data) -> {
    String role = data.get("role").getOrElse("viewer");
    // call your API here ...
    return Try.of(() -> "user-id-12345");
};

GeneratorStore generatorStore = GeneratorStore.create();
generatorStore.addGenerator("createUser", createUser);
```

A description can optionally be provided as second argument:

```java
generatorStore.addGenerator("createUser", "Creates a user and returns the ID", createUser);
```

---

## Inline Generators

Inline generators are simpler generators that take no parameters and can be embedded anywhere in a string using
`?{GENERATOR_NAME}`.

> **Note:** Inline generator names must consist only of uppercase letters, digits, and underscores (`[A-Z0-9_]`).

### Annotation-based (recommended)

Use the `@InlineGen` annotation on `InlineGenerator` fields and register them via `registerGenerators()`:

```java
public class MyInlineGenerators {

    @InlineGen(name = "UUID")
    public final InlineGenerator uuid = () -> Try.of(() -> UUID.randomUUID().toString());

    @InlineGen(name = "TIMESTAMP")
    public final InlineGenerator timestamp = () -> Try.of(() -> String.valueOf(System.currentTimeMillis()));
}

// Registration
GeneratorStore store = GeneratorStore.create()
    .registerGenerators(new MyInlineGenerators());
```

If the `name` attribute is omitted, the field name is used:

```java
@InlineGen
public final InlineGenerator MY_SESSION_ID = () -> Try.of(() -> "sess-" + UUID.randomUUID());
// registered as "MY_SESSION_ID"
```

`@InlineGen` and `@Generator` annotations can be mixed freely in the same provider class:

```java
public class AllGenerators {

    @Generator(name = "createUser")
    public final DataGenerator createUser = (data) -> Try.of(() -> "user-42");

    @InlineGen(name = "REQUEST_ID")
    public final InlineGenerator requestId = () -> Try.of(() -> UUID.randomUUID().toString());
}
```

### Legacy form (deprecated)

```java
GeneratorStore generatorStore = GeneratorStore.create()
    .addInlineGenerator("UUID", () -> Try.of(() -> UUID.randomUUID().toString()))
    .addInlineGenerator("TIMESTAMP", () -> Try.of(() -> String.valueOf(System.currentTimeMillis())));
```

> **Deprecated:** `addInlineGenerator()` is deprecated since 2.2.0 and will be removed in a future version.
> Use the `@InlineGen` annotation instead.

### Usage

Used in a step string:

```
My order reference is ?{UUID}
```

Inline generators can also be combined with parameter assignment:

```
order-?{UUID} => ${ORDER_REF}
```

The built-in `TIMESTAMP_IN_MS` inline generator is always available:

```
?{TIMESTAMP_IN_MS}
```

---

## Full Example

```java
// Generator provider class
public class ContractGenerators {

    @Generator(name = "createDraft", description = "Creates a draft contract and returns its ID")
    public final DataGenerator createDraft = (data) -> {
        String title = data.get("title").getOrElse("Unnamed");
        // call API ...
        return Try.of(() -> "draft-id-42");
    };

    @Generator(name = "publishContract")
    public DataGenerator publishContract(String environment) {
        return (data) -> {
            String contractId = data.get("contractId").getOrElse("");
            // publish to given environment ...
            return Try.of(() -> "published-" + contractId + "-on-" + environment);
        };
    }

    @InlineGen(name = "REQUEST_ID")
    public final InlineGenerator requestId = () -> Try.of(() -> UUID.randomUUID().toString());
}

// Setup in your Cucumber hooks or step definitions
GeneratorStore store = GeneratorStore.create()
    .registerGenerators(new ContractGenerators());

// Parsing step data
store.parseAndExecute("createDraft{title: My Contract} => ${DRAFT_ID}");
store.parseAndExecute("publishContract{environment: staging, contractId: ${DRAFT_ID}} => ${RESULT}");

String result = store.parseAndExecute("${RESULT}").get();
// result: "published-draft-id-42-on-staging"
```

---

## Error Handling

All `parseAndExecute` calls return a `Try<String>`. On failure, the `Try` holds the exception:

```java
Try<String> result = store.parseAndExecute("myGenerator{param: value}");

if (result.isFailure()) {
    System.err.println("Generator failed: " + result.getCause().getMessage());
}
```

Common error causes:

| Cause | Description |
|---|---|
| Generator name not found | The input string is returned as-is (no error) |
| Missing required parameter | `IllegalArgumentException` with parameter name |
| Invalid parameter type | `IllegalArgumentException` with type hint |
| Duplicate generator name | `IllegalArgumentException` on registration |
| Generator returns `null` | `IllegalStateException` |
