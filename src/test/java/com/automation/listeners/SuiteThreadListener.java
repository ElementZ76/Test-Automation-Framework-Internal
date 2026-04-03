package com.automation.listeners;

import com.automation.utils.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;

import java.util.List;

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
    }
}