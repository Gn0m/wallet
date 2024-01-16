FROM openjdk:20
LABEL authors="Leonid"

WORKDIR /wallet-app

ARG APP_JAR=wallet-0.0.1-SNAPSHOT.jar

COPY build/libs/${APP_JAR} wallet-app.jar

ENTRYPOINT ["java", "-jar", "wallet-app.jar"]

EXPOSE 8080