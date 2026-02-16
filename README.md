# Web Automation Framework — SauceDemo

![CI](https://github.com/ElementZ76/Test-Automation-Framework-Internal/actions/workflows/ci.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-11+-orange)
![Selenium](https://img.shields.io/badge/Selenium-4.27-green)
![Cucumber](https://img.shields.io/badge/Cucumber-7.14-brightgreen)
![TestNG](https://img.shields.io/badge/TestNG-7.8-red)
![Maven](https://img.shields.io/badge/Build-Maven-yellow)
![Allure](https://img.shields.io/badge/Reports-Allure-orange)

A BDD test automation framework for the [SauceDemo](https://www.saucedemo.com/) e-commerce application. Built with Selenium WebDriver, Cucumber, and TestNG. Includes a full CI/CD pipeline via GitHub Actions with live Allure reporting published to GitHub Pages.

📊 **[View Live Allure Report](https://elementz76.github.io/Test-Automation-Framework-Internal/)**

---

## Table of Contents

- [Project Structure](#project-structure)
- [Tech Stack](#tech-stack)
- [Framework Design](#framework-design)
- [Test Scenarios](#test-scenarios)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running Tests](#running-tests)
- [Reports](#reports)
- [CI/CD Pipeline](#cicd-pipeline)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)

---

## Project Structure

```text
Test-Automation-Framework-Internal/
│
├── .github/
│   └── workflows/
│       └── ci.yml                      # GitHub Actions CI/CD pipeline
│
├── src/test/
│   ├── java/com/automation/
│   │   ├── base/
│   │   │   └── TestBase.java           # Browser setup, teardown, and reusable wrappers
│   │   ├── models/
│   │   │   └── SauceData.java          # POJO for JSON test data deserialization
│   │   ├── pages/                      # Page Object Model (POM)
│   │   │   ├── LoginPage.java
│   │   │   ├── ProductListPage.java
│   │   │   ├── CartPage.java
│   │   │   ├── CheckoutInfoPage.java
│   │   │   ├── CheckoutOverviewPage.java
│   │   │   └── CheckoutCompletePage.java
│   │   ├── runners/
│   │   │   └── TestRunner.java         # TestNG + Cucumber entry point
│   │   ├── stepdef/
│   │   │   ├── StepDef.java            # Cucumber step definitions
│   │   │   └── ApplicationHooks.java   # Before/After hooks, screenshot on failure
│   │   └── utils/
│   │       └── JsonUtils.java          # JSON test data reader (Jackson)
│   │
│   └── resources/
│       ├── features/
│       │   └── Saucedemo.feature       # Gherkin BDD scenarios
│       ├── testdata/
│       │   └── data.json               # Externalized test data
│       ├── config.properties           # Browser, URL, timeout config
│       ├── testng.xml                  # TestNG suite definition
│       └── log4j2.xml                  # Log4j2 logging configuration
│
├── pom.xml                             # Maven dependencies and plugin config
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
| Design Pattern | Page Object Model (POM) |
| CI/CD | GitHub Actions |

---

## Framework Design

### TestBase — Centralized Browser Control

All page classes extend `TestBase`, which provides:

- `initialization()` — launches the configured browser with popups disabled. Automatically runs headless when executed in CI (detected via the `CI` environment variable set by GitHub Actions)
- `clickOn(WebElement)` — click with retry logic and `StaleElementReferenceException` handling
- `sendText(WebElement, String)` — clears and types into inputs with retry logic
- `waitForVisibility(WebElement)` — explicit wait until element is visible
- `waitForClickability(WebElement)` — explicit wait until element is interactable
- `invisibilityOfElement(WebElement)` — waits for element to disappear

### Page Object Model

Each page of the application has a dedicated class. Page classes use `@FindBy` annotations and `PageFactory` for element location, and return the next relevant page object from action methods to support fluent chaining.

### ApplicationHooks — Lifecycle and Screenshot Capture

`@Before` launches the browser before each scenario. `@After` is split into two ordered hooks:

- `order = 1` runs first — captures a screenshot on failure and attaches it to both the Allure report and the Cucumber HTML report
- `order = 0` runs last — safely closes the browser after the screenshot is already taken

### Test Data

All credentials, personal info, product lists, and expected error messages are stored externally in `data.json`. Tests reference data by index, keeping step definitions free of hard-coded values. `JsonUtils` deserializes the JSON into `SauceData` POJOs using Jackson.

---

## Test Scenarios

Defined in `src/test/resources/features/Saucedemo.feature`:

| Tag | Scenario | Description |
| :--- | :--- | :--- |
| `@smoke @regression` | Complete E2E purchase flow | Login → add products → cart → checkout → confirm order |
| `@regression @negative` | Login fails with locked out user | Verifies correct error message for locked accounts |
| `@smoke` | Quick smoke — add single product | Login → add product → verify cart badge count |

---

## Prerequisites

- **Java JDK 11+** — configured in system `PATH`
- **Maven 3.6+** — configured in system `PATH`
- **Google Chrome** — latest stable version
- **Git**
- **IDE** — IntelliJ IDEA or Eclipse (optional for local runs)

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/ElementZ76/Test-Automation-Framework-Internal.git
cd Test-Automation-Framework-Internal
```

### 2. Install dependencies

```bash
mvn clean install -DskipTests
```

### 3. Verify setup

```bash
mvn clean test -Dcucumber.filter.tags="@smoke"
```

Chrome should open, run the smoke test, and close. Results appear in `target/allure-results/`.

---

## Running Tests

### Run all tests

```bash
mvn clean test
```

### Run by tag

```bash
# Smoke tests only
mvn test -Dcucumber.filter.tags="@smoke"

# Full regression suite
mvn test -Dcucumber.filter.tags="@regression"

# Negative test cases
mvn test -Dcucumber.filter.tags="@negative"
```

### Via IDE

1. Open the project as a Maven project
2. Navigate to `src/test/java/com/automation/runners/TestRunner.java`
3. Right-click → **Run As** → **TestNG Test**

---

## Reports

### Allure (recommended)

```bash
# Run tests first, then:
mvn allure:serve
```

Opens a live Allure report in your browser with step-by-step execution details, timing, tags, and screenshots embedded directly inside any failed scenario.

```bash
# Generate static HTML only (no auto-open)
mvn allure:report
# Output: target/site/allure-maven-plugin/index.html
```

### Cucumber HTML

Available at `target/cucumber-reports/cucumber.html` after any test run. Open directly in a browser.

### Live report (CI)

Every push to `main` triggers the pipeline and publishes results automatically:

📊 **https://elementz76.github.io/Test-Automation-Framework-Internal/**

---

## CI/CD Pipeline

The GitHub Actions workflow (`.github/workflows/ci.yml`) runs on every push and pull request to `main`.

### Pipeline stages

```
Push to main
     ↓
Checkout code
     ↓
Set up JDK 11 (with Maven cache)
     ↓
Run tests (headless Chrome, all scenarios)
     ↓
Upload artifacts: allure-results, cucumber-report, logs
     ↓
Generate Allure HTML report
     ↓
Deploy to GitHub Pages (gh-pages branch)
```

### Key behaviours

- Tests run in **headless Chrome** automatically on CI — no configuration needed
- Tests run in **headed Chrome** locally — browser opens visibly as expected
- The pipeline continues even if tests fail (`continue-on-error: true`) so reports are always generated and uploaded
- Artifacts (results, reports, logs) are retained for 7 days per run
- The Allure report keeps a history of the last 20 runs for trend analysis

### Manual trigger

The pipeline can also be triggered manually from the **Actions** tab in GitHub using the `workflow_dispatch` event — no push required.

---

## Configuration

All environment settings are in `src/test/resources/config.properties`:

```properties
url = https://www.saucedemo.com/
browser = chrome
implicitwait = 10
```

| Property | Description | Accepted Values |
| :--- | :--- | :--- |
| `url` | Base URL of the application | Any valid URL |
| `browser` | Browser to launch | `chrome`, `firefox`, `edge` |
| `implicitwait` | Implicit wait timeout in seconds | Integer |

No code changes are needed to switch browsers or environments — edit this file only.

---

## Troubleshooting

### Chrome does not open locally

Confirm `--headless` is not hardcoded in `TestBase.java`. The framework detects CI automatically via `System.getenv("CI")`. Locally this is `null`, so headed mode is used. On GitHub Actions it is `"true"`, so headless mode activates.

### Maven dependencies not downloading

```bash
mvn clean install -U
```

In Eclipse: right-click project → **Maven** → **Update Project** → check **Force Update** → OK.

### TestNG plugin missing in Eclipse

Go to **Help** → **Eclipse Marketplace** → search **TestNG for Eclipse** → install → restart Eclipse.

### Allure report is blank after CI run

Check the **Actions** run log for compilation errors. The most common cause is a filename case mismatch — Java class names must exactly match their filenames (case-sensitive on Linux). For example, `CartPage.java` not `cartPage.java`.

### Tests compile locally but fail on CI

Ensure all source files are committed and tracked by Git:

```bash
git ls-files src/
```

Every `.java` file should appear. If any are missing, stage and commit them explicitly:

```bash
git add src/
git commit -m "fix: ensure all source files are tracked"
git push origin main
```
