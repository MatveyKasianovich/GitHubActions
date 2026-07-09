FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw -B dependency:go-offline

COPY src/ src/

RUN ./mvnw -B clean package -DskipTests
RUN find target -name "*.jar" -not -name "*.original" -exec mv {} target/app.jar \;

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/target/app.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "/app/app.jar"]