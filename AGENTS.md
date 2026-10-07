# AGENTS.md

Guidance for coding agents working on thekla4j. Project conventions live in
[.claude/CLAUDE.md](.claude/CLAUDE.md); this file covers how to build and test.

thekla4j is a Gradle multi-module Java 17 project implementing the Screenplay Pattern. Always use
the Gradle wrapper from the repository root.

## Build

```bash
./gradlew build
```

This runs spotless, javadoc and every test, including the integration tests.

## Test

Unit tests need nothing but the wrapper. The integration tests (`IT_*`) need a browser and the
`frameworktester` web application. Both come from `global_resources/docker-compose.yml`:

```bash
docker compose -f global_resources/docker-compose.yml up -d
```

### 1. Run the tests against a local browser

The default. `seleniumGridConfig.yaml` has `defaultConfig: LOCAL`, so the browser is started on
this machine and reaches the application as `localhost`.

```bash
./gradlew :thekla4j-browser:selenium:test
```

### 2. Run the tests against the selenium grid

Two properties switch the run over. `thekla4j.browser.selenium.config` selects the entry of
`seleniumGridConfig.yaml`, and `thekla4j.test.appUrl` is needed because a browser inside a grid
container cannot reach the application as `localhost`.

```bash
./gradlew :thekla4j-browser:selenium:test \
    -Dthekla4j.browser.selenium.config=seleniumGrid \
    -Dthekla4j.test.appUrl=http://host.docker.internal:3000
```

`thekla4j.browser.config` selects the entry of `browserConfig.yaml` the same way, so a single run
can be pointed at another browser, for example
`-Dthekla4j.browser.config=gridFirefox`.

These properties only reach the test JVM because
`buildSrc/src/main/groovy/com.teststeps.java-conventions.gradle` forwards them. Integration tests
must build their browser through `BrowserSetup`, never by calling `SeleniumLoader.of` directly, or
they ignore the configuration and always start a local browser.
