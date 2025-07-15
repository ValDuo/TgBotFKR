package com.javarush.telegram;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Не найден config.properties");
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Error loading config", e);
        }
    }

    public static String getBotName() {
        return props.getProperty("TELEGRAM_BOT_NAME");
    }

    public static String getBotToken() {
        return props.getProperty("TELEGRAM_BOT_TOKEN");
    }

    public static String getAIToken() {
        return props.getProperty("OPEN_AI_TOKEN");
    }
}