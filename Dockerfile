# STAGE 1: Bauen (Kompilieren) mit Java JDK
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
# Kopiert den Maven-Wrapper und deinen Code in den Container
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY src ./src
# Baut die neue .jar Datei direkt im Container
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# STAGE 2: Ausführen mit kleinem Java JRE
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Holt sich die frisch gebaute .jar aus Stage 1
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
