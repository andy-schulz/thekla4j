---
title: Allure
parent: Utilities
layout: default
has_children: false
nav_order: 200
---

# Allure Integration

thekla4j provides two dedicated Allure integrations — one for JUnit 5 and one for Cucumber JVM 7:

| Module                             | Class                           | Use with       |
|------------------------------------|---------------------------------|----------------|
| `thekla4j-allure`                  | `Thekla4jAllureJunit5Extension` | JUnit 5        |
| `thekla4j-allure-cucumber-plugins` | `Thekla4jAllureCucumberPlugin`  | Cucumber JVM 7 |

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
| **Fixture lifecycle**      | Wraps `@BeforeAll`, `@AfterAll`, `@BeforeEach`, `@AfterEach` as named Allure fixtures with pass/fail status                                                                |
| **Parameterized tests**    | Publishes `@ParameterizedTest` parameters to Allure via report entries                                                                                                     |

### Setup

**Gradle**
```groovy
testImplementation 'com.test-steps.thekla4j:thekla4j-allure:<version>'
```

**Maven**
```xml
<dependency>
    <groupId>com.test-steps.thekla4j</groupId>
    <artifactId>thekla4j-allure</artifactId>
    <version>${thekla4j.version}</version>
    <scope>test</scope>
</dependency>
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

With this setting, `Thekla4jAllureJunit5Extension` applies automatically to **all** test
classes without any `@ExtendWith` annotation.

### Available annotations

| Annotation     | Target        | Allure label  | Description                                                        |
|----------------|---------------|---------------|--------------------------------------------------------------------|
| `@ParentSuite` | class         | `parentSuite` | Top-level grouping in the Suites tree                              |
| `@Suite`       | class         | `suite`       | Mid-level grouping (overrides the auto-generated class-name suite) |
| `@SubSuite`    | class         | `subSuite`    | Leaf-level grouping in the Suites tree                             |
| `@Epic`        | class, method | `epic`        | Epic in the Behaviors tree; method value overrides class value     |
| `@Feature`     | class, method | `feature`     | Feature in the Behaviors tree; method value overrides class value  |
| `@Story`       | class, method | `story`       | Story in the Behaviors tree; method value overrides class value    |

### Example

```java
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Epic;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Feature;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Story;
import com.teststeps.thekla4j.allure.junit5.extensions.tags.Suite;

@Epic("Shopping")
@Feature("Checkout")
@Suite("E2E Tests")
public class CheckoutTest {

  @Story("Guest checkout")
  @Test
  void guestCanCheckOut() { ... }

  @Story("Registered user checkout")
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

**Maven**
```xml
<dependency>
    <groupId>com.test-steps.thekla4j</groupId>
    <artifactId>thekla4j-allure-cucumber-plugins</artifactId>
    <version>${thekla4j.version}</version>
    <scope>test</scope>
</dependency>
```

### Registering the plugin

The plugin is pre-registered via the Java SPI (`META-INF/services/io.cucumber.plugin.Plugin`),
so Cucumber picks it up automatically when the JAR is on the classpath.

You can also register it explicitly in your `@CucumberOptions`:

```java
@CucumberOptions(
    features = "src/test/resources/features",
    plugin = {
        "com.teststeps.thekla4j.allure.cucumber.plugins.Thekla4jAllureCucumberPlugin"
    }
)
public class RunCucumberTest {}
```

### ActivityError mapping

If a step throws an `ActivityError`, the test is reported as **FAILED** (red) rather than
BROKEN (yellow). The full error message and stack trace are included in the Allure status
details.

### Default label behaviour

When no explicit tags are provided, the plugin derives the following Allure labels automatically:

| Allure label | Default value                                         |
|--------------|-------------------------------------------------------|
| `feature`    | The Gherkin `Feature:` name                           |
| `suite`      | The Gherkin `Feature:` name                           |
| `story`      | The scenario name                                     |
| `package`    | The `.feature` file path (slashes → dots, `.` → `_`) |
| `testClass`  | The scenario name                                     |
| `framework`  | `cucumber7jvm`                                        |
| `language`   | `java`                                                |

### Gherkin tag reference

Tags are read from both the `Feature:` line and individual `Scenario:` lines.
Scenario-level tags take precedence over feature-level tags.

**Composite tags** use the `@KEY=value` format:

| Tag | Effect in Allure |
|-----|-----------------|
| `@SEVERITY=<level>` | Sets the severity label. Valid levels: `blocker`, `critical`, `normal`, `minor`, `trivial` |
| `@ISSUE=<id>` | Adds a single issue link (e.g. `@ISSUE=PROJ-123`) |
| `@ISSUES=<id1>,<id2>` | Adds multiple issue links (comma- or semicolon-separated) |
| `@TMSLINK=<id>` | Adds a TMS (test-management system) link |
| `@LINK=<url>` | Adds a plain hyperlink |
| `@LINK.<type>=<name>` | Adds a named typed link (e.g. `@LINK.confluence=MyPage`) |
| `@OWNER=<name>` | Sets the test owner label |
| `@EPIC=<name>` | Sets the Epic in the Behaviors tree |
| `@STORY=<name>` | Sets the Story label (overrides the scenario-name default) |
| `@SUITE=<name>` | Sets the Suite label (overrides the feature-name default) |
| `@SUB_SUITE=<name>` | Sets the Sub-Suite label |
| `@PARENT_SUITE=<name>` | Sets the Parent Suite label; the `PARENT_SUITE` environment variable overrides the tag value |
| `@TEST_ID=<id>` | Attaches a test ID — shown as `<scenario name> (testId: <id>)` in the report |

**Simple tags** (no `=`):

| Tag | Effect in Allure |
|-----|-----------------|
| `@FLAKY` | Marks the test as flaky |
| `@KNOWN` | Marks the test as a known issue |
| `@MUTED` | Mutes the test in the Allure report |
| `@critical`, `@blocker`, `@normal`, `@minor`, `@trivial` | Bare-word severity shorthand (equivalent to `@SEVERITY=<level>`) |
| Any other tag | Added as a raw tag label |

### Example feature file

```gherkin
@EPIC=Shopping @SUITE=E2E @PARENT_SUITE=Regression
Feature: Checkout

  @SEVERITY=critical @ISSUE=SHOP-42 @OWNER=andy
  Scenario: Guest can check out
    Given a guest user with items in the cart
    When the guest completes checkout
    Then the order confirmation is displayed

  @STORY=Registered checkout @TMSLINK=TC-007 @FLAKY
  Scenario: Registered user can check out
    Given a logged-in user with items in the cart
    When the user completes checkout
    Then the order is saved to their account
```

### Fixture hooks

Cucumber `@Before` and `@After` hooks are reported in Allure as **fixture containers**
(prepare / tear-down). They appear in the Allure report's timeline and test-body view
with their own pass/fail status and error details.

`@BeforeStep` and `@AfterStep` hooks are reported inline as regular **steps** inside
the test case.

### Data tables and attachments

| Cucumber event | Allure attachment |
|---|---|
| Step with a `DataTable` argument | TSV attachment named *Data table* |
| `scenario.write("text")` | Plain-text attachment named *Text output* |
| `scenario.embed(bytes, mediaType, name)` | Attachment with the given media type and name |
