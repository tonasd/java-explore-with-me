FROM amazoncorretto:11-alpine-jdk
LABEL authors="tonasd"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ewm-stats-service.jar
ENTRYPOINT ["java","-jar","/ewm-stats-service.jar"]
