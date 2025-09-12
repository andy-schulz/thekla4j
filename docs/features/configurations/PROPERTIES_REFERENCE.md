---
title: Properties Reference
parent: PROPERTIES
layout: default
nav_order: 1
---

# Thekla4j Properties Reference

This document provides a comprehensive reference for all available properties in the Thekla4j framework. Properties can
be set via system properties or configuration files.

## How to Set Properties

Properties can be set in the following ways (in order of precedence):

1. **System Properties**: Use `-D` flag when running Java (highest priority)
   ```bash
   java -Dthekla4j.browser.highlightElements=false YourTestClass
   ```

2. **Property Files**: Create a `thekla4j.properties` file in your classpath
   ```properties
   # Example thekla4j.properties file
   thekla4j.browser.highlightElements=false
   thekla4j.browser.slowDownExecution=true
   thekla4j.browser.slowDownTimeInSeconds=2
   thekla4j.browser.screenshot.relativePath=build/screenshots
   thekla4j.browser.selenium.bidi.log=true
   ```

3. **Default Values**: Properties will use their default values if not explicitly set

### Property File Configuration

The framework automatically looks for a file named `thekla4j.properties` in the classpath. This file should be placed in:

- **Maven projects**: `src/main/resources/thekla4j.properties` or `src/test/resources/thekla4j.properties`
- **Gradle projects**: `src/main/resources/thekla4j.properties` or `src/test/resources/thekla4j.properties`
- **JAR files**: Include the file in the root of your JAR's classpath

**Property file format:**
```properties
# Comments start with # or !
# Format: property.name=value
thekla4j.browser.highlightElements=true
thekla4j.browser.slowDownExecution=false
thekla4j.core.see.wait.factor=1.5

# Boolean values (case-insensitive)
thekla4j.browser.autoScroll.enabled=TRUE
thekla4j.browser.selenium.bidi.log=False

# String values
thekla4j.browser.autoScroll.vertical=center
thekla4j.browser.screenshot.absolutePath=/path/to/screenshots

# Empty values are allowed for some properties
thekla4j.browser.screenshot.relativePath=
```

**Property Precedence:**
System properties override property file values, which override default values.

## Core Properties

Properties from the `thekla4j-core` module:

| Property Name                     | Type          | Default Value              | Description                              |
|-----------------------------------|---------------|----------------------------|------------------------------------------|
| `thekla4j.tempDir.path`           | String        | System temp directory      | Absolute path to the used temp directory |
| `thekla4j.tempDir.base.path`      | String        | System base temp directory | Base temp directory path                 |
| `thekla4j.core.see.wait.factor`   | String/Number | "1"                        | Wait factor for see activity             |
| `thekla4j.core.retry.wait.factor` | String/Number | "1"                        | Wait factor for retry activity           |

## Browser Core Properties

Properties from the `thekla4j-browser-core` module:

| Property Name                              | Type          | Default Value             | Description                                                                       |
|--------------------------------------------|---------------|---------------------------|-----------------------------------------------------------------------------------|
| `thekla4j.browser.highlightElements`       | Boolean       | "true"                    | Highlight elements during interaction. Possible values: true, false               |
| `thekla4j.browser.slowDownExecution`       | Boolean       | "false"                   | Slow down execution for debugging. Possible values: true, false                   |
| `thekla4j.browser.slowDownTimeInSeconds`   | Number        | "1"                       | Time in seconds to slow down the execution                                        |
| `thekla4j.browser.autoScroll.enabled`      | Boolean       | "false"                   | Enable automatic scrolling to elements. Possible values: true, false              |
| `thekla4j.browser.autoScroll.vertical`     | String        | "center"                  | Vertical scroll position. Possible values: top, center, bottom                    |
| `thekla4j.browser.screenshot.relativePath` | String        | "" (empty)                | Relative project path to store the screenshots                                    |
| `thekla4j.browser.screenshot.absolutePath` | String        | Current working directory | Absolute path to store the screenshots                                            |
| `thekla4j.browser.config`                  | String        | None (required)           | The browser configuration to use                                                  |
| `thekla4j.browser.element.wait.factor`     | String/Number | "1"                       | Multiplier for the wait time for elements. Default is 1.0, which means no scaling |

## Selenium Properties

Properties from the `thekla4j-browser-selenium` module:

| Property Name                        | Type    | Default Value   | Description                            |
|--------------------------------------|---------|-----------------|----------------------------------------|
| `thekla4j.browser.selenium.config`   | String  | None (required) | The Selenium configuration to use      |
| `thekla4j.browser.selenium.bidi.log` | Boolean | "false"         | Use WebDriver Bidi to get browser logs |

## Property Types

- **String**: Text values
- **Boolean**: "true" or "false" (case-insensitive)
- **Number**: Numeric values that can be parsed as integers or floats
- **None (required)**: Properties that must be explicitly set and have no default value

## Usage Examples

### Basic Browser Configuration

```bash
# Enable element highlighting and slow down execution for debugging
java -Dthekla4j.browser.highlightElements=true \
     -Dthekla4j.browser.slowDownExecution=true \
     -Dthekla4j.browser.slowDownTimeInSeconds=2 \
     YourTestClass
```

### Screenshot Configuration

```bash
# Set custom screenshot directory
java -Dthekla4j.browser.screenshot.absolutePath=/path/to/screenshots \
     YourTestClass
```

### Selenium Configuration

```bash
# Configure Selenium with BiDi logging
java -Dthekla4j.browser.selenium.config=chrome \
     -Dthekla4j.browser.selenium.bidi.log=true \
     YourTestClass
```

### Performance Tuning

```bash
# Adjust wait factors for faster/slower execution
java -Dthekla4j.core.see.wait.factor=0.5 \
     -Dthekla4j.core.retry.wait.factor=2 \
     -Dthekla4j.browser.element.wait.factor=1.5 \
     YourTestClass
```

## Notes

- Properties with default values will use those defaults if not explicitly set
- Properties marked as "None (required)" will throw runtime exceptions if not set
- Boolean properties accept "true"/"false" (case-insensitive)
- Wait factor properties accept decimal values (e.g., "0.5", "2.0")
- Paths can be absolute or relative depending on the property
