# ==========================================
# ETAPA 1: Compilar el proyecto
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos primero el pom.xml para aprovechar caché de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente y compilamos
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# ETAPA 2: Imagen final liviana
# ==========================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Carpeta para los uploads de usuarios
RUN mkdir -p /app/uploads

# Copiamos solo el JAR compilado de la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Puerto que expone la app
EXPOSE 8080

# Comando de arranque con perfil de producción
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]