package com.automation.listeners;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;


public class SuiteThreadListener implements IAlterSuiteListener {

    private static final Logger log = LogManager.getLogger(SuiteThreadListener.class);
    private static final String CONFIG_PATH = "src/test/resources/config.properties";

    @Override
    public void alter(List<XmlSuite> suites) {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            prop.load(fis);
        } catch (Exception e) {
            log.warn("SuiteThreadListener: could not read {}. Thread count unchanged. ({})",
                    CONFIG_PATH, e.getMessage());
            return;
        }

        String sysProp   = System.getProperty("threads");
        String configVal = prop.getProperty("threads", "").trim();

        String resolved = (sysProp != null && !sysProp.trim().isEmpty())
                ? sysProp.trim()
                : configVal;

        if (resolved.isEmpty()) {
            log.warn("SuiteThreadListener: 'threads' not found in -D args or config. Defaulting to 1.");
            resolved = "1";
        }

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
                log.info("SuiteThreadListener: parallel mode was NONE, set to METHODS.");
            }
            log.info("SuiteThreadListener: suite '{}' thread-count set to {} (source: {})",
                    suite.getName(), threadCount,
                    (sysProp != null && !sysProp.trim().isEmpty()) ? "-D system property" : "config.properties");
        }
    }
}
