# Контекст сборки — корень taxi-dispatcher (нужны parent pom и proto/)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY proto proto
COPY dispatch-service dispatch-service
COPY ride-api/pom.xml ride-api/pom.xml
RUN mvn -q -pl dispatch-service -am -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/dispatch-service/target/dispatch-service-*.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]
