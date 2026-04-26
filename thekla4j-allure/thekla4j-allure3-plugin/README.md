# thekla4j Allure 3 Plugin

Allure 3 report plugin that adds a requirements section to the report.
It collects all links of type `requirement` from test results and writes them to `requirements.json` in the report output.

## Prerequisites

- Node.js 18+
- Allure 3 CLI: `npm install -g allure`

## Installation

### Release (npmjs.com)

```bash
npm install @teststeps/thekla4j-allure3-plugin
```

### Snapshot (GitHub Packages)

Add the following to your project's `.npmrc` to resolve `@teststeps` packages from GitHub Packages:

```
@teststeps:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

Then install:

```bash
npm install @teststeps/thekla4j-allure3-plugin@snapshot
```

## Configuration

Create an `allurerc.mjs` in the directory where you run `allure generate`:

```js
import { defineConfig } from "allure";

export default defineConfig({
  name: "Allure Report",
  output: "./allure-report",
  plugins: {
    requirements: {
      import: "@teststeps/thekla4j-allure3-plugin"
    }
  }
});
```

### Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `fileName` | `string` | `"requirements.json"` | Output file name written to the report directory |

```js
new RequirementsPlugin({ fileName: "my-requirements.json" })
```

## Usage

Run Allure report generation pointing at your test results directory:

```bash
allure generate ./allure-results
```

The plugin produces `requirements.json` in the report output — a grouped list of requirements and the tests linked to them.

## How it works

The plugin reads all test results and extracts links where `type === "requirement"`.
It groups them by URL, collects the name, status, and flakiness of each linked test, and writes the result as JSON.

Example output (`requirements.json`):

```json
[
  {
    "id": "REQ-42",
    "url": "https://jira.example.com/browse/REQ-42",
    "tests": [
      { "name": "user can log in", "status": "passed", "flaky": false },
      { "name": "user sees dashboard", "status": "failed", "flaky": true }
    ]
  }
]
```
