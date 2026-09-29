FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY habit-tracker-db/pom.xml habit-tracker-db/pom.xml
COPY habit-tracker-impl/pom.xml habit-tracker-impl/pom.xml

COPY habit-tracker-db/src habit-tracker-db/src
COPY habit-tracker-impl/src habit-tracker-impl/src

RUN mvn -B -pl habit-tracker-impl -am package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/habit-tracker-impl/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]