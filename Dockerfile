# ----------------------------------------------------
# STAGE 1: Build (Gradle + Java 11)
# ----------------------------------------------------
FROM gradle:7.6-jdk11 AS build

WORKDIR /app

# Copiamos todo el proyecto
COPY . .

# Construimos el JAR ejecutable (sin daemon, sin tests)
RUN gradle clean bootJar -x test --no-daemon

# ----------------------------------------------------
# STAGE 2: Runtime (Java 11 ligero)
# ----------------------------------------------------
FROM eclipse-temurin:11-jre-jammy

WORKDIR /app

# Railway usa puerto dinámico
ENV PORT=8080
EXPOSE 8080

# Copiamos el JAR generado por Gradle
COPY --from=build /app/build/libs/*.jar app.jar

# Ejecutamos la aplicación (limitando memoria para free tier)
ENTRYPOINT ["java", "-Xms128m", "-Xmx512m", "-jar", "app.jar"]
