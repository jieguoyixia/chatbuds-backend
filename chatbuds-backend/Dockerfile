# Use a lightweight OpenJDK image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper first
COPY mvnw .
COPY .mvn/ .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Pre-download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the project
RUN ./mvnw clean package -DskipTests

# Expose default Spring Boot port
EXPOSE 8080

# Run the JAR
CMD ["java", "-jar", "target/chatbuds-backend-0.0.1-SNAPSHOT.jar"]