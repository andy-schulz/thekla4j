---
title: Allure
parent: Utilities
layout: default
has_children: false
nav_order: 200
---

# Allure Integration

thekla4j provides dedicated Allure integrations for JUnit 5, Cucumber JVM 7, and a report plugin
for a separate **Requirements** section:

| Module                             | Class                           | Use with       |
|------------------------------------|---------------------------------|----------------|
| `thekla4j-allure-junit-extensions` | `Thekla4jAllureJunit5Extension` | JUnit 5        |
| `thekla4j-allure-cucumber-plugins` | `Thekla4jAllureCucumberPlugin`  | Cucumber JVM 7 |
| `thekla4j-allure2-plugin`          | `Thekla4jAllurePlugin`          | Allure 2 Report |

Both integrations share the same core behaviour: `ActivityError` failures are reported as
**FAILED** (red) instead of BROKEN (yellow), and Allure report hierarchy labels are populated
from tags.

---

## Table of Contents
{: .no_toc }

1. TOC
{:toc}

---

## Background

Allure determines a test's final status by inspecting the thrown exception:

| Thrown type                | Allure status | Colour    |
|----------------------------|---------------|-----------|
| `java.lang.AssertionError` | FAILED        | 🔴 red    |
| Any other `Throwable`      | BROKEN        | 🟡 yellow |

thekla4j's `ActivityError` extends `Throwable` directly. Because it is not a
`java.lang.AssertionError`, Allure would normally classify its failures as **BROKEN**
even though they represent deliberate assertion mismatches. Both integrations fix this.

---

## JUnit 5 – `Thekla4jAllureJunit5Extension`

`Thekla4jAllureJunit5Extension` is a single JUnit 5 extension that combines all
thekla4j-specific Allure capabilities:

| Capability                 | Description                                                                                                                                                                |
|----------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **ActivityError → FAILED** | Catches `ActivityError` during a test, sets Allure status to FAILED, and re-throws as `AssertionError` so both the extension and `AllureJunitPlatform` agree on the status |
| **Hierarchy labels**       | Processes `@Epic`, `@Feature`, `@Story`, `@Suite`, `@SubSuite`, `@ParentSuite` class/method annotations                                                                    |
| **Issue links**            | Processes `@Issues({...})` on class/method and adds Allure issue links                                                                                                     |
| **Requirement links**      | Processes `@Reqs({...})` on class/method and adds Allure requirement links                                                                                                  |
| **Test ID**                | Processes `@TestId("...")` on class/method, appends to test name and sets `testCaseId`                                                                                      |
| **Activity log steps**     | Scans `@AllureActor` fields and maps each Actor's activity log tree to native Allure steps                                                                                  |
| **HTML activity log**      | Attaches the HTML activity log on test failure for quick inspection                                                                                                         |
| **Fixture lifecycle**      | Wraps `@BeforeAll`, `@AfterAll`, `@BeforeEach`, `@AfterEach` as named Allure fixtures with pass/fail status                                                                |
| **Parameterized tests**    | Publishes `@ParameterizedTest` parameters to Allure via report entries                                                                                                     |

### Setup

**Gradle**
```groovy
testImplementation 'com.test-steps.thekla4j:thekla4j-allure-junit-extensions:<version>'
```

### Registering the extension

#### Option 1 – Per test class

```java
import com.teststeps.thekla4j.allure.junit5.extensions.Thekla4jAllureJunit5Extension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(Thekla4jAllureJunit5Extension.class)
public class MyTest {

  @Test
  void myTest() {
    // ActivityError thrown here → Allure reports FAILED (red)
  }
}
```

#### Option 2 – Global auto-detection

The extension is pre-registered via
`META-INF/services/org.junit.jupiter.api.extension.Extension`. Enable it project-wide by
adding the following to `src/test/resources/junit-platform.properties`:

```properties
junit.jupiter.extensions.autodetection.enabled=true
```

### Available annotations

| Annotation      | Target        | Effect                                                                     |
|-----------------|---------------|----------------------------------------------------------------------------|
| `@ParentSuite`  | class         | Top-level grouping in the Suites tree                                      |
| `@Suite`        | class         | Mid-level grouping (overrides the auto-generated class-name suite)         |
| `@SubSuite`     | class         | Leaf-level grouping in the Suites tree                                     |
| `@Epic`         | class, method | Epic in the Behaviors tree; method value overrides class value             |
| `@Feature`      | class, method | Feature in the Behaviors tree; method value overrides class value          |
| `@Story`        | class, method | Story in the Behaviors tree; method value overrides class value            |
| `@Issues`       | class, method | Adds one or more issue links; method value overrides class value           |
| `@Reqs`         | class, method | Adds one or more requirement links; method value overrides class value     |
| `@TestId`       | class, method | Appends test ID to name and sets `testCaseId`; method overrides class      |
| `@AllureActor`  | field         | Marks an `Actor` field for activity log mapping to Allure steps            |

### @TestId

Appends the test ID to the test name in the Allure report and sets the `testCaseId` field.
The report shows the test as `"testName (TestId: TC-001)"`.

```java
@TestId("TC-001")
@Test
void loginWithValidCredentials() { ... }
```

Method-level `@TestId` overrides class-level, matching the behaviour of `@Issues` and `@Reqs`.

### @AllureActor – Activity Log Steps

Mark `Actor` fields with `@AllureActor` to automatically map the activity log tree to
native Allure steps after test execution. Each actor's log appears under a
**📋 Activity Log** section in the test body.

```java
@ExtendWith(Thekla4jAllureJunit5Extension.class)
class MyTest {

  @AllureActor
  Actor alice = Actor.named("Alice");

  @Test
  void searchForProduct() {
    alice.attemptsTo(new SearchForProduct("Laptop"));
  }
}
```

The Allure report shows:

```
📋 Activity Log
└── Log of Alice
    └── SearchForProduct - search for product 'Laptop'
        ├── EnterSearchTerm - enter search term 'Laptop'
        └── ClickSearchButton - click search button
```

**Step names** combine the activity class name and the `@Action`/`@Workflow` description,
separated by ` - `. If the combined name exceeds 100 characters, it is truncated with `...`
and the full description is available as a step parameter.

**On test failure**, the HTML activity log is additionally attached for quick inspection
in the Allure attachment tab.

### Example

```java
@ExtendWith(Thekla4jAllureJunit5Extension.class)
@Epic("Shopping")
@Feature("Checkout")
@Suite("E2E Tests")
@Issues({"SHOP-101", "SHOP-102"})
@Reqs({"REQ-1001", "REQ-1002"})
public class CheckoutTest {

  @AllureActor
  Actor tester = Actor.named("Tester");

  @Story("Guest checkout")
  @TestId("TC-CHECKOUT-001")
  @Test
  void guestCanCheckOut() { ... }

  @Story("Registered user checkout")
  @TestId("TC-CHECKOUT-002")
  @Test
  void registeredUserCanCheckOut() { ... }
}
```

---

## Cucumber 7 JVM – `Thekla4jAllureCucumberPlugin`

`Thekla4jAllureCucumberPlugin` is a `ConcurrentEventListener` that bridges Cucumber JVM 7
and Allure. It translates Cucumber events into Allure test results, maps Gherkin tags to
Allure labels and links, and correctly classifies `ActivityError` as **FAILED**.

### Setup

**Gradle**
```groovy
testImplementation 'com.test-steps.thekla4j:thekla4j-allure-cucumber-plugins:<version>'
```

### Registering the plugin

You can register it explicitly in your `@CucumberOptions` or via the JUnit Platform Suite:

```java
@Suite
@IncludeEngines("cucumber")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "src/test/resources/features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.myapp.steps")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
    value = "com.teststeps.thekla4j.allure.cucumber.plugins.Thekla4jAllureCucumberPlugin")
class RunCucumberTest {}
```

### Activity Log Steps with `Thekla4jWorld`

To get automatic activity log mapping in Cucumber tests, extend `Thekla4jWorld`.

**Step 1** – Create a project World that extends `Thekla4jWorld`:

```java
public class World extends Thekla4jWorld {
    // add project-specific concerns: browser setup, API clients, etc.
}
```

**Step 2** – Use the World in step definitions (injected via PicoContainer):

```java
public class MyStepDefs {
    private final World world;

    public MyStepDefs(World world) {
        this.world = world;
    }

    @Given("{word} is on stage")
    public void actorIsOnStage(String name) {
        world.callActorToStageNamed(name);
    }
}
```

After each scenario, the plugin maps all actors' activity logs to Allure steps and attaches
the HTML log on failure. No additional glue package or bridge hook is required.

### `Thekla4jWorld`

`Thekla4jWorld` (module `thekla4j-cucumber:thekla4j-world`) is the base world class for
Cucumber tests. It provides:

| Method                      | Description                                        |
|-----------------------------|----------------------------------------------------|
| `getCast()`                 | Returns the `Cast` of actors                       |
| `callActorToStageNamed(name)` | Creates or retrieves an actor by name            |
| `getGeneratorStore()`       | Returns the `GeneratorStore` for dynamic test data |
| `generateData(string)`      | Parses and executes data generators in a string    |
| `registerGenerators(providers)` | Registers `@Generator`/`@InlineGen` annotated providers |

### Gherkin tag reference

Tags are read from both the `Feature:` line and individual `Scenario:` lines.
Scenario-level tags take precedence over feature-level tags.

#### thekla4j plugin tags

These tags are introduced by the thekla4j Allure Cucumber plugin.

| Tag                   | Effect in Allure                                                  |
|-----------------------|-------------------------------------------------------------------|
| `@REQ=<id>`           | Adds a single requirement link                                    |
| `@REQS=<id1>,<id2>`   | Adds multiple requirement links (comma- or semicolon-separated)   |
| `@TEST_ID=<id>`       | Attaches a test ID — shown as `<scenario name> (testId: <id>)` in the report |

#### Existing/common Allure-Cucumber semantics (also supported by this plugin)

These tags are also parsed by `Thekla4jAllureCucumberPlugin`, but their meaning follows
common Allure/Cucumber conventions.

**Composite tags** use the `@KEY=value` format:

| Tag                    | Effect in Allure                                                                             |
|------------------------|----------------------------------------------------------------------------------------------|
| `@SEVERITY=<level>`    | Sets the severity label. Valid levels: `blocker`, `critical`, `normal`, `minor`, `trivial` |
| `@ISSUE=<id>`          | Adds a single issue link (e.g. `@ISSUE=PROJ-123`)                                            |
| `@ISSUES=<id1>,<id2>`  | Adds multiple issue links (comma- or semicolon-separated)                                    |
| `@TMSLINK=<id>`        | Adds a TMS (test-management system) link                                                     |
| `@LINK=<url>`          | Adds a plain hyperlink                                                                       |
| `@LINK.<type>=<name>`  | Adds a named typed link (e.g. `@LINK.confluence=MyPage`)                                     |
| `@OWNER=<name>`        | Sets the test owner label                                                                    |
| `@EPIC=<name>`         | Sets the Epic in the Behaviors tree                                                          |
| `@STORY=<name>`        | Sets the Story label (overrides the scenario-name default)                                   |
| `@SUITE=<name>`        | Sets the Suite label (overrides the feature-name default)                                    |
| `@SUB_SUITE=<name>`    | Sets the Sub-Suite label                                                                     |
| `@PARENT_SUITE=<name>` | Sets the Parent Suite label; the `PARENT_SUITE` environment variable overrides the tag value |

**Simple tags** (no `=`):

| Tag                                                       | Effect in Allure                                                 |
|-----------------------------------------------------------|------------------------------------------------------------------|
| `@FLAKY`                                                  | Marks the test as flaky                                          |
| `@KNOWN`                                                  | Marks the test as a known issue                                  |
| `@MUTED`                                                  | Mutes the test in the Allure report                              |
| `@critical`, `@blocker`, `@normal`, `@minor`, `@trivial` | Bare-word severity shorthand (equivalent to `@SEVERITY=<level>`) |
| Any other tag                                             | Added as a raw tag label                                         |

### Example feature file

The example below intentionally combines thekla4j-specific tags and standard Allure-style tags
(all handled by the plugin).

```gherkin
@EPIC=Shopping @SUITE=E2E @PARENT_SUITE=Regression
Feature: Checkout

  @SEVERITY=critical @ISSUE=SHOP-42 @OWNER=andy @REQS=REQ-001,REQ-002
  Scenario: Guest can check out
    Given Alice is on stage
    When Alice searches for "Laptop"
    Then the search result should contain "Laptop"

  @STORY=Registered_checkout @TEST_ID=TC-007 @FLAKY
  Scenario: Registered user can check out
    Given Bob is on stage
    When Bob searches for "Tablet"
    Then the search result should contain "Tablet"
```

---

## Allure report plugin – `thekla4j-allure2-plugin`

Use this plugin when you want requirement links to appear in a dedicated **Requirements**
section in the report (instead of only under generic Links).

### What it does

- collects links where `type=requirement`
- writes requirement data to `extra.requirements` and `widgets/requirements.json`
- renders a dedicated **Requirements** block in test details
- removes requirement links from the default **Links** block to avoid duplicates

### Required test-side link generation

- JUnit: annotate tests/classes with `@Reqs({...})`
- Cucumber: use `@REQ=` / `@REQS=`
- (optional) configure link pattern in `allure.properties`:

```properties
thekla4j.req.link.issue.pattern=https://requirements.example.com/{}
```

### Report setup (Gradle + Allure CLI)

1. Add the plugin JAR (artifact `com.test-steps.thekla4j:thekla4j-allure2-plugin:<version>`)
   to the Allure plugin `lib/` directory.
2. Place the plugin descriptor under
   `<allure-home>/plugins/thekla4j-allure2-plugin/allure-plugin.yml`.
3. Place frontend assets (`index.js`, `styles.css`) under
   `<allure-home>/plugins/thekla4j-allure2-plugin/static/`.
4. Enable plugin ID `thekla4j-allure2-plugin` in `<allure-home>/config/allure.yml`.

After that, generate the report as usual (`allureReport` / `allureServe`).

### Jenkins-ready prepacked Allure CLI (Gradle task)

If you want a ready-to-use Allure Commandline bundle for Jenkins Global Tool Configuration,
build it from the dedicated distribution subproject:

```bash
./gradlew :thekla4j-allure:allure2-commandline-distribution:packageAllureWithThekla4jPlugin
```

Generated artifact:

```text
thekla4j-allure/allure2-commandline-distribution/build/distributions/allure-commandline-thekla4j-<allureVersion>.zip
```

The ZIP already contains:

- the Allure CLI binaries
- `plugins/thekla4j-allure2-plugin/allure-plugin.yml`
- `plugins/thekla4j-allure2-plugin/static/{index.js,styles.css}`
- `plugins/thekla4j-allure2-plugin/lib/thekla4j-allure2-plugin-*.jar`
- merged `config/allure.yml` with `thekla4j-allure2-plugin` enabled

Use this ZIP as the Allure Commandline installation in Jenkins, then reference that tool in your
pipeline `allure` step.

---

## Configuration properties

| Property                              | Source                           | Description                                             |
|---------------------------------------|----------------------------------|---------------------------------------------------------|
| `thekla4j.req.link.issue.pattern`     | `allure.properties` or system property | URL pattern for requirement links (`{}` = ID placeholder) |
| `allure.link.issue.pattern`           | `allure.properties`              | URL pattern for issue links (standard Allure property)  |
