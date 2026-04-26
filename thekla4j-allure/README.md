# thekla4j-allure

Allure integrations for thekla4j — JUnit 5, Cucumber JVM 7, and a report plugin.

## Modules

| Submodule            | Artifact                              | Description                                                     |
|----------------------|---------------------------------------|-----------------------------------------------------------------|
| `junit-extensions`   | `thekla4j-allure-junit-extensions`    | JUnit 5 extension: labels, links, ActivityError mapping, activity log steps |
| `cucumber-plugins`   | `thekla4j-allure-cucumber-plugins`    | Cucumber 7 plugin: tags, ActivityError mapping, activity log steps via `Thekla4jWorld` |
| `thekla4j-allure2-plugin` | `thekla4j-allure2-plugin`       | Allure 2 report-side plugin: dedicated Requirements section     |
| `allure2-commandline-distribution` | _(build-only)_      | Builds a Jenkins-ready Allure 2 Commandline ZIP with thekla4j plugin preinstalled |
| `thekla4j-allure3-plugin` | `@teststeps/thekla4j-allure3-plugin` | Allure 3 report-side plugin (npm) with requirements export |

## Features

### Activity Log Steps

Both JUnit 5 and Cucumber integrations map the Actor's activity log tree to native Allure
steps. The steps appear under a **📋 Activity Log** section in the test body with:

- Status propagation (passed/failed) from the activity tree
- Step names combining activity name + description (truncated at 100 chars)
- Input/output as step parameters
- Attachments (screenshots, text) on individual steps
- HTML activity log attached on test failure

**JUnit 5** — mark Actor fields with `@AllureActor`:

```java
@AllureActor
Actor alice = Actor.named("Alice");
```

**Cucumber** — extend `Thekla4jWorld` and include the hook glue path:

```java
public class World extends Thekla4jWorld { ... }
```

### @TestId

Appends a test ID to the test name: `"testName (TestId: TC-001)"`.

- JUnit 5: `@TestId("TC-001")` on class or method
- Cucumber: `@TEST_ID=TC-001` tag

### Requirement links

Adds requirement links to the Allure report with link type `requirement`.

- JUnit 5: `@Reqs({"REQ-1001", "REQ-1002"})` on class or method
- Cucumber: `@REQ=REQ-1001` or `@REQS=REQ-1001,REQ-1002` tag
- URL pattern: `thekla4j.req.link.issue.pattern=https://your-tracker/requirements/{}`

### Report plugin

The `thekla4j-allure2-plugin` report-side plugin extracts requirement links into a
dedicated **Requirements** section in the Allure test result view.

See [ALLURE.md](../docs/features/utilities/ALLURE.md) for full documentation.
