# thekla4j-examples

Example tests demonstrating thekla4j's Allure integration features.

## Running the tests

```bash
./gradlew :thekla4j-examples:test
```

Some tests are intentionally failing to demonstrate how failures appear in the Allure report.
The build is configured with `ignoreFailures = true`.

`thekla4j-examples` is standalone: its tests are skipped for root aggregate task runs
(`test`, `check`, `build`) unless `:thekla4j-examples:*` is explicitly requested.

## Generating the Allure report

```bash
# Static HTML report (opens in build/reports/allure-report/)
./gradlew :thekla4j-examples:allureReport

# Live server (opens browser automatically)
./gradlew :thekla4j-examples:allureServe
```

Both tasks now reuse the prepacked Allure home from
`:thekla4j-allure:allure2-commandline-distribution` by depending on
`prepareAllureWithThekla4jPlugin`.

## Example tests

### JUnit 5

| Test class                      | Demonstrates                                                                                                                      |
|---------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| `ActivityLogStepsExampleTest`   | `@AllureActor` activity log mapping with nested workflows, chained tasks with input/output, JSON payloads, and failing activities |
| `StepNameTruncationExampleTest` | Step name truncation at 100 chars, long descriptions as parameters, multiple actors, all annotation types                         |
| `TestIdExampleTest`             | `@TestId` annotation appending test IDs to names                                                                                  |
| `RequirementsReportExampleTest` | `@Reqs` annotation with the Requirements report plugin                                                                            |
| `SetupTeardownExampleTest`      | `@BeforeEach`/`@AfterEach` fixture lifecycle with actors                                                                          |

### Cucumber

| File                           | Demonstrates                                                                              |
|--------------------------------|-------------------------------------------------------------------------------------------|
| `CucumberExampleTest`          | JUnit Platform Suite runner with Cucumber engine                                          |
| `activity_log_example.feature` | Activity log steps in Cucumber with `@EPIC`, `@STORY`, `@SUITE`, `@REQS`, `@TEST_ID` tags |
| `ActivityLogStepDefinitions`   | Step definitions using `Thekla4jWorld` with PicoContainer DI                              |

## Allure plugin setup

The `thekla4j-allure2-plugin` adds a **Requirements** section to the Allure report.
`thekla4j-examples` no longer assembles plugin files itself; it directly uses the prepared
Allure home produced by `:thekla4j-allure:allure2-commandline-distribution`.

## Publishing

`thekla4j-examples` is excluded from Maven publish flows. Root `publish` does not publish
examples artifacts.

### Adapting for your own project

Copy the same pattern used here:

1. Build or prepare a shared Allure home in a dedicated distribution module.
2. In your consumer module, set `allureHome` for `allureReport`/`allureServe`.
3. Depend on the distribution module's prepare task.
