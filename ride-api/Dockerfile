# Контекст сборки — корень taxi-dispatcher (нужны parent pom и proto/)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY proto proto
COPY ride-api ride-api
COPY dispatch-service/pom.xml dispatch-service/pom.xml
RUN mvn -q -pl ride-api -am -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/ride-api/target/ride-api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
