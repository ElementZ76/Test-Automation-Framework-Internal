***

# Multi-App Test Automation Framework

![Selenium](https://img.shields.io/badge/Selenium-4.43-green)
![Cucumber](https://img.shields.io/badge/Cucumber-7.14-brightgreen)
![TestNG](https://img.shields.io/badge/TestNG-7.8-red)
![Maven](https://img.shields.io/badge/Build-Maven-yellow)
![Allure](https://img.shields.io/badge/Reports-Allure-orange)

BDD test automation framework for [SauceDemo](https://www.saucedemo.com/) and [Flipkart](https://www.flipkart.com/) built with Selenium WebDriver, Cucumber, and TestNG. Includes a CI/CD pipeline via GitHub Actions with Allure reporting published to GitHub Pages.

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
- [Running from IntelliJ IDEA](#running-from-intellij-idea)
- [Selenium Grid](#selenium-grid)
- [Reports](#reports)
- [CI/CD Pipeline](#cicd-pipeline)
- [Configuration](#configuration)
- [Onboarding a New Application](#onboarding-a-new-application)
- [Troubleshooting](#troubleshooting)

---

## Project Structure

```text
TestAutomationFramework/
├── src/
│   └── test/
│       ├── java/
│       │   └── com/automation/
│       │       ├── driver/
│       │       │   ├── DriverFactory.java
│       │       │   └── DriverManager.java
│       │       ├── listeners/
│       │       │   └── SuiteThreadListener.java
│       │       ├── models/
│       │       │   ├── SauceData.java
│       │       │   └── FlipkartData.java
│       │       ├── pages/
│       │       │   ├── BasePage.java
│       │       │   ├── saucedemo/
│       │       │   │   └── *.java
│       │       │   └── flipkart/
│       │       │       ├── HomePage.java
│       │       │       ├── SearchResultsPage.java
│       │       │       ├── ProductDetailsPage.java
│       │       │       └── CartPage.java
│       │       ├── runners/
│       │       │   ├── SauceDemoTestRunner.java
│       │       │   └── FlipkartTestRunner.java
│       │       ├── stepdef/
│       │       │   ├── saucedemo/
│       │       │   │   └── *.java
│       │       │   └── flipkart/
│       │       │       ├── ApplicationHooks.java
│       │       │       └── FlipkartStepDef.java
│       │       └── utils/
│       │           ├── ConfigManager.java
│       │           └── JsonUtils.java
│       └── resources/
│           ├── features/
│           │   ├── saucedemo/
│           │   │   └── Saucedemo.feature
│           │   └── flipkart/
│           │       └── Flipkart.feature
│           ├── testdata/
│           │   ├── sauceDemoData.json
│           │   └── flipkartData.json
│           ├── config.properties
│           ├── saucedemo.properties
│           ├── flipkart.properties
│           ├── allure.properties
│           ├── log4j2.xml
│           ├── testng-saucedemo.xml
│           └── testng-flipkart.xml
├── .github/workflows/ci.yml
├── pom.xml
└── README.md

```

---

## Tech Stack

| Component | Technology |
| --- | --- |
| Language | Java 11 |
| Browser Automation | Selenium WebDriver 4.43 |
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

### Driver Management

`DriverFactory` instantiates the browser based on `browser` and `executionMode` config values. `DriverManager` holds the instance in a `ThreadLocal<WebDriver>`, making parallel execution thread-safe. Every scenario gets its own driver via `@Before` in the respective `ApplicationHooks` and tears it down via `@After`.

### BasePage

All page classes extend `BasePage`, which provides shared interaction utilities. No `Thread.sleep()` is used anywhere in the framework:

* `clickOn(WebElement)` — click with retry logic and `StaleElementReferenceException` handling.
* `sendText(WebElement, String)` — clears and types into inputs with retry logic.
* `waitForVisibility(WebElement)` — explicit wait until element is visible.
* `waitForClickability(WebElement)` — explicit wait until element is interactable.
* `switchToNewTab(String originalTab)` — explicit wait for a new tab to open and switches driver focus to it. Ideal for dealing with `target="_blank"` product links.
* `waitForStableElementCount(String xpath, Duration timeout, long intervalMs)` — blocks execution until the number of elements matching the locator is identical across two consecutive polls. Prevents flakiness when JS-driven grids (like Flipkart search results) re-render after sorting or filtering.

### Page Object Model

Each page has a dedicated class using `@FindBy` annotations and `PageFactory`. Action methods return the next page object to support fluent chaining.

### ApplicationHooks

Manages the Cucumber scenario lifecycle for each app (SauceDemo & Flipkart):

* `@Before` — launches the browser before each scenario via `DriverFactory.initializeDriver()`.
* `@After (order = 1)` — captures a screenshot on failure and attaches it to the Allure report.
* `@After (order = 0)` — closes the browser after the screenshot has been taken.

### ConfigManager

Resolves configuration in this priority order:

```text
-D flag (System property)           ← highest priority
    ↓ not found
{appName}.properties value          ← loaded when -DappName is set
    ↓ not found
config.properties value             ← base config, always loaded
    ↓ not found
hard-coded default                  ← lowest priority

```

### JsonUtils & Test Data

A generic test data loader that accepts a `TypeReference` so any POJO type (`SauceData.class`, `FlipkartData.class`) can be deserialized — keeping the utility entirely uncoupled from application domains. All user credentials, terms, filters, and validations live in the `.json` files inside `testdata/` and are fetched natively by index in the Cucumber steps.

---

## Test Scenarios

Defined in `src/test/resources/features/`:

| App | Tag | Scenario |
| --- | --- | --- |
| **SauceDemo** | `@smoke @regression` | Complete E2E purchase flow — login → add products → cart → checkout → confirm |
| **SauceDemo** | `@regression @negative` | Login fails with locked out user — verifies error message |
| **SauceDemo** | `@smoke` | Quick smoke — login → add single product → verify cart badge |
| **Flipkart** | `@smoke @search @pdp` | Guest User Search & Discovery — searches product, validates PDP load and details |
| **Flipkart** | `@regression @filter @sort` | JS Dynamic Grids — filters by brand, applies sorting mechanisms (low-high, popularity) |
| **Flipkart** | `@smoke @cart` | Cart Operations — adds items from PDP, modifies cart, removes item, validates empty states |
| **Flipkart** | `@regression @cart` | Cart Persistence — confirms product stays in the cart after a hard page refresh |
| **Flipkart** | `@smoke @checkout` | Checkout Flow — ensures guest checkout redirects properly to the login page |

---

## Prerequisites

* Java JDK 11+
* Maven 3.6+
* Google Chrome (latest stable)
* Git

---

## Getting Started

```bash
git clone [https://github.com/ElementZ76/Test-Automation-Framework-Internal.git](https://github.com/ElementZ76/Test-Automation-Framework-Internal.git)
cd Test-Automation-Framework-Internal
mvn clean install -DskipTests

```

To run SauceDemo tests:

```bash
mvn clean test -DappName=saucedemo -Dtags="@smoke"

```

To run Flipkart tests:

```bash
mvn clean test -DappName=flipkart -Dtags="@smoke"

```

---

## Running Tests

All parameters are passed with `-D` and override the defaults in `config.properties`.

| Parameter | Default | Options | Description |
| --- | --- | --- | --- |
| `appName` | *(not set)* | `saucedemo`, `flipkart` | Loads `{appName}.properties` on top of `config.properties` |
| `browser` | `chrome` | `chrome`, `firefox`, `edge` | Browser to launch |
| `threads` | `3` | Any integer ≥ 1 | Parallel scenario threads |
| `tags` | *(all scenarios)* | Any Cucumber tag expression | Filter scenarios by tag |
| `executionMode` | `local` | `local`, `grid` | Local browser or Selenium Grid |
| `gridUrl` | `http://localhost:4444` | Any valid URL | Grid hub endpoint |

```bash
# Run as named application
mvn clean test -DappName=flipkart

# Filter by tag
mvn clean test -DappName=flipkart -Dtags="@search"
mvn clean test -DappName=flipkart -Dtags="not @negative"

# Change browser
mvn clean test -DappName=flipkart -Dbrowser=firefox

# Parallel execution
mvn clean test -DappName=flipkart -Dthreads=4

# Combined
mvn clean test -DappName=flipkart -Dbrowser=chrome -Dthreads=3 -Dtags="@smoke"

```

### Thread Behavior

Thread count is resolved following the ConfigManager priority order above. `SuiteThreadListener` applies the count to both `threadCount` and `dataProviderThreadCount` on the suite. `TestRunner.setUpClass` applies it again on the test-level DataProvider. Both are required because Cucumber scenarios run via TestNG's DataProvider thread pool, not the suite method pool.

Recommended parallel range: 1–5 threads for local runs. CI uses the value in `config.properties` unless overridden.

### Supported Browsers

| Browser | Local | Grid | Headless (CI) |
| --- | --- | --- | --- |
| Chrome | ✅ | ✅ | ✅ auto |
| Firefox | ✅ | ✅ | ✅ auto |
| Edge | ✅ | ✅ | ✅ auto |

Headless mode is enabled automatically when the `CI` environment variable is set (GitHub Actions sets this by default). It is never enabled on local runs.

---

## Running from IntelliJ IDEA

IntelliJ runs TestNG directly without Surefire. Parameters are passed as JVM options in the run configuration. `ConfigManager` reads from `System.getProperty()` in both cases, so behavior is identical to Maven.

### Method 1 — Run via `testng.xml` (recommended)

1. Open `src/test/resources/testng-flipkart.xml` (or the equivalent SauceDemo file)
2. Right-click → **Run '...xml'**
3. To override parameters: **Run → Edit Configurations → VM options**:

```text
   -DappName=flipkart -Dbrowser=chrome -Dthreads=3 -Dtags=@smoke

```

### Method 2 — Run via `TestRunner` class

1. Open `src/test/java/com/automation/runners/FlipkartTestRunner.java`
2. Right-click → **Run 'FlipkartTestRunner'**
3. Add VM options as above in the run configuration

### Method 3 — Run individual scenarios from the feature file

1. Open `src/test/resources/features/flipkart/Flipkart.feature`
2. Click the green gutter arrow next to any scenario
3. Parameters from `config.properties` apply; override via VM options in the generated run config

---

## Selenium Grid

The framework supports [Selenium Grid Standalone](https://www.selenium.dev/documentation/grid/), which combines the hub and node into a single process.

### Step 1 — Start Standalone Grid

Download the Selenium Server jar from the [Selenium releases page](https://github.com/SeleniumHQ/selenium/releases) and run:

```bash
java -jar selenium-server-<version>.jar standalone

```

Verify the grid is healthy at `http://localhost:4444/ui` before running tests.

### Step 2 — Run Tests Against the Grid

```bash
mvn clean test -DappName=flipkart -Dbrowser=chrome -Dthreads=3 -DgridUrl=http://localhost:4444 -DexecutionMode=grid

```

When `executionMode=grid`, `DriverFactory` creates a `RemoteWebDriver` pointed at the grid URL. Everything else — hooks, steps, waits, screenshots, reports — behaves identically to a local run.

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

```text
Push → Checkout → Setup JDK 11 → Run Tests (headless Chrome)
     → Upload artifacts → Generate Allure report → Deploy to GitHub Pages

```

The pipeline continues even if tests fail (`continue-on-error: true`) so reports are always generated. Artifacts are retained for 7 days. The Allure report retains history for the last 20 runs.

---

## Configuration

### Base config — `src/test/resources/config.properties`

```properties
# Application
# url     = 
# appName =

# Browser
browser       = chrome

# Timeouts
implicitWait  = 10

# Parallel execution
threads       = 3

# Execution
executionMode = local
gridUrl       = http://localhost:4444

```

All values can be overridden at runtime without modifying this file:

```bash
mvn clean test -Dbrowser=edge -Dthreads=5 -DexecutionMode=grid

```

### App-specific config — `src/test/resources/flipkart.properties`

When `-DappName=flipkart` is passed, `ConfigManager` loads `flipkart.properties` after `config.properties` and merges the values on top. Only include keys that are specific to or different for this application.

```properties
url     = [https://www.flipkart.com/](https://www.flipkart.com/)
browser = chrome
threads = 4

```

---

## Onboarding a New Application

The framework is designed so that onboarding a new application requires no changes to any existing core file.

### Step 1 — Create an app-specific properties file

Create `src/test/resources/{appName}.properties` with only the values unique to the new application:

```properties
# src/test/resources/hrm.properties
url     = [https://your-hrm-app.com/](https://your-hrm-app.com/)
browser = chrome
threads = 2

```

### Step 2 — Create a data model

Add a POJO under `src/test/java/com/automation/models/` for the new app's test data shape:

```java
// com/automation/models/HrmData.java
public class HrmData {
    private String username;
    private String password;
    // getters and setters
}

```

### Step 3 — Create a test data file

Add `src/test/resources/testdata/hrmData.json` with the test data for the new application.

### Step 4 — Create page objects

Add page classes under a namespaced sub-package to avoid collision with existing pages:

```text
com/automation/pages/hrm/LoginPage.java
com/automation/pages/hrm/DashboardPage.java

```

### Step 5 — Create feature files and step definitions

```text
src/test/resources/features/hrm/Login.feature
com/automation/stepdef/hrm/HrmStepDef.java

```

Create a new Test Runner specific to the app to isolate execution (`HrmTestRunner.java`).

### Step 6 — Run the new application

```bash
mvn clean test -DappName=hrm -Dtags="@smoke"

```

`ConfigManager` loads `config.properties` first, then merges `hrm.properties` on top. The URL, browser, and any other overrides in `hrm.properties` take effect automatically.

---

## Troubleshooting

**Chrome does not open locally** — confirm `--headless` is not hardcoded anywhere. Headless mode is only enabled when the `CI` environment variable is set, which GitHub Actions does automatically.

**`-Dthreads` has no effect** — ensure `pom.xml` does not contain a `<threads>` entry in Surefire's `<systemPropertyVariables>` block. That mapping was previously misconfigured and has been removed.

**`-DappName` config not loading** — confirm the file exists at `src/test/resources/{appName}.properties` and the filename matches the `appName` value exactly (case-sensitive on Linux).

**Maven dependencies not downloading** — run `mvn clean install -U`. In Eclipse: right-click project → Maven → Update Project → Force Update → OK.

**TestNG plugin missing in Eclipse** — Help → Eclipse Marketplace → search "TestNG for Eclipse" → install → restart.

**Allure report blank after CI** — check the Actions log for compilation errors. The most common cause on Linux is a filename case mismatch between the Java class name and its filename.

**Tests compile locally but fail on CI** — ensure all source files are tracked by Git: `git ls-files src/`. If any are missing, `git add src/` and commit.

**IntelliJ and Maven produce different results** — verify both are using the same `config.properties` and that VM options in IntelliJ match the `-D` flags used in Maven. Both resolve properties through `ConfigManager` identically.

```

```