FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml ./pom.xml
COPY common-lib ./common-lib
RUN mvn -q -f pom.xml install -N -DskipTests
RUN mvn -q -f common-lib/pom.xml install -DskipTests
COPY notification-service ./notification-service
RUN mvn -q -f notification-service/pom.xml package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/notification-service/target/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
