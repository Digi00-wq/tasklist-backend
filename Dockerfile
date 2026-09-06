# Ein winziges Linux-Image, das nur Java 17 installiert hat
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Kopiert deine fertige .jar Datei in den Container
COPY target/*.jar app.jar

# Öffnet Port 8080 nach außen
EXPOSE 8080

# Der Startbefehl
ENTRYPOINT ["java", "-jar", "app.jar"]
