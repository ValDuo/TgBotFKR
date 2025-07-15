FROM openjdk:21
WORKDIR /src/main/java/com/javarush/telegram
COPY TinderBoltApp.java .
CMD ["java", "-jar", "tgbot.jar"]