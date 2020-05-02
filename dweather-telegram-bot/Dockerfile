FROM openjdk:11-jdk-slim
COPY /build/libs/dweather-telegram-bot-0.0.1-SNAPSHOT.jar /usr/local/dweather-telegram-bot/dweather-telegram-bot.jar
WORKDIR /usr/local/dweather-telegram-bot/
ENTRYPOINT ["java","-jar", "dweather-telegram-bot.jar"]