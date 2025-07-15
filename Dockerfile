FROM bellsoft/liberica-openjdk-debian:17
WORKDIR /app
COPY target/TinderBolt-1.0-SNAPSHOT.jar app.jar
COPY src/main/resources/config.properties ./config/
CMD ["java", "-jar", "app.jar"]