# Web Automation Framework

![Java](https://img.shields.io/badge/Java-11+-orange) ![Selenium](https://img.shields.io/badge/Selenium-4.x-green) ![TestNG](https://img.shields.io/badge/TestNG-7.x-red) ![Maven](https://img.shields.io/badge/Build-Maven-yellow)

## Overview
A clean, maintainable **Selenium-based Test Automation Framework** designed for web application testing. Built with industry best practices, this framework leverages the **Page Object Model (POM)** design pattern to ensure code reusability, maintainability, and scalability.

## Project Structure
```text
src/
├── main/java/com/automation/
│   ├── base/
│   │   └── TestBase.java
│   ├── models/
│   │   └── SauceData.java
│   ├── pages/
│   │   ├── LoginPage.java
│   │   ├── ProductListPage.java
│   │   ├── CartPage.java
│   │   ├── CheckoutInfoPage.java
│   │   ├── CheckoutOverviewPage.java
│   │   └── CheckoutCompletePage.java
│   └── utils/
│       └── JsonUtils.java
├── test/java/com/automation/
│   └── stepdefinitions/
│       └── StepDefinitions.java
└── test/resources/
    ├── features/
    │   └── Saucedemo.feature
    ├── testdata/
    │   └── testdata.json
    └── config.properties
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

## How to Run Locally

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
