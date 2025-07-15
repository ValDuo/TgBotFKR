package com.javarush.telegram;

import com.javarush.telegram.ChatGPTService;
import com.javarush.telegram.DialogMode;
import com.javarush.telegram.MultiSessionTelegramBot;
import com.javarush.telegram.UserInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import static cn.hutool.core.lang.Console.print;


public class TinderBoltApp extends MultiSessionTelegramBot {
    private static final Properties props = new Properties();
    private static final Logger log = LoggerFactory.getLogger(TinderBoltApp.class);

    //получение данных конфиги
    static {
        try (InputStream input = TinderBoltApp.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new IllegalStateException("Файл config.properties не найден в classpath");
            }
            props.load(input);

        } catch (Exception e) {
            log.error("Ошибка загрузки конфигурации", e);
            throw new RuntimeException("Не удалось загрузить конфигурацию", e);
        }
    }

    public static final String TELEGRAM_BOT_NAME = getRequiredProperty("BOT_NAME");
    public static final String TELEGRAM_BOT_TOKEN = getRequiredProperty("BOT_TOKEN");
    public static final String OPEN_AI_TOKEN = getRequiredProperty("AI_TOKEN");

    private static String getRequiredProperty(String key) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Не найден обязательный параметр: " + key);
        }
        return value.trim();
    }

    private ChatGPTService chatGPT = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode currentMode = null;

    public TinderBoltApp() {

        super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);
    }

    @Override
    public void onUpdateEventReceived(Update update) {
        String message = getMessageText();

        if(message.equals("/start")){
            currentMode = DialogMode.MAIN;
            sendPhotoMessage("main");
            String text = loadMessage("main");
            sendTextMessage(text);
            sendTextButtonsMessage("Основные вопросы:",
                    "Почему нет ответа на мое обращение?", "otvet",
                    "Как связаться с Вами? ", "message",
                    "Как сделать перерасчет? ", "pereraschet",
                    "Как мне сменить собственника?", "sobstvennik",
                    "Включен ли мой дом в программу ремонта?", "kapremont",
                    "Зарегистрировано ли мое письмо?", "letter",
                    "Почему не работает личный кабинет? ", "lichnkab",
                    "Как получать квитанцию на электронную почту?", "kvitancia",
                    "Другой вопрос", "another");

            showMainMenu("Главное меню бота", "/start");
            return;
        }

//        if(message.equals("/gpt")){
//            currentMode = DialogMode.GPT;
//            String text = loadMessage("gpt");
//            sendPhotoMessage("gpt");
//            sendTextMessage(text);
//            return;
//        }

        if (currentMode == DialogMode.MAIN && !isMessageCommand()){
            String query = getCallbackQueryButtonKey();
            if(query.startsWith("otve")) {
                String text = loadMessage("otvet");
                sendTextMessage(text);
                sendTextButtonsMessage("Остались вопросы?",
                        "На главную", "main");

                return;
            }

            if(query.startsWith("mess")){
                String text = loadMessage("message");
                sendTextMessage(text);
                sendTextButtonsMessage("Остались вопросы?",
                        "На главную", "main");
                return;
            }

            if(query.startsWith("pere")){
                String text = loadMessage("pereraschet");
                sendTextMessage(text);
                sendTextButtonsMessage("Остались вопросы?",
                        "На главную", "main");
                return;
            }

            if(query.startsWith("sobs")){
                String text = loadMessage("sobstvennik");
                sendTextMessage(text);
                sendTextButtonsMessage("Остались вопросы?",
                        "На главную", "main");
                return;
            }

            if(query.startsWith("kap")){
                String text = loadMessage("kapremont");
                sendTextMessage(text);
                sendTextButtonsMessage("Остались вопросы?",
                        "На главную", "main");
                return;
            }

            if(query.startsWith("let")){
                String text = loadMessage("letter");
                sendTextMessage(text);
                sendTextButtonsMessage("Остались вопросы?",
                        "На главную", "main");
                return;
            }

            if(query.startsWith("lich")){
                String text = loadMessage("lichnkab");
                sendTextMessage(text);
                sendTextButtonsMessage("Остались вопросы?",
                        "На главную", "main");
                return;
            }

            if(query.startsWith("kvit")){
                String text = loadMessage("kvitancia");
                sendTextMessage(text);
                sendTextButtonsMessage("Остались вопросы?",
                        "На главную", "main");
                return;
            }

            if(query.startsWith("kvit")){
                String text = loadMessage("kvitancia");
                sendTextMessage(text);
                sendTextButtonsMessage("Остались вопросы?",
                        "На главную", "main");
                return;
            }
            if(query.startsWith("ano")){
                String text = loadMessage("another");
                sendTextMessage(text);

                return;
            }
            if(query.startsWith("ma")){
                sendPhotoMessage("main");
                String text = loadMessage("main");
                sendTextMessage(text);
                sendTextButtonsMessage("Основные вопросы:",
                        "Почему нет ответа на мое обращение?", "otvet",
                        "Как связаться с Вами? ", "message",
                        "Как сделать перерасчет? ", "pereraschet",
                        "Как мне сменить собственника?", "sobstvennik",
                        "Включен ли мой дом в программу ремонта?", "kapremont",
                        "Зарегистрировано ли мое письмо?", "letter",
                        "Почему не работает личный кабинет? ", "lichnkab",
                        "Как получать квитанцию на электронную почту?", "kvitancia",
                        "Другой вопрос", "another");

                showMainMenu("Главное меню бота", "/start");
                return;
            }



        }

//
//        if(currentMode == DialogMode.DATE && !isMessageCommand()){
//            String query = getCallbackQueryButtonKey();
//            if(query.startsWith("date_")){
//                sendPhotoMessage(query);
//                sendTextMessage("Отличный выбор! \nТвоя задача пригласить девушку/парня на свидание за 5 сообщений: ");
//
//                String promt = loadPrompt(query);
//                chatGPT.setPrompt(promt);
//                return;
//            }
//
//
//            Message msg = sendTextMessage("Подождите, собеседник набирает текст...");
//            String answer = chatGPT.addMessage(message);
//            updateTextMessage(msg, answer);
//            return;
//        }
//
//        if(message.equals("/message")){
//            currentMode = DialogMode.MESSAGE;
//            sendPhotoMessage("message");
//            sendTextButtonsMessage("Пришлите в чат вашу переписку","Следующее сообщение","message_next",
//                    "Пригласить на свидание","message_date");
//            return;
//        }
//
//        if(currentMode == DialogMode.MESSAGE && !isMessageCommand()){
//            String query = getCallbackQueryButtonKey();
//            if(query.startsWith("message_")){
//                String promt = loadPrompt(query);
//                String userChatList = String.join("\n\n", list);
//
//                Message msg = sendTextMessage("Подождите пару секунд - ChatGPT думает...");
//                String answer = chatGPT.sendMessage(promt, userChatList);
//                updateTextMessage(msg, answer);
//                return;
//            }
//            list.add(message);
//            return;
//        }
//
//        if(message.equals("/profile")){
//            currentMode = DialogMode.PROFILE;
//            sendPhotoMessage("profile");
//
//            me = new UserInfo();
//            questionCount = 1;
//            sendTextMessage("Сколько вам лет?");
//            return;
//        }
//
//        if(currentMode == DialogMode.PROFILE && !isMessageCommand()){
//            switch (questionCount){
//                case 1:
//                    me.age = message;
//                    questionCount = 2;
//                    sendTextMessage("Кем вы работаете?");
//                    return;
//                case 2:
//                    me.occupation = message;
//                    questionCount = 3;
//                    sendTextMessage("У вас есть хобби?");
//                    return;
//                case 3:
//                    me.hobby = message;
//                    questionCount = 4;
//                    sendTextMessage("Что вам не нравится в людях?");
//                    return;
//                case 4:
//                    me.hobby = message;
//                    questionCount = 5;
//                    sendTextMessage("Цель знакомства?");
//                    return;
//                case 5:
//                    me.goals = message;
//
//                    String aboutMyself = me.toString();
//                    String promt = loadPrompt("profile");
//                    Message msg = sendTextMessage("Подождите пару секунд - ChatGPT думает...");
//                    String answer = chatGPT.sendMessage(promt, aboutMyself);
//                    updateTextMessage(msg,answer);
//                    return;
//            }
//            return;
//        }
//
//        if(message.equals("/opener")){
//            currentMode = DialogMode.OPENER;
//            sendPhotoMessage("opener");
//
//            she = new UserInfo();
//            questionCount = 1;
//            sendTextMessage("Пришли информацию о человеке для знакомства:\nИмя девушки?");
//            return;
//        }
//
//        if(currentMode == DialogMode.OPENER && !isMessageCommand()){
//            switch (questionCount){
//                case 1:
//                    she.name = message;
//                    questionCount = 2;
//                    sendTextMessage("Сколько ей лет?");
//                    return;
//                case 2:
//                    she.age = message;
//                    questionCount = 3;
//                    sendTextMessage("Есть ли у нее хобби?");
//                    return;
//                case 3:
//                    she.hobby = message;
//                    questionCount = 4;
//                    sendTextMessage("Кем она работает?");
//                    return;
//                case 4:
//                    she.occupation = message;
//                    questionCount = 5;
//                    sendTextMessage("Цель знакомства?");
//                    return;
//                case 5:
//                    she.goals = message;
//                    String aboutFriend = message;
//                    String promt = loadPrompt("opener");
//                    Message msg = sendTextMessage("Подождите пару секунд - ChatGPT думает...");
//                    String answer = chatGPT.sendMessage(promt, aboutFriend);
//                    updateTextMessage(msg,answer);
//                    return;
//            }
//            return;
//        }
//

        sendTextMessage("Отправьте мне одну из существующих команд: " + message);
        showMainMenu("Главное меню бота", "/start");

    }
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }

}
