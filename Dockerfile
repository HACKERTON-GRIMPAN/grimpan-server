FROM openjdk:17-alpine

WORKDIR /app

ARG JAR_PATH=./build/libs

COPY ${JAR_PATH}/EmoDiary-0.0.1-SNAPSHOT.jar app.jar

CMD ["java","-jar","./app.jar","--spring.profiles.active=dev"]