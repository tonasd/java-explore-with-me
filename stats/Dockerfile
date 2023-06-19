FROM amazoncorretto:11-alpine-jdk
LABEL authors="tonasd"
ARG JAR_FILE=service/target/*.jar
COPY ${JAR_FILE} stats-service.jar
ENTRYPOINT ["java","-jar","/stats-service.jar"]
