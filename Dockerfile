# Etapa 1: Construir el proyecto Spring Boot
FROM maven:3.8.4-openjdk-17-slim AS build

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el pom.xml y descargar las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente del proyecto
COPY src ./src

# Compilar el proyecto y generar el archivo JAR
RUN mvn clean package -DskipTests

# Etapa 2: Crear la imagen con solo el JAR y los archivos estáticos de React
FROM openjdk:17-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR generado por Maven
COPY --from=build /app/target/*.jar app.jar

# Copiar los archivos estáticos de React desde resources/static
COPY ./src/main/resources/static /app/static

# Exponer el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "/app/app.jar"]