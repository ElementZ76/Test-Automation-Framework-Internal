package com.automation.listeners;

import com.automation.utils.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class SuiteThreadListener implements IAlterSuiteListener {
    private static final Logger log = LogManager.getLogger(SuiteThreadListener.class);

    @Override
    public void alter(List<XmlSuite> suites) {
        String resolved = ConfigManager.get("threads", "1");
        int threadCount;

        try {
            threadCount = Integer.parseInt(resolved);
            if (threadCount < 1) throw new NumberFormatException("must be >= 1");
        } catch (NumberFormatException e) {
            log.error("SuiteThreadListener: invalid threads value '{}'. Defaulting to 1.", resolved);
            threadCount = 1;
        }

        for (XmlSuite suite : suites) {
            suite.setThreadCount(threadCount);
            suite.setDataProviderThreadCount(threadCount);
            if (suite.getParallel() == XmlSuite.ParallelMode.NONE) {
                suite.setParallel(XmlSuite.ParallelMode.METHODS);
            }
            log.info("SuiteThreadListener: suite '{}' thread-count set to {}", suite.getName(), threadCount);
        }
        writeAllureEnvironment();
    }

    private void writeAllureEnvironment() {
        Properties env = new Properties();
        env.setProperty("Browser", ConfigManager.get("browser", "crhome"));
        env.setProperty("Execution.Mode", ConfigManager.get("executionMode", "local"));
        env.setProperty("Threads", ConfigManager.get("threads", "1"));
        env.setProperty("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
        env.setProperty("Java.Version", System.getProperty("java.version"));
        env.setProperty("URL", ConfigManager.get("url", " "));

        String allureResultDir=System.getProperty("allure.results.directory", "target/allure-results");
        try {
        Path dir = Paths.get(allureResultDir);
        Files.createDirectories(dir);
        try (OutputStream out = Files.newOutputStream(dir.resolve("environment.properties"))) {
            env.store(out,null);
        }
        log.info("Allure environment.properties written to {}.", allureResultDir);
    } catch (IOException e) {
            log.warn("Failed to write Allure environment.properties: {}", e.getMessage());
    }
    }
}