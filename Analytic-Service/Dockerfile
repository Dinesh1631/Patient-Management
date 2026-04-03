#Getting maven for code building
FROM maven:3.9.9-eclipse-temurin-21 AS builder
#Current working directory
WORKDIR /app
#copying the pom file to current dir
COPY pom.xml .
#installing all the maven depencies before building code
RUN mvn dependency:go-offline -B
#Copying the source code folder
COPY src ./src
#Building and packing the code into jar file.
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk AS runner
#Current working directory
WORKDIR /app
# Copy the JAR from the builder stage into this image
# The JAR is renamed to BillingService.jar inside the container
COPY --from=builder /app/target/*.jar ./app.jar

# Expose port 4000 so it can be accessed from outside the container
EXPOSE 4002

# Set the entrypoint to run the Spring Boot application
# This is the command executed when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]



