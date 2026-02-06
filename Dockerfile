# ----------------------------------------------------
# STAGE 1: Build (Gradle + Java 11)
# ----------------------------------------------------
FROM gradle:7.6-jdk11 AS build

WORKDIR /app

# Copiamos todo el proyecto
COPY . .

# Construimos el JAR ejecutable (sin tests)
RUN gradle clean bootJar -x test

# ----------------------------------------------------
# STAGE 2: Runtime (Java 11 ligero)
# ----------------------------------------------------
FROM eclipse-temurin:11-jre-jammy

WORKDIR /app

EXPOSE 8080

# Copiamos el JAR generado
COPY --from=build /app/build/libs/*.jar app.jar

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
