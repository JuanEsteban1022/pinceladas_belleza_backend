# ----------------------------------------------------
# STAGE 1: Build (Construir la aplicación y crear el JAR)
# ----------------------------------------------------
# Usamos una imagen que ya tiene Gradle y Java 17
FROM gradle:8.4.0-jdk17 AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos de configuración de Gradle para cachear dependencias
COPY build.gradle settings.gradle ./
COPY gradlew gradle ./

# Copia el código fuente completo
COPY src ./src

# Construye la aplicación y genera el JAR ejecutable
RUN ./gradlew bootJar

# ----------------------------------------------------
# STAGE 2: Runtime (Ejecutar el JAR en un entorno ligero)
# ----------------------------------------------------
# Usamos una imagen más pequeña que solo tiene el JRE (Runtime Environment)
FROM eclipse-temurin:17-jre-jammy

# Define el puerto que tu aplicación Spring Boot usará (Render lo necesita)
EXPOSE 8080

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR desde la etapa de construcción anterior
# La ruta por defecto de Gradle es build/libs/*.jar
COPY --from=build /app/build/libs/*.jar app.jar

# Comando para ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
