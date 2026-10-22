FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:25-jre

WORKDIR /app
RUN useradd --system --uid 10001 --create-home appuser
COPY --from=build --chown=10001:10001 /workspace/target/employeedirectory-*.jar /app/app.jar

ENV SERVER_PORT=8086
EXPOSE 8086
USER 10001:10001

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
