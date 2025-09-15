---
title: Config
parent: Browser
grand_parent: Web
layout: default
has_children: false
nav_order: 1240
---

# Browser Configuration Reference

This document provides comprehensive documentation for browser configurations in the Thekla4j framework. The framework
supports three main configuration types: `BrowserConfig`, `SeleniumGridConfig`, and `AppiumConfig`.

## Overview

Browser configurations define how browsers are launched and behave during test execution. Configurations can be loaded
from YAML/JSON files or created programmatically.

## Configuration Types

### 1. BrowserConfig

The `BrowserConfig` class defines the basic browser settings for browser selection and execution.

Multiple configurations can be defined in a single YAML/JSON file, each identified by a unique name. 
A `defaultConfig: myFirstConfig` property must be specified to indicate which configuration to use at browser startup.

It can be overwritten at runtime:

```java

Browser browser = Selenium.browser()
    .usingBrowserConfig(Option.of("myConfigName")) // load specific config by name
    .updateBrowserConfig(cfg -> cfg.withHeadless(true)) // override specific properties programmatically
    .build();

```

#### Attributes of a single BrowserConfig

| Attribute            | Type              | Required | Default  | Description                                                     |
|----------------------|-------------------|----------|----------|-----------------------------------------------------------------|
| `browserName`        | `BrowserName`     | Yes      | `CHROME` | Browser type: `CHROME`, `CHROMIUM`, `FIREFOX`, `EDGE`, `SAFARI` |
| `browserVersion`     | `String`          | No       | `null`   | Specific browser version (e.g., "120.0")                        |
| `platformName`       | `OperatingSystem` | No       | `null`   | Operating system: `WINDOWS`, `MAC`, `LINUX`, `ANDROID`, `IOS`   |
| `osVersion`          | `String`          | No       | `null`   | Operating system version (e.g., "10", "11")                     |
| `deviceName`         | `String`          | No       | `null`   | Device name for mobile testing (required for mobile)            |
| `enableFileUpload`   | `boolean`         | No       | `false`  | Enable file upload capabilities                                 |
| `enableFileDownload` | `boolean`         | No       | `false`  | Enable file download capabilities                               |
| `binary`             | `String`          | No       | `null`   | Path to custom browser binary                                   |
| `headless`           | `boolean`         | No       | `false`  | Run browser in headless mode                                    |
| `browserArgs`        | `List<String>`    | No       | `[]`     | Additional browser command line arguments                       |
| `debug`              | `DebugOptions`    | No       | `null`   | Browser debugging configuration                                 |
| `video`              | `VideoConfig`     | No       | `null`   | Video recording configuration                                   |

#### Example BrowserConfig YAML

```yaml
#config to use when starting the browser
defaultConfig: headlessFirefox

# Desktop Chrome Configuration
desktopChrome:
  browserName: chrome
  browserVersion: "120.0"
  platformName: Windows
  osVersion: "11"
  headless: false
  enableFileDownload: true
  browserArgs:
    - "--no-sandbox"
    - "--disable-dev-shm-usage"
    - "--disable-gpu"
  debug:
    debuggerAddress: "localhost:9222"
    downloadPath: "/path/to/downloads"
  video:
    record: true
    relativePath: "videos/test-recordings"
    filePrefix: "test-"

# Headless Firefox Configuration
headlessFirefox:
  browserName: firefox
  headless: true
  enableFileUpload: true
  browserArgs:
    - "--width=1920"
    - "--height=1080"

# Mobile Safari Configuration
mobileSafari:
  browserName: safari
  platformName: iOS
  osVersion: "16.0"
  deviceName: "iPhone 14"
```

### 2. SeleniumGridConfig

The `SeleniumGridConfig` class configures connections to Selenium Grid or cloud providers like BrowserStack, Sauce Labs, etc.

Multiple configurations can be defined in a single YAML/JSON file, each identified by a unique name.
A `defaultConfig: myFirstGridConfig` property must be specified to indicate which configuration to use at driver startup.

It can be overwritten at runtime:
```java

Browser browser = Selenium.browser()
    .usingSeleniumConfig(Option.of("myGridConfigName")) // load specific config by name
    .updateSeleniumConfig(cfg -> cfg.withRemoteUrl("http://newRemoteUrl.io")) // override specific properties programmatically
    .build();

```

#### Attributes

| Attribute      | Type                               | Required | Default | Description                                    |
|----------------|------------------------------------|----------|---------|------------------------------------------------|
| `remoteUrl`    | `String`                           | Yes      | -       | URL of the Selenium Grid hub or cloud provider |
| `capabilities` | `Map<String, Map<String, String>>` | No       | `{}`    | Additional capabilities organized by category  |

#### Example SeleniumGridConfig YAML

```yaml
#config to use when starting the browser on the grid, use LOCAL when no configuration shall be used. e.g. when starting a debug session 
defaultConfig: localGrid

# Local Selenium Grid
localGrid:
  remoteUrl: "http://localhost:4444/wd/hub"
  capabilities:
    seleniumOptions:
      se:recordVideo: "true"
      se:screenResolution: "1920x1080"
    customCapabilities:
      build: "build-123"
      project: "my-project"

# BrowserStack Configuration
browserStack:
  remoteUrl: "https://hub-cloud.browserstack.com/wd/hub"
  capabilities:
    bstack:
      userName: "your-username"
      accessKey: "your-access-key"
      buildName: "Test Build"
      projectName: "My Project"
      sessionName: "Test Session"
      local: "false"
      debug: "true"
      networkLogs: "true"
      consoleLogs: "errors"
    se:
      acceptInsecureCerts: "true"

# Sauce Labs Configuration
sauceLabs:
  remoteUrl: "https://ondemand.saucelabs.com:443/wd/hub"
  capabilities:
    sauceOptions:
      username: "your-username"
      accessKey: "your-access-key"
      build: "build-123"
      name: "Test Name"
      tags: [ "regression", "chrome" ]
      recordVideo: "true"
      recordScreenshots: "true"
```

### 3. AppiumConfig

The `AppiumConfig` class configures connections to Appium servers for mobile application testing.

#### Attributes

| Attribute      | Type                               | Required | Default | Description                               |
|----------------|------------------------------------|----------|---------|-------------------------------------------|
| `remoteUrl`    | `String`                           | Yes      | -       | URL of the Appium server                  |
| `capabilities` | `Map<String, Map<String, String>>` | No       | `{}`    | Appium capabilities organized by category |

#### Example AppiumConfig YAML

```yaml
defaultConfig: localAppiumIOS

# Local Appium Server - iOS
localAppiumIOS:
  remoteUrl: "http://localhost:4723/wd/hub"
  capabilities:
    appium:
      automationName: "XCUITest"
      app: "/path/to/app.ipa"
      bundleId: "com.example.app"
      udid: "device-udid"
      xcodeOrgId: "team-id"
      xcodeSigningId: "iPhone Developer"

# Local Appium Server - Android
localAppiumAndroid:
  remoteUrl: "http://localhost:4723/wd/hub"
  capabilities:
    appium:
      automationName: "UiAutomator2"
      app: "/path/to/app.apk"
      appPackage: "com.example.app"
      appActivity: "com.example.app.MainActivity"
      noReset: "true"
      fullReset: "false"
```

## Nested Configuration Objects

### DebugOptions

Used within `BrowserConfig` for debugging configuration:

```yaml
debug:
  debuggerAddress: "localhost:9222"  # Chrome DevTools debugging address
  downloadPath: "/absolute/path/to/downloads"  # Download directory path
```

### VideoConfig

Used within `BrowserConfig` for video recording:

```yaml
video:
  record: true  # Enable/disable video recording
  relativePath: "videos/recordings"  # Relative path for video files
  filePrefix: "test-run-"  # Prefix for video filenames
```

## Default Configuration Property

Each configuration type supports a `defaultConfig` property that serves as a base configuration. This allows you to
define common settings once and override specific properties as needed.


## Programmatic Configuration Override

You can override configuration properties programmatically using the builder pattern or the `with` methods:

### Java Examples

```java
// Create base configuration
BrowserConfig baseConfig = BrowserConfig.of(BrowserName.CHROME)
        .withHeadless(false)
        .withEnableFileDownload(true);

// Override specific properties for different tests
BrowserConfig headlessConfig = baseConfig.withHeadless(true);
BrowserConfig firefoxConfig = baseConfig.withBrowserName(BrowserName.FIREFOX);

// Selenium Grid configuration
SeleniumGridConfig gridConfig = SeleniumGridConfig.of("http://localhost:4444/wd/hub")
    .withCapabilities(HashMap.of(
        "se", HashMap.of(
            "recordVideo", "true",
            "screenResolution", "1920x1080"
        )
    ));

// Appium configuration
AppiumConfig appiumConfig = AppiumConfig.of("http://localhost:4723/wd/hub")
    .withCapabilities(HashMap.of(
        "appium", HashMap.of(
            "app", "/path/to/app.ipa"
        )
    ));
```

### Using Configuration in Selenium Builder

```java
// Load configuration from file and override programmatically
BrowserConfig config = loadConfigFromFile("chrome-config.yml")
        .withHeadless(true)  // Override for CI environment
        .withBrowserArgs(List.of("--window-size=1920,1080"));

Actor actor = Actor.named("TestActor")
    .can(Browse.with(
        Selenium.using(config)
            .build()
    ));
```

## Configuration Loading

Configurations are typically loaded from YAML files in the classpath:

```java
// Load from resources
BrowserConfig config = YAML.fromResource("configs/chrome-config.yml", BrowserConfig.class);
SeleniumGridConfig gridConfig = YAML.fromResource("configs/grid-config.yml", SeleniumGridConfig.class);
AppiumConfig appiumConfig = YAML.fromResource("configs/appium-config.yml", AppiumConfig.class);
```

## Best Practices

1. **Use Default Configurations**: Define common settings in `defaultConfig` to reduce duplication
2. **Environment-Specific Overrides**: Use programmatic overrides for environment-specific settings (CI, staging, prod)
3. **Capability Organization**: Group related capabilities under meaningful category names
4. **Security**: Never commit sensitive information like access keys to version control
5. **Documentation**: Comment your configuration files to explain non-obvious settings

## Available Values

### Browser Names

- `chrome` - Google Chrome
- `chromium` - Chromium browser
- `firefox` - Mozilla Firefox
- `edge` - Microsoft Edge
- `safari` - Apple Safari

### Operating Systems

- `Windows` - Microsoft Windows
- `OS X` - Apple macOS
- `Linux` - Linux distributions
- `Android` - Android mobile OS
- `iOS` - Apple iOS mobile OS

### Common Browser Arguments

#### Chrome/Chromium

- `--headless` - Run in headless mode
- `--no-sandbox` - Disable sandbox (required in some CI environments)
- `--disable-dev-shm-usage` - Disable /dev/shm usage
- `--disable-gpu` - Disable GPU acceleration
- `--window-size=1920,1080` - Set window size
- `--disable-extensions` - Disable browser extensions

#### Firefox

- `--headless` - Run in headless mode
- `--width=1920` - Set window width
- `--height=1080` - Set window height
- `--safe-mode` - Start in safe mode
