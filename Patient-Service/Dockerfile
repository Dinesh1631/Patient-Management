# ------------------------------
# Stage 1: Build Stage
# ------------------------------

# Use Maven + JDK 21 image to build the Spring Boot app
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy only the pom.xml first
# This is for caching dependencies. Docker will only re-download dependencies if pom.xml changes
COPY pom.xml .

# Download all dependencies in advance to speed up builds later
RUN mvn dependency:go-offline -B

# Copy the source code of the project into the container
COPY src ./src

# Build the project (clean previous builds and package the app as a JAR)
RUN mvn clean package -DskipTests
# After this step, the JAR will be located at: /app/target/Patient-Service-0.0.1-SNAPSHOT.jar

# ------------------------------
# Stage 2: Run Stage (Slim Image)
# ------------------------------

# Use a plain JDK 21 image to run the application
# This keeps the final image smaller, without all Maven files
FROM openjdk:21-jdk AS runner

# Set the working directory for runtime
WORKDIR /app

# Copy the JAR from the builder stage into this image
# The JAR is renamed to Patient-Service.jar inside the container
COPY --from=builder /app/target/Patient-Service-0.0.1-SNAPSHOT.jar ./Patient-Service.jar

# Expose port 4000 so it can be accessed from outside the container
EXPOSE 4000

# Set the entrypoint to run the Spring Boot application
# This is the command executed when the container starts
ENTRYPOINT ["java", "-jar", "Patient-Service.jar"]
