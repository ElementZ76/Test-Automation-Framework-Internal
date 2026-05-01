package com.automation.utils;


import java.io.FileInputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigManager {
    private static final Logger log = LogManager.getLogger(String.valueOf(ConfigManager.class));
    public static final Properties prop = new Properties();
    private static final String CONFIG_PATH = "src/test/resources/config.properties";

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            prop.load(fis);
            log.info("config.properties loaded successfully into ConfigManager");
        } catch (Exception e) {
            log.info("Cannot read config.properties. Aborting.");
            throw new RuntimeException("Failed to load config properties", e);
        }

        String appName = System.getProperty("appName", prop.getProperty("appName"));
        if (appName != null && !appName.trim().isEmpty()) {
            String appConfig = "src/test/resources/" + appName.trim() + ".properties";
            try (FileInputStream fis = new FileInputStream(appConfig)) {
                prop.load(fis);
                log.info("{}.properties loaded and merged into ConfigManager", appName);
            } catch (Exception e) {
                log.warn("No {}.properties found for appName='{}'. Using config.properties only.", appName, appName);
            }
        }
    }

    /**
     * Resolves key from System properties. If not found, it falls back to set default
     * @param key key to look for in config file
     * @param hardDefault to use when key is not found
     * @return set key if found in config, else return hard default
     */
    public static String get(String key, String hardDefault) {
        String sysProp = System.getProperty(key);
        if (sysProp!=null && !sysProp.trim().isEmpty()) {
            return sysProp.trim();
        }
        String configVal = prop.getProperty(key);
        if(configVal!=null && !configVal.trim().isEmpty()) {
            return configVal.trim();
        }
        log.warn("'{} not found. Using built in default: '{}''", key, hardDefault);
        return hardDefault;
    }

    public static String get(String key) {
        return get(key, " ");
    }

}