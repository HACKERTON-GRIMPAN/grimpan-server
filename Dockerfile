FROM openjdk:17-alpine

WORKDIR /app

ARG JAR_PATH=./build/libs

COPY ${JAR_PATH}/DrawingDiary-0.0.1-SNAPSHOT.jar ${JAR_PATH}/DrawingDiary-0.0.1-SNAPSHOT.jar

CMD ["java","-jar","./build/libs/DrawingDiary-0.0.1-SNAPSHOT.jar"]