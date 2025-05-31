FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace/app

# Copy pom.xml file and download dependencies
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Give execution permissions to mvnw and build the JAR
RUN chmod +x ./mvnw
RUN ./mvnw install -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
# VOLUME /tmp

# Create a non-root user to run the application
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the JAR from the builder stage
COPY --from=builder /workspace/app/target/*.jar app.jar

# Expose the port the app will run on
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]