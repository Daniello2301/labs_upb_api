FROM openjdk:17-jdk-slim
LABEL authors="Daniel Lopera Gómez"
VOLUME /tmp
ARG JAR_FILE=target/Labs_UPB-0.0.2.jar
COPY ${JAR_FILE} app_labs_iee.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app_labs_iee.jar"]

