# Claude instructions for thekla4j

## 1. Think Before Coding

**Don't assume. Don't hide confusion. Surface tradeoffs.**

Before implementing:
- State your assumptions explicitly. If uncertain, ask.
- If multiple interpretations exist, present them - don't pick silently.
- If a simpler approach exists, say so. Push back when warranted.
- If something is unclear, stop. Name what's confusing. Ask.

## 2. Simplicity First

**Minimum code that solves the problem. Nothing speculative.**

- No features beyond what was asked.
- No abstractions for single-use code.
- No "flexibility" or "configurability" that wasn't requested.
- No error handling for impossible scenarios.
- If you write 200 lines and it could be 50, rewrite it.

Ask yourself: "Would a senior engineer say this is overcomplicated?" If yes, simplify.

## 3. Surgical Changes

**Touch only what you must. Clean up only your own mess.**

When editing existing code:
- Don't "improve" adjacent code, comments, or formatting.
- Don't refactor things that aren't broken.
- Match existing style, even if you'd do it differently.
- If you notice unrelated dead code, mention it - don't delete it.

When your changes create orphans:
- Remove imports/variables/functions that YOUR changes made unused.
- Don't remove pre-existing dead code unless asked.

The test: Every changed line should trace directly to the user's request.

## 4. Goal-Driven Execution

**Define success criteria. Loop until verified.**

Transform tasks into verifiable goals:
- "Add validation" → "Write tests for invalid inputs, then make them pass"
- "Fix the bug" → "Write a test that reproduces it, then make it pass"
- "Refactor X" → "Ensure tests pass before and after"

For multi-step tasks, state a brief plan:
```
1. [Step] → verify: [check]
2. [Step] → verify: [check]
3. [Step] → verify: [check]
```

Strong success criteria let you loop independently. Weak criteria ("make it work") require constant clarification.

## Project context

- thekla4j is a **Gradle multi-module Java project** implementing the Screenplay Pattern for test automation.
- Primary language is **Java** with supporting Gradle/Groovy files.
- The repository uses **JDK 17** for builds and CI.

## Code style and conventions

- Follow `.editorconfig` for formatting:
  - default indentation: 2 spaces
  - Java max line length: 120
  - line endings: LF for text files
- Keep changes focused and module-scoped in this monorepo.
- Prefer existing project patterns and naming over introducing new abstractions.

## Build and test

- Use the Gradle wrapper from the repository root.
- Main verification command:

```bash
./gradlew build
```

## Practical guidance for changes

- Update only the modules relevant to the requested change.
- When behavior changes, update or add tests in the affected module(s).
- Keep public API changes intentional and consistent with existing package structure.
