FROM gradle:8.7-jdk21 AS build
WORKDIR /app

# Copy the Gradle build files
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Download dependencies first (better caching)
RUN gradle dependencies --no-daemon

# Copy the source code
COPY src ./src

# Build the application
RUN gradle build --no-daemon -x test

# Create the runtime image
FROM amazoncorretto:21-alpine
WORKDIR /app

# Add a non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Set proper ownership
RUN chown spring:spring /app

# Switch to non-root user
USER spring

# Set the entry point
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the port that the application runs on
EXPOSE 8080 