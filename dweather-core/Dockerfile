FROM openjdk:11-jdk-slim
COPY /build/libs/dweather-core-0.0.1-SNAPSHOT.jar /usr/local/dweather-core/dweather-core.jar
WORKDIR /usr/local/dweather-core/
EXPOSE 8080
ENTRYPOINT ["java","-jar", "dweather-core.jar"]