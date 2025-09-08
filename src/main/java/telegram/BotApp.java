package telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.InputStream;
import java.util.Properties;

import static cn.hutool.core.lang.Console.print;


public class BotApp extends MultiSessionTelegramBot {
    private static final Properties props = new Properties();
    private static final Logger log = LoggerFactory.getLogger(BotApp.class);

    //получение данных конфиги
    static {
        try (InputStream input = BotApp.class.getClassLoader()
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

    public BotApp() {

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

            showMainMenu("- открыть главное меню бота", "/start", "- задать вопрос искусственному интеллекту", "/gpt");
            return;
        }

        if(message.equals("/gpt")){
            currentMode = DialogMode.GPT;
            String text = loadMessage("gpt");
            sendPhotoMessage("gpt");
            sendTextMessage("Напишите любое сообщение и посмотрим, что вам ответит *наша нейросеть*: ");
            String question = getMessageText();

            String promt = loadPrompt(question);
            Message msg = sendTextMessage("Подождите пару секунд - ChatGPT думает...");
            String answer = chatGPT.sendMessage(promt,question);
            updateTextMessage(msg, answer);

            return;
        }

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

                showMainMenu("- открыть главное меню бота", "/start", "- задать вопрос искусственному интеллекту", "/gpt");
                return;
            }



        }


        sendTextMessage("Отправьте мне одну из существующих команд из Menu" );
        showMainMenu("- открыть главное меню бота", "/start", "- задать вопрос искусственному интеллекту", "/gpt");

    }
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new BotApp());
    }

}
