//package com.javarush.telegram;
//
//import java.io.InputStream;
//import java.util.Properties;
//
//public final class ConfigLoader {
//    private static final Properties props = new Properties();
//    private static final String CONFIG_FILE = "config.properties";
//
//    static {
//        loadConfiguration();
//    }
//
//    private static void loadConfiguration() {
//        try (InputStream input = ConfigLoader.class.getClassLoader()
//                .getResourceAsStream(CONFIG_FILE)) {
//
//            if (input == null) {
//                throw new IllegalStateException(
//                        "Configuration file '" + CONFIG_FILE + "' not found in classpath. " +
//                                "Place it in src/main/resources/"
//                );
//            }
//
//            props.load(input);
//
//            // Проверка обязательных параметров
//            validateRequiredProperties();
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to load configuration", e);
//        }
//    }
//
//    private static void validateRequiredProperties() {
//        checkProperty(TELEGRAM_BOT_NAME);
//        checkProperty(TELEGRAM_BOT_TOKEN);
//        checkProperty(OPEN_AI_TOKEN);
//    }
//
//    private static void checkProperty(String key) {
//        if (!props.containsKey(key)) {
//            throw new IllegalStateException(
//                    "Missing required property in config: '" + key + "'"
//            );
//        }
//    }
//
//    // Методы для получения значений
//    public static String getBotName() {
//        return getRequiredProperty(TELEGRAM_BOT_NAME);
//    }
//
//    public static String getBotToken() {
//        return getRequiredProperty(TELEGRAM_BOT_TOKEN);
//    }
//
//    public static String getAIToken() {
//        return getRequiredProperty(OPEN_AI_TOKEN);
//    }
//
//    public static String getOptionalProperty(String key, String defaultValue) {
//        return props.getProperty(key, defaultValue).trim();
//    }
//
//    private static String getRequiredProperty(String key) {
//        String value = props.getProperty(key);
//        if (value == null || value.trim().isEmpty()) {
//            throw new IllegalStateException(
//                    "Property '" + key + "' is missing or empty in configuration"
//            );
//        }
//        return value.trim();
//    }
//}