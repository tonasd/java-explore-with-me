FROM amazoncorretto:11-alpine-jdk
LABEL authors="tonasd"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} server.jar
ENTRYPOINT ["java","-jar","/server.jar"]
