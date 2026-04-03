# SauceDemo Test Automation Framework

![Selenium](https://img.shields.io/badge/Selenium-4.27-green)
![Cucumber](https://img.shields.io/badge/Cucumber-7.14-brightgreen)
![TestNG](https://img.shields.io/badge/TestNG-7.8-red)
![Maven](https://img.shields.io/badge/Build-Maven-yellow)
![Allure](https://img.shields.io/badge/Reports-Allure-orange)

BDD test automation framework for [SauceDemo](https://www.saucedemo.com/) built with Selenium WebDriver, Cucumber, and TestNG. Includes a CI/CD pipeline via GitHub Actions with Allure reporting published to GitHub Pages.

📊 **[Live Allure Report](https://elementz76.github.io/Test-Automation-Framework-Internal/)**

---

## Table of Contents

- [Project Structure](#project-structure)
- [Tech Stack](#tech-stack)
- [Framework Design](#framework-design)
- [Test Scenarios](#test-scenarios)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running Tests](#running-tests)
- [Selenium Grid](#selenium-grid)
- [Reports](#reports)
- [CI/CD Pipeline](#cicd-pipeline)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)

---

## Project Structure
```
TestAutomationFramework/
├── src/
│   └── test/
│       ├── java/
│       │   └── com/automation/
│       │       ├── driver/
│       │       │   ├── DriverFactory.java       # Browser instantiation (Chrome/Firefox/Edge, local/grid)
│       │       │   └── DriverManager.java        # ThreadLocal WebDriver management
│       │       ├── listeners/
│       │       │   └── SuiteThreadListener.java  # IAlterSuiteListener for dynamic thread config
│       │       ├── models/
│       │       │   └── SauceData.java            # POJO for test data deserialization
│       │       ├── pages/
│       │       │   ├── BasePage.java             # Shared wait utilities and interaction methods
│       │       │   ├── LoginPage.java
│       │       │   ├── ProductListPage.java
│       │       │   ├── CartPage.java
│       │       │   ├── CheckoutInfoPage.java
│       │       │   ├── CheckoutOverviewPage.java
│       │       │   └── CheckoutCompletePage.java
│       │       ├── runners/
│       │       │   └── TestRunner.java           # Cucumber-TestNG entry point with parallel DataProvider
│       │       ├── stepdef/
│       │       │   ├── ApplicationHooks.java     # @Before/@After: driver init, screenshot on failure
│       │       │   └── StepDef.java              # Cucumber step definitions
│       │       └── utils/
│       │           ├── ConfigManager.java        # Property resolution: -D flag → config.properties → default
│       │           └── JsonUtils.java            # Jackson-based JSON test data loader
│       └── resources/
│           ├── features/
│           │   └── Saucedemo.feature             # Cucumber feature file
│           ├── testdata/
│           │   └── data.json                     # Test data (credentials, products, expected messages)
│           ├── config.properties                 # Default configuration (browser, URL, threads, timeouts)
│           ├── log4j2.xml                        # Logging configuration
│           └── testng.xml                        # TestNG suite definition with SuiteThreadListener
├── logs/
│   └── automation.log                            # Runtime log output
├── pom.xml                                       # Maven build, dependencies, Surefire and Allure plugins
└── README.md
```

---

## Tech Stack

| Component | Technology |
| :--- | :--- |
| Language | Java 11 |
| Browser Automation | Selenium WebDriver 4.27 |
| Test Framework | TestNG 7.8 |
| BDD Layer | Cucumber 7.14 |
| Build Tool | Maven |
| Reporting | Allure 2.24 + Cucumber HTML |
| Logging | Log4j2 |
| Test Data | JSON via Jackson |
| Design Pattern | Page Object Model |
| CI/CD | GitHub Actions |

---

## Framework Design

### TestBase

All page classes extend `TestBase`, which handles browser lifecycle and provides shared utilities:

- `initialization()` — launches the configured browser. Detects CI via the `CI` environment variable and enables headless mode automatically. Supports local and Selenium Grid execution via `executionMode`.
- `clickOn(WebElement)` — click with retry logic and `StaleElementReferenceException` handling.
- `sendText(WebElement, String)` — clears and types into inputs with retry logic.
- `waitForVisibility(WebElement)` — explicit wait until element is visible.
- `waitForClickability(WebElement)` — explicit wait until element is interactable.

### Page Object Model

Each page of the application has a dedicated class using `@FindBy` annotations and `PageFactory`. Action methods return the next page object to support fluent chaining.

### ApplicationHooks

Manages the Cucumber lifecycle:

- `@Before` — launches the browser before each scenario.
- `@After (order = 1)` — captures a screenshot on failure and attaches it to the Allure and Cucumber HTML reports.
- `@After (order = 0)` — closes the browser after the screenshot has been taken.

### Test Data

All credentials, personal info, product lists, and expected error messages live in `data.json`. Tests reference data by index. `JsonUtils` deserializes the JSON into `SauceData` POJOs using Jackson.

---

## Test Scenarios

Defined in `src/test/resources/features/Saucedemo.feature`:

| Tag | Scenario |
| :--- | :--- |
| `@smoke @regression` | Complete E2E purchase flow — login → add products → cart → checkout → confirm |
| `@regression @negative` | Login fails with locked out user — verifies error message |
| `@smoke` | Quick smoke — login → add single product → verify cart badge |

---

## Prerequisites

- Java JDK 11+
- Maven 3.6+
- Google Chrome (latest stable)
- Git

---

## Getting Started

### Clone the repo, install dependencies and verify the setup by running @smoke
```bash
git clone https://github.com/ElementZ76/Test-Automation-Framework-Internal.git
cd Test-Automation-Framework-Internal
mvn clean install -DskipTests
mvn clean test -Dtags="@smoke"
```

---

## Running Tests

All parameters are passed with `-D` and override the defaults in `config.properties`.

| Parameter | Default | Description |
| :--- | :--- | :--- |
| `browser` | `chrome` | Browser to launch (`chrome`, `firefox`, `edge`) |
| `threads` | `1` | Number of parallel threads |
| `tags` | *(all)* | Cucumber tag expression |
| `executionMode` | `local` | `local` or `grid` |
| `gridUrl` | `http://localhost:4444` | Selenium Grid hub URL |

```bash
# Run all tests
mvn clean test

# Filter by tag
mvn clean test -Dtags="@smoke"
mvn clean test -Dtags="@regression"
mvn clean test -Dtags="not @wip"

# Change browser
mvn clean test -Dbrowser=firefox

# Parallel execution
mvn clean test -Dthreads=3
```

---

## Selenium Grid

The framework supports [Selenium Grid Standalone](https://www.selenium.dev/documentation/grid/), which combines the hub and node into a single process.

### Step 1 — Start Standalone Grid

Download the Selenium Server jar from the [Selenium releases page](https://github.com/SeleniumHQ/selenium/releases) and run:

```bash
java -jar selenium-server-<version>.jar standalone
```

The grid starts on port `4444` by default. Verify it is healthy by visiting `http://localhost:4444/ui` in a browser before running tests.

### Step 2 — Run Tests Against the Grid (one-command-test for all parameters)

```bash
mvn clean test -Dbrowser=chrome -Dthreads=3 -DgridUrl=http://localhost:4444 -DexecutionMode=grid
```

When `executionMode=grid`, `TestBase` creates a `RemoteWebDriver` pointed at the grid URL instead of launching a local browser. Everything else — hooks, steps, waits, screenshots, reports — behaves identically to a local run.

---

## Reports

### Allure

```bash
# Serve live report after a test run
mvn allure:serve

# Generate static HTML only
mvn allure:report
# Output: target/site/allure-maven-plugin/index.html
```

### Cucumber HTML

Available at `target/cucumber-reports/cucumber.html` after any test run.

### CI Report

Every push to `main` generates and publishes a report automatically:
📊 **https://elementz76.github.io/Test-Automation-Framework-Internal/**

---

## CI/CD Pipeline

The GitHub Actions workflow runs on every push and pull request to `main`, and can also be triggered manually from the Actions tab.

```
Push → Checkout → Setup JDK 11 → Run Tests (headless Chrome)
     → Upload artifacts → Generate Allure report → Deploy to GitHub Pages
```

The pipeline continues even if tests fail (`continue-on-error: true`) so reports are always generated. Artifacts are retained for 7 days. The Allure report retains history for the last 20 runs.

---

## Configuration

`src/test/resources/config.properties`

```properties
url           = https://www.saucedemo.com/
browser       = chrome
implicitwait  = 10
threads       = 1
executionMode = local
gridUrl       = http://localhost:4444
```

---

## Troubleshooting

**Chrome does not open locally** — confirm `--headless` is not hardcoded in `TestBase.java`. Headless mode is only enabled when the `CI` environment variable is set, which GitHub Actions does automatically.

**Maven dependencies not downloading** — run `mvn clean install -U`. In Eclipse: right-click project → Maven → Update Project → Force Update → OK.

**TestNG plugin missing in Eclipse** — Help → Eclipse Marketplace → search "TestNG for Eclipse" → install → restart.

**Allure report blank after CI** — check the Actions log for compilation errors. The most common cause on Linux is a filename case mismatch between the Java class name and its filename.

**Tests compile locally but fail on CI** — ensure all source files are tracked by Git: `git ls-files src/`. If any are missing, `git add src/` and commit.