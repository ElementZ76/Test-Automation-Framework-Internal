# Web Automation Framework - SauceDemo Test

![Java](https://img.shields.io/badge/Java-11+-orange) ![Selenium](https://img.shields.io/badge/Selenium-4.x-green) ![TestNG](https://img.shields.io/badge/TestNG-7.x-red) ![Maven](https://img.shields.io/badge/Build-Maven-yellow) ![CI](https://github.com/ElementZ76/Test-Automation-Framework-Internal/actions/workflows/ci.yml/badge.svg)

## Overview
A comprehensive BDD test automation framework for SauceDemo e-commerce application using Selenium WebDriver, Cucumber, and TestNG with Allure reporting.

## Project Structure
```text
Test-Automation-Framework-Internal/
│
├── src/
│   ├── main/java/com/automation/
│   │   ├── base/
│   │   │   └── TestBase.java           # Base class with browser setup and reusable methods
│   │   ├── models/
│   │   │   └── SauceData.java          # POJO for JSON test data deserialization
│   │   ├── pages/                      # Page Object Model (POM)
│   │   │   ├── LoginPage.java
│   │   │   ├── ProductListPage.java
│   │   │   ├── CartPage.java
│   │   │   ├── CheckoutInfoPage.java
│   │   │   ├── CheckoutOverviewPage.java
│   │   │   └── CheckoutCompletePage.java
│   │   └── utils/
│   │       └── JsonUtils.java          # Utility for reading JSON test data
│   │
│   └── test/
│       ├── java/com/automation/
│       │   ├── hooks/
│       │   │   └── Hooks.java          # Cucumber hooks (setup/teardown, screenshots)
│       │   ├── runners/
│       │   │   └── TestRunner.java     # TestNG runner with Cucumber integration
│       │   └── stepdef/
│       │       └── StepDef.java        # Cucumber step definitions
│       │
│       └── resources/
│           ├── features/
│           │   └── Saucedemo.feature   # BDD feature file (Gherkin syntax)
│           ├── testdata/
│           │   └── data.json           # Test data in JSON format
│           ├── config.properties       # Application configuration
│           ├── testng.xml              # TestNG suite configuration
│           └── log4j2.xml              # Logging configuration
│
├── target/
│   ├── allure-results/                 # Allure test results
│   └── cucumber-reports/               # Cucumber HTML reports
│
├── pom.xml                             # Maven dependencies and plugins
└── README.md                           # This file
```

## Tech Stack
| Component | Tool / Library |
| :--- | :--- |
| **Language** | Java (JDK 11+) |
| **Web Automation** | Selenium WebDriver |
| **Test Framework** | TestNG |
| **Build Tool** | Maven |
| **Design Pattern** | Page Object Model (POM) |
| **Version Control** | Git/GitHub |

## Prerequisites
* **Java (JDK):** JDK 11 or higher installed and configured in system path
* **Maven:** Version 3.6+ 
* **IDE:** Eclipse, IntelliJ IDEA, or VS Code
* **Git:** For version control

## How to Run
### 1. Clone the repo
```bash
git clone https://github.com/ElementZ76/Test-Automation-Framework-Internal.git
cd Test-Automation-Framework-Internal
```

### 2. Install dependencies
```bash
mvn clean install -DskipTests
```

### 3. Run All Tests
```bash
mvn clean test
```

### 4. Run Specific Tags
#### Run just smoke test
```bash
mvn test -Dcucumber.filter.tags="@smoke"
```
#### Run regression tests
```bash
mvn test -Dcucumber.filter.tags="@regression"
```

#### Run negative tests
```bash
mvn test -Dcucumber.filter.tags="@negative"
```

### 5. Generate Allure Report
```bash
# Generate and view report in browser
mvn allure:serve
# OR generate report only
mvn allure:report
# Report location: target/site/allure-maven-plugin/index.html
```

## How to Run via Eclipse/IntelliJ IDE

### 1. Clone the Repository
```bash
git clone <repository-url>
cd TestAutomationFramework
```

### 2. Import into IDE

**For Eclipse:**
1. Open Eclipse
2. Go to **File** > **Import...**
3. Expand **Maven** folder and select **Existing Maven Projects**
4. Click **Next**
5. Browse and select the project root folder (containing `pom.xml`)
6. Ensure `pom.xml` checkbox is selected
7. Click **Finish**

*Note: Wait for Maven to download dependencies (check progress bar at bottom right)*

**For IntelliJ IDEA:**
1. Open IntelliJ IDEA
2. Select **File** > **Open**
3. Navigate to project folder and select it
4. Click **OK**
5. IntelliJ will automatically detect and import the Maven project

### 3. Update Configuration
Edit `src/test/resources/config.properties` to set your test environment:
```properties
app.url=https://your-application-url.com
browser=chrome
implicit.wait=10
explicit.wait=20
```

### 4. Run Tests

**Via Maven (Command Line):**
```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=LoginTest
```

**Via IDE:**
1. Navigate to test class in `src/test/java/com.automation.runners`
2. Right-click the test class or test method
3. Select **Run As** > **TestNG Test**

## Configuration

All test settings are centralized in `src/test/resources/config.properties`:

| Property | Description | Example Values |
|----------|-------------|----------------|
| `app.url` | Base URL of application under test | `https://demo.app.com` |
| `browser` | Browser type | `chrome`, `firefox`, `edge` |
| `implicit.wait` | Default implicit wait (seconds) | `10` |
| `explicit.wait` | Explicit wait timeout (seconds) | `20` |

**To change settings:** Simply edit `config.properties` - no code modifications needed.

## Troubleshooting

### Maven Dependencies Not Downloading
```bash
# Force update Maven project
mvn clean install -U
```

**In Eclipse:**
- Right-click project > **Maven** > **Update Project**
- Check **Force Update of Snapshots/Releases**
- Click **OK**

### TestNG Not Found in Eclipse
1. Go to **Help** > **Eclipse Marketplace**
2. Search for "TestNG for Eclipse"
3. Install the plugin
4. Restart Eclipse
5. Right-click project > **Maven** > **Update Project**

## License
This project is intended for internal use and learning purposes.
