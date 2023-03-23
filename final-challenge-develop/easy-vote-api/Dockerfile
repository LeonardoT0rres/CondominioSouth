FROM maven:3.8.7-eclipse-temurin-11-alpine AS build

WORKDIR /tmp
COPY ./pom.xml ./
COPY ./src ./src

RUN mvn clean package -Dmaven.test.skip

FROM openjdk:11

VOLUME /tmp
ARG JAR_FILE=/*.jar
COPY --from=build /tmp/target${JAR_FILE} easy-vote-api.jar

ENTRYPOINT ["java", "-jar", "easy-vote-api.jar"]
