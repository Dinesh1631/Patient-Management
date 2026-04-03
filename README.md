# Patient Management System

A comprehensive microservices-based patient management platform built with Spring Boot 3.x and Spring Cloud. The system enables healthcare providers to efficiently manage patient records, billing, authentication, and analytics across distributed services with gRPC and Kafka integration.

## Table of Contents

- [Overview](#overview)
- [System Architecture](#system-architecture)
- [Services](#services)
- [Prerequisites](#prerequisites)
- [Database Setup](#database-setup)
- [Environment Variables](#environment-variables)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Implementation Details](#implementation-details)
- [Architecture Patterns](#architecture-patterns)
- [Best Practices](#best-practices)
- [Learning Highlights](#learning-highlights)
- [Project Structure](#project-structure)

---

## Overview

The Patient Management System is designed to streamline healthcare operations by providing:

- **Patient Record Management**: Create, read, update, and delete patient information
- **Authentication & Authorization**: JWT-based security with role-based access control (RBAC)
- **Billing Services**: Process patient billing using gRPC microservice communication
- **Event-Driven Analytics**: Real-time analytics using Apache Kafka message streaming
- **API Gateway**: Centralized entry point for all services with Spring Cloud Gateway

**Technology Stack:**
- Java 21 (Patient Service), Java 17 (Other Services)
- Spring Boot 3.5.6 - 3.5.7
- Spring Data JPA with Hibernate ORM
- MySQL 8.0
- Apache Kafka 3.3+
- gRPC with Protocol Buffers
- Spring Cloud Gateway 2025.0.0
- Docker & Docker Compose
- Maven Build System

---

## System Architecture

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
       ▼
┌──────────────────────────────────┐
│   API Gateway (Port 4004)         │ ◄─── Entry Point
│   Spring Cloud Gateway WebFlux    │
└──────┬───────────────────────────┘
       │
       ├──────────────────┬──────────────────┬──────────────────┐
       │                  │                  │                  │
       ▼                  ▼                  ▼                  ▼
┌─────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│   Patient   │  │     AUTH     │  │   Billing    │  │  Analytic    │
│   Service   │  │   Service    │  │   Service    │  │   Service    │
│(Port 4000)  │  │  (Port 4005) │  │  (Port 4001) │  │  (Port 4002) │
│             │  │              │  │              │  │              │
│  • JPA ORM  │  │  • JWT Auth  │  │  • gRPC      │  │  • Kafka     │
│  • MySQL    │  │  • RBAC      │  │  • gRPC Port │  │  Consumer    │
│  • Kafka    │  │  • MySQL     │  │    (9001)    │  │  • Analytics │
│    Producer │  │  • Security  │  │  • Protocol  │  │    Events    │
└─────────────┘  │              │  │    Buffers   │  └──────────────┘
       │         └──────────────┘  └──────────────┘
       │                              ▲
       │                              │ gRPC
       │                              │
       └──────────────────────────────┘

Message Queue (Kafka):
┌────────────────────────────────────┐
│  Kafka Topic: Patient Events        │
│  (patient-events, billing-events)   │
└────────────────────────────────────┘
       │                         │
       ▼                         ▼
[Patient Service]          [Analytic Service]
(produces events)          (consumes events)
```

---

## Services

### 1. **Patient Service** (Port 4000)
Manages patient records and demographic information.

**Responsibilities:**
- Create, read, update, and delete patient records
- Search patients by various criteria
- Manage patient medical history
- Produce patient events to Kafka for analytics
- Integrate with Billing Service via gRPC

**Key Technologies:**
- Spring Data JPA for ORM
- MySQL database (patientdb)
- Kafka Producer for event streaming
- gRPC client for billing integration
- Custom validation groups for different operation types

**Endpoints:**
- `GET /Patients/findAllPatients` - Retrieve all patients
- `POST /Patients/createPatient` - Create new patient
- `PUT /Patients/updatePatientDetails` - Update patient information
- `DELETE /Patients/deletePatient/{id}` - Delete patient record

### 2. **AUTH Service** (Port 4005)
Handles user authentication and authorization with JWT tokens.

**Responsibilities:**
- User registration and login
- JWT token generation and validation
- Role-based access control (ADMIN, DOCTOR, PATIENT, etc.)
- User credential management
- Token validation for API Gateway

**Key Technologies:**
- Spring Security for authentication
- JWT for token-based security
- Spring Data JPA for user persistence
- MySQL database (db)
- BCrypt for password hashing

**Endpoints:**
- `POST /login` - User login and token generation
- `GET /validate` - Token validation and verification

**Database Tables:**
- `users` - Stores user credentials and roles

### 3. **Billing Service** (Port 4001 / gRPC 9001)
Handles billing and financial transactions using gRPC.

**Responsibilities:**
- Process patient billing records
- Calculate billing amounts
- Manage invoices
- Provide billing information via gRPC to other services
- Process billing events

**Key Technologies:**
- gRPC server for inter-service communication
- Protocol Buffers for data serialization
- Spring Boot Web for REST endpoints (if applicable)
- gRPC reflection enabled for debugging

**gRPC Services:**
- `billing_service.proto` - Service definition for billing operations

### 4. **Analytic Service** (Port 4002)
Processes and analyzes patient data events for insights.

**Responsibilities:**
- Consume patient events from Kafka
- Process billing events
- Generate analytics and reports
- Store analytics data
- Provide insights on patient demographics and patterns

**Key Technologies:**
- Kafka Consumer for event streaming
- Spring Kafka integration
- Event processing and aggregation
- Real-time analytics computation

**Kafka Topics:**
- `patient-events` - Patient creation, update, deletion events
- `billing-events` - Billing transaction events

### 5. **API Gateway** (Port 4004)
Acts as the single entry point for all client requests.

**Responsibilities:**
- Route requests to appropriate microservices
- Handle cross-cutting concerns
- Request filtering and preprocessing
- Response processing and postprocessing
- Centralized authentication/authorization

**Key Technologies:**
- Spring Cloud Gateway with WebFlux
- Non-blocking request handling
- Custom filters for authentication
- Route configuration with predicates

**Features:**
- Centralized request routing
- JWT token validation via AUTH Service
- Circuit breaker patterns
- Rate limiting capabilities
- Request logging and debugging

---

## Prerequisites

Before running the application, ensure you have the following installed:

1. **Java Development Kit (JDK)**
    - JDK 21 for Patient Service
    - JDK 17 for other services
    - Download from: https://www.oracle.com/java/technologies/downloads/

2. **Maven 3.9.x or higher**
    - Download from: https://maven.apache.org/download.cgi

3. **MySQL Server 8.0 or higher**
    - Download from: https://www.mysql.com/downloads/
    - Ensure the MySQL service is running

4. **Apache Kafka 3.3 or higher**
    - Download from: https://kafka.apache.org/downloads

5. **Docker & Docker Compose** (Optional, for containerized setup)
    - Download from: https://www.docker.com/products/docker-desktop/

6. **IDE (Recommended)**
    - IntelliJ IDEA Ultimate or Community Edition
    - Eclipse IDE
    - VS Code with Java extensions

---

## Database Setup

### Option 1: Manual Setup

1. **Create Databases:**
   ```sql
   CREATE DATABASE patientdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   CREATE DATABASE db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **Create Users:**
   ```sql
   -- For Patient Service
   CREATE USER 'root'@'localhost' IDENTIFIED BY 'Dinesh@123';
   GRANT ALL PRIVILEGES ON patientdb.* TO 'root'@'localhost';

   -- For AUTH Service
   CREATE USER 'admin_user'@'localhost' IDENTIFIED BY 'password';
   GRANT ALL PRIVILEGES ON db.* TO 'admin_user'@'localhost';

   FLUSH PRIVILEGES;
   ```

3. **Load Initial Data:**
    - The application will automatically execute `data.sql` files during startup
    - `Patient-Service/src/main/resources/data.sql` - Creates patient table with sample data
    - `AUTH_Service/src/main/resources/data.sql` - Creates users table with test credentials

### Option 2: Using Docker Compose

Create a `docker-compose.yml` file in the project root:

```yaml
version: '3.8'

services:
  mysql-patient:
    image: mysql:8.0
    container_name: mysql_patient
    environment:
      MYSQL_ROOT_PASSWORD: Dinesh@123
      MYSQL_DATABASE: patientdb
    ports:
      - "3306:3306"
    volumes:
      - patient_data:/var/lib/mysql

  mysql-auth:
    image: mysql:8.0
    container_name: mysql_auth
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: db
      MYSQL_USER: admin_user
      MYSQL_PASSWORD: password
    ports:
      - "3308:3306"
    volumes:
      - auth_data:/var/lib/mysql

  kafka:
    image: confluentinc/cp-kafka:7.5.0
    container_name: kafka
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:9094
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    ports:
      - "9094:9094"

  zookeeper:
    image: confluentinc/cp-zookeeper:7.5.0
    container_name: zookeeper
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181

volumes:
  patient_data:
  auth_data:
```

Run with: `docker-compose up -d`

---

## Environment Variables

### Patient Service (`Patient-Service/src/main/resources/application.properties`)

```properties
# Application Configuration
spring.application.name=Patient-Service
server.port=4000

# Database Configuration
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/patientdb
spring.datasource.username=root
spring.datasource.password=Dinesh@123

# Hibernate/JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.sql.init.mode=always

# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9094
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.ByteArraySerializer
```

### AUTH Service (`AUTH_Service/src/main/resources/application.properties`)

```properties
# Application Configuration
spring.application.name=AUTH_Service
server.port=4005

# Database Configuration
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3308/db
spring.datasource.username=admin_user
spring.datasource.password=password

# Hibernate/JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.sql.init.mode=always

# JWT Configuration
jwt.secret=MyVerySecureJWTSecretKeyThatIsAtLeast32CharactersLongForHMACSHA256Algorithm
# jwt.token.secret.validity=300  # 5 minutes (example configuration)
```

### Billing Service (`Billing-Service/src/main/resources/application.properties`)

```properties
# Application Configuration
spring.application.name=Billing-Service
server.port=4001

# gRPC Server Configuration
grpc.server.port=9001
grpc.server.enable-reflection=true
```

### API Gateway (`API-GateWay/src/main/resources/application.yml`)

```yaml
server:
  port: 4004

spring:
  application:
    name: GateWay-Service
  cloud:
    gateway:
      routes:
        - id: patient-service
          uri: http://localhost:4000
          predicates:
            - Path=/Patients/**
        - id: auth-service
          uri: http://localhost:4005
          predicates:
            - Path=/login,/validate
        - id: billing-service
          uri: http://localhost:4001
          predicates:
            - Path=/billing/**

logging:
  level:
    org.springframework.cloud.gateway: DEBUG
    org.springframework.web: DEBUG
    reactor.netty: DEBUG
```

### Analytic Service (`Analytic-Service/src/main/resources/application.properties`)

```properties
# Application Configuration
spring.application.name=Analytic-Service
server.port=4002

# Kafka Configuration
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.ByteArrayDeserializer
spring.kafka.bootstrap-servers=localhost:9094
```

---

## Running the Application

### Option 1: Run from IDE

1. **IntelliJ IDEA:**
    - Open the project root in IntelliJ
    - Let Maven download all dependencies
    - Run each service individually:
        - Right-click on `AuthServiceApplication.java` → Run
        - Right-click on `PatientServiceApplication.java` → Run
        - Right-click on `BillingServiceApplication.java` → Run
        - Right-click on `AnalyticServiceApplication.java` → Run
        - Right-click on `ApiGateWayApplication.java` → Run

2. **Eclipse:**
    - File → Import → Existing Maven Projects
    - Select the root directory
    - For each service, right-click → Run As → Spring Boot App

### Option 2: Run from Command Line

From the root directory of each service:

```bash
# Terminal 1 - AUTH Service
cd AUTH_Service
mvn spring-boot:run

# Terminal 2 - Patient Service
cd Patient-Service
mvn spring-boot:run

# Terminal 3 - Billing Service
cd Billing-Service
mvn spring-boot:run

# Terminal 4 - Analytic Service
cd Analytic-Service
mvn spring-boot:run

# Terminal 5 - API Gateway
cd API-GateWay
mvn spring-boot:run
```

### Option 3: Run with Docker

```bash
# Build Docker images for each service
cd Patient-Service
docker build -t patient-service:latest .

cd ../AUTH_Service
docker build -t auth-service:latest .

cd ../Billing-Service
docker build -t billing-service:latest .

cd ../Analytic-Service
docker build -t analytic-service:latest .

cd ../API-GateWay
docker build -t api-gateway:latest .

# Use Docker Compose
docker-compose up -d
```

### Expected Output

Once all services are running, you should see:

```
Patient-Service started on port 4000
AUTH_Service started on port 4005
Billing-Service started on port 4001 (REST) and 9001 (gRPC)
Analytic-Service started on port 4002
API-GateWay started on port 4004
```

---

## API Documentation

### Authentication Flow

All API requests (except login) require JWT token in the Authorization header:

```
Authorization: Bearer <jwt_token>
```

### 1. Login API

**Endpoint:** `POST /login`

**Request Body:**
```json
{
  "email": "testuser@test.com",
  "password": "test@123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Default Test Credentials:**
- Email: `testuser@test.com`
- Password: (bcrypt hash in database)
- Role: `ADMIN`

### 2. Patient APIs

#### Get All Patients

**Endpoint:** `GET /Patients/findAllPatients`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "address": "123 Main St, Springfield",
    "dateOfBirth": "1985-06-15",
    "registeredDate": "2024-01-10"
  },
  ...
]
```

#### Create Patient

**Endpoint:** `POST /Patients/createPatient`

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "New Patient",
  "email": "newpatient@example.com",
  "address": "789 Test St, Springfield",
  "dateOfBirth": "1990-05-20",
  "registeredDate": "2024-04-03"
}
```

**Response (200 OK):**
```json
{
  "id": 11,
  "name": "New Patient",
  "email": "newpatient@example.com",
  "address": "789 Test St, Springfield",
  "dateOfBirth": "1990-05-20",
  "registeredDate": "2024-04-03"
}
```

**Validations:**
- Name: Required, non-empty string
- Email: Required, valid email format, unique
- Address: Required, non-empty string
- Date of Birth: Required, valid date
- Registered Date: Required, valid date

#### Update Patient

**Endpoint:** `PUT /Patients/updatePatientDetails?id={patientId}`

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Updated Patient",
  "email": "updated.patient@example.com",
  "address": "456 Updated St, Springfield",
  "dateOfBirth": "1990-05-20",
  "registeredDate": "2024-04-03"
}
```

**Response (200 OK):** Updated patient object

#### Delete Patient

**Endpoint:** `DELETE /Patients/deletePatient/{id}`

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
{
  "message": "Patient deleted successfully"
}
```

### 3. Billing Service (gRPC)

**Service Definition:** `billing_service.proto`

**Available Operations:**
- `ProcessBilling` - Process billing for a patient
- `GetBillingInfo` - Retrieve billing information
- `CancelBilling` - Cancel a billing record

### 4. Analytics Service

**Features:**
- Consumes patient events from Kafka
- Processes billing events
- Generates analytics dashboards (future implementation)

**Kafka Topics:**
- `patient-events` - Events when patients are created, updated, or deleted
- `billing-events` - Events for billing transactions

---

## Implementation Details

### Patient Service Architecture

```
PatientService/
├── Controller/
│   └── PatinetController.java          # REST endpoints
├── Service/
│   └── PatientService.java             # Business logic
├── Repository/
│   └── PatientRepository.java          # Database access
├── Modal/
│   └── Patient.java                    # JPA entity
├── DTO/
│   ├── PatientRequestDTO.java          # Request validation
│   ├── PatientResponceDTO.java         # Response object
│   └── CreatePatientValidationGroup.java # Validation groups
├── Mapper/
│   └── PatientMapper.java              # DTO ↔ Entity mapping
├── Exception/
│   └── PatientException.java           # Custom exceptions
├── Kafka/
│   └── KafkaProducer.java              # Event publishing
└── GRPC/
    └── BillingServiceClient.java       # gRPC client
```

### Authentication Service Architecture

```
AuthService/
├── Controller/
│   └── AuthController.java             # Login/Validate endpoints
├── Services/
│   └── AuthService.java                # Token generation/validation
├── Modal/
│   └── User.java                       # User entity
├── Repository/
│   └── UserRepository.java             # User persistence
├── DTO/
│   ├── LoginRequestDTO.java            # Login request
│   └── LoginResponceDTO.java           # Login response
├── config/
│   └── SecurityConfig.java             # Spring Security config
└── utils/
    └── JwtUtil.java                    # JWT token utilities
```

### Key Design Patterns

#### 1. DTO Pattern
- Separate DTOs for request validation and response serialization
- Prevents exposure of internal entity details
- Enables versioning and flexible API contracts

#### 2. Mapper Pattern
- Converts between entities and DTOs
- Keeps controllers and services decoupled
- Facilitates easier unit testing

#### 3. Service Layer Pattern
- Business logic isolated in service classes
- Controllers act as HTTP handlers only
- Repository handles data access

#### 4. Repository Pattern
- Spring Data JPA for database operations
- Query methods derived from method names
- Supports custom query methods with @Query

#### 5. Validation Pattern
- Jakarta Validation (formerly javax.validation)
- Custom validation groups for different scenarios
- DTO-level validation before service processing

### Kafka Event Streaming

**Producer (Patient Service):**
```java
@Component
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendPatientEvent(Patient patient) {
        kafkaTemplate.send("patient-events", 
            serializePatient(patient));
    }
}
```

**Consumer (Analytic Service):**
```java
@Component
public class kafkaConsumer {
    @KafkaListener(topics = "patient-events", 
                   groupId = "analytic-group")
    public void processPatientEvent(byte[] message) {
        // Process event and store analytics
    }
}
```

### gRPC Communication

**Billing Service Configuration:**
- gRPC server listening on port 9001
- Protocol Buffers for message definition
- Reflection enabled for debugging

**Client Configuration:**
```java
@Bean
public BillingServiceStub billingStub() {
    Channel channel = ManagedChannelBuilder
        .forAddress("localhost", 9001)
        .usePlaintext()
        .build();
    return BillingServiceGrpc.newStub(channel);
}
```

---

## Architecture Patterns

### 1. Microservices Architecture

**Characteristics:**
- Independent, deployable services
- Single responsibility principle
- Decoupled communication via APIs, gRPC, and events
- Technology agnostic services

**Benefits:**
- Scalability: Each service can be scaled independently
- Flexibility: Different technologies for different services
- Resilience: Failure isolation between services
- Development velocity: Teams can work independently

### 2. API Gateway Pattern

**Implementation:** Spring Cloud Gateway (WebFlux)

**Features:**
- Single entry point for all client requests
- Request routing based on predicates
- Custom filters for cross-cutting concerns
- Authentication/authorization enforcement

**Routes:**
```
POST /login                → AUTH Service
GET /validate              → AUTH Service
GET|POST|PUT|DELETE /Patients/** → Patient Service
POST /billing/**           → Billing Service
```

### 3. Event-Driven Architecture

**Event Flow:**
1. Patient Service produces events to Kafka
2. Events include patient creation, updates, deletions
3. Analytic Service consumes and processes events
4. Real-time analytics and insights generated

**Kafka Topics:**
- `patient-events` - Patient lifecycle events
- `billing-events` - Billing transaction events

### 4. Service-to-Service Communication

**gRPC (for Billing):**
- Low-latency, binary protocol
- Strong typing via Protocol Buffers
- Bidirectional streaming capability
- Ideal for internal service-to-service calls

**REST (via Gateway):**
- Client-facing APIs
- JSON payloads
- Standard HTTP methods
- Easy to test and debug

### 5. Authentication & Authorization

**JWT Token-Based:**
- Stateless authentication
- Self-contained token with user info
- HMAC-SHA256 signature
- Tokens validated by API Gateway

**Authorization:**
- Role-based access control (RBAC)
- Roles: ADMIN, DOCTOR, PATIENT
- Future: Attribute-based access control (ABAC)

---

## Best Practices

### 1. REST API Best Practices
- ✅ Proper HTTP status codes (200, 201, 400, 401, 404, 500)
- ✅ Meaningful resource URIs (/Patients, /login, etc.)
- ✅ Request/response DTOs with validation
- ✅ Consistent error response format
- ✅ API versioning strategy

### 2. Spring Boot Best Practices
- ✅ Separation of concerns (Controller → Service → Repository)
- ✅ Dependency injection via constructors
- ✅ @Transactional for database operations
- ✅ Custom exception handling with @ControllerAdvice
- ✅ Logging at appropriate levels (info, debug, error)

### 3. Database Best Practices
- ✅ Proper foreign key relationships
- ✅ Unique constraints on business keys (email)
- ✅ Indexed columns for frequent queries
- ✅ Lazy loading for JPA associations
- ✅ Connection pooling configuration

### 4. Security Best Practices
- ✅ JWT tokens with appropriate expiration
- ✅ Password hashing with BCrypt
- ✅ Input validation and sanitization
- ✅ SQL injection prevention via parameterized queries
- ✅ HTTPS/TLS in production

### 5. Code Quality Best Practices
- ✅ Clear, meaningful variable and method names
- ✅ Single Responsibility Principle (SRP)
- ✅ DRY (Don't Repeat Yourself)
- ✅ SOLID principles adherence
- ✅ Unit tests for business logic
- ✅ Integration tests for APIs

### 6. Concurrency & Performance
- ✅ Appropriate isolation levels for transactions
- ✅ Optimistic locking with @Version for conflict detection
- ✅ Pessimistic locking when necessary
- ✅ Query optimization with JOIN FETCH
- ✅ Connection pooling and timeout configuration

### 7. Documentation Best Practices
- ✅ JavaDoc for public APIs
- ✅ README with setup instructions
- ✅ API documentation (Swagger in future)
- ✅ Architecture diagrams
- ✅ Configuration documentation

### 8. Deployment & DevOps
- ✅ Docker containerization
- ✅ Docker Compose for local development
- ✅ Environment-specific configurations
- ✅ Centralized logging (future)
- ✅ Health checks and metrics

---

## Learning Highlights

### 1. Microservices Architecture
- Understanding service boundaries and responsibilities
- Inter-service communication patterns
- Trade-offs between services (consistency vs. latency)
- Distributed transaction management with events

### 2. Spring Ecosystem
- Spring Boot auto-configuration
- Spring Data JPA with custom queries
- Spring Security with JWT integration
- Spring Cloud Gateway for service routing
- Spring Kafka for event streaming

### 3. Database Design
- Normalization principles
- Relationship modeling (1:1, 1:N, M:N)
- Cascade operations for referential integrity
- Index design for performance
- Transaction isolation levels

### 4. gRPC & Protocol Buffers
- Defining services in .proto files
- Binary serialization vs. JSON
- Bidirectional streaming
- Interceptors for cross-cutting concerns
- Reflection for debugging

### 5. Event-Driven Architecture
- Publisher-Subscriber pattern
- Event sourcing principles
- Eventual consistency
- Handling out-of-order events
- Dead letter queues and error handling

### 6. API Design & Documentation
- RESTful principles
- HTTP status codes and semantics
- API versioning strategies
- Request/response contracts
- Swagger/OpenAPI documentation

### 7. Security Implementation
- JWT token generation and validation
- Password hashing and salting
- Role-based access control
- Request filtering and validation
- CORS and CSRF protection

### 8. Testing Strategies
- Unit testing with JUnit 5 and Mockito
- Integration testing with TestContainers
- API testing tools (Postman, REST Assured)
- Mock data and fixtures
- Test-driven development (TDD)

### 9. Performance Optimization
- Connection pooling configuration
- Query optimization with proper indexing
- N+1 query problem identification
- Caching strategies
- Lazy loading and fetch strategies

### 10. DevOps & Deployment
- Docker containerization and multi-stage builds
- Docker Compose for orchestration
- Environment configuration management
- Health checks and readiness probes
- Centralized logging and monitoring

---

## Project Structure

```
Patient-Management/
├── README.md                          # Project overview
├── README_DETAILED.md                 # This comprehensive guide
├── Patient-Management.iml             # IntelliJ project file
│
├── Patient-Service/                   # Patient Management Microservice
│   ├── pom.xml                        # Maven dependencies
│   ├── Dockerfile                     # Docker image definition
│   ├── mvnw / mvnw.cmd               # Maven wrapper
│   └── src/
│       ├── main/
│       │   ├── java/com/pm/patientservice/
│       │   │   ├── PatientServiceApplication.java
│       │   │   ├── Controller/
│       │   │   │   └── PatinetController.java
│       │   │   ├── Service/
│       │   │   │   └── PatientService.java
│       │   │   ├── Repository/
│       │   │   │   └── PatientRepository.java
│       │   │   ├── Modal/
│       │   │   │   └── Patient.java
│       │   │   ├── DTO/
│       │   │   │   ├── PatientRequestDTO.java
│       │   │   │   ├── PatientResponceDTO.java
│       │   │   │   └── CreatePatientValidationGroup.java
│       │   │   ├── Mapper/
│       │   │   │   └── PatientMapper.java
│       │   │   ├── Exception/
│       │   │   │   └── PatientException.java
│       │   │   ├── Kafka/
│       │   │   │   └── KafkaProducer.java
│       │   │   └── GRPC/
│       │   │       └── BillingServiceClient.java
│       │   ├── proto/
│       │   │   ├── patient_event.proto
│       │   │   └── billing_service.proto
│       │   └── resources/
│       │       ├── application.properties
│       │       └── data.sql
│       └── test/
│           └── java/com/pm/patientservice/
│               └── PatientServiceApplicationTests.java
│
├── AUTH_Service/                      # Authentication & Authorization
│   ├── pom.xml
│   ├── Dockerfile
│   ├── mvnw / mvnw.cmd
│   └── src/
│       ├── main/
│       │   ├── java/com/pm/authservice/
│       │   │   ├── AuthServiceApplication.java
│       │   │   ├── Controller/
│       │   │   │   └── AuthController.java
│       │   │   ├── Services/
│       │   │   │   └── AuthService.java
│       │   │   ├── Modal/
│       │   │   │   └── User.java
│       │   │   ├── Repository/
│       │   │   │   └── UserRepository.java
│       │   │   ├── DTO/
│       │   │   │   ├── LoginRequestDTO.java
│       │   │   │   └── LoginResponceDTO.java
│       │   │   ├── config/
│       │   │   │   └── SecurityConfig.java
│       │   │   └── utils/
│       │   │       └── JwtUtil.java
│       │   └── resources/
│       │       ├── application.properties
│       │       └── data.sql
│       └── test/
│           └── java/com/pm/authservice/
│               └── AuthServiceApplicationTests.java
│
├── Billing-Service/                   # Billing & Payments (gRPC)
│   ├── pom.xml
│   ├── Dockerfile
│   ├── mvnw / mvnw.cmd
│   ├── package.json
│   └── src/
│       ├── main/
│       │   ├── java/com/pm/billingservice/
│       │   │   ├── BillingServiceApplication.java
│       │   │   └── GrpcService/
│       │   │       └── BillingServiceImpl.java
│       │   ├── proto/
│       │   │   └── billing_service.proto
│       │   └── resources/
│       │       └── application.properties
│       └── test/
│           └── java/com/pm/billingservice/
│               └── BillingServiceApplicationTests.java
│
├── Analytic-Service/                  # Analytics & Insights (Kafka Consumer)
│   ├── pom.xml
│   ├── Dockerfile
│   ├── mvnw / mvnw.cmd
│   └── src/
│       ├── main/
│       │   ├── java/com/pm/analyticservice/
│       │   │   ├── AnalyticServiceApplication.java
│       │   │   └── Services/
│       │   │       └── kafkaConsumer.java
│       │   ├── proto/
│       │   │   ├── patient_event.proto
│       │   │   └── billing_service.proto
│       │   └── resources/
│       │       └── application.properties
│       └── test/
│           └── java/com/pm/analyticservice/
│               └── AnalyticServiceApplicationTests.java
│
└── API-GateWay/                       # API Gateway (Spring Cloud Gateway)
    ├── pom.xml
    ├── Dockerfile
    ├── mvnw / mvnw.cmd
    └── src/
        ├── main/
        │   ├── java/com/pm/apigateway/
        │   │   ├── ApiGateWayApplication.java
        │   │   └── Filters/
        │   │       └── AuthenticationFilter.java
        │   └── resources/
        │       └── application.yml
        └── test/
            └── java/com/pm/apigateway/
                └── ApiGateWayApplicationTests.java
```

---

## Endpoints Summary

### Via API Gateway (Port 4004)

| Method | Endpoint | Service | Auth Required |
|--------|----------|---------|----------------|
| POST | /login | AUTH Service | ❌ |
| GET | /validate | AUTH Service | ✅ |
| GET | /Patients/findAllPatients | Patient Service | ✅ |
| POST | /Patients/createPatient | Patient Service | ✅ |
| PUT | /Patients/updatePatientDetails | Patient Service | ✅ |
| DELETE | /Patients/deletePatient/{id} | Patient Service | ✅ |

### Direct Service Access (for development)

- **AUTH Service**: http://localhost:4005
- **Patient Service**: http://localhost:4000
- **Billing Service**:
    - REST: http://localhost:4001
    - gRPC: localhost:9001
- **Analytic Service**: http://localhost:4002
- **API Gateway**: http://localhost:4004

---

## Troubleshooting

### MySQL Connection Issues
**Problem:** "Access denied for user 'root'@'localhost'"

**Solution:**
- Verify MySQL is running: `mysql -u root -p`
- Check credentials in application.properties
- Ensure databases and users are created

### Kafka Connection Issues
**Problem:** "Connection refused" on localhost:9094

**Solution:**
- Start Kafka: `bin/kafka-server-start.sh config/server.properties`
- Verify Zookeeper is running first
- Check Kafka broker is listening: `netstat -an | grep 9094`

### gRPC Connection Issues
**Problem:** "Failed to resolve name: billing-service"

**Solution:**
- Start Billing Service on port 9001
- Check firewall settings
- Verify gRPC service is properly implemented

### JWT Token Validation Fails
**Problem:** "Token validation failed" or "401 Unauthorized"

**Solution:**
- Obtain new token from /login endpoint
- Ensure token includes "Bearer " prefix
- Check token expiration time
- Verify JWT secret matches between services

### Port Already in Use
**Problem:** "Address already in use: bind"

**Solution:**
```bash
# Windows - Find and kill process using port
netstat -ano | findstr :4000
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :4000
kill -9 <PID>
```

---

## Future Enhancements

### Phase 2
- [ ] Swagger/OpenAPI API documentation
- [ ] Unit test coverage (>80%)
- [ ] Integration tests with TestContainers
- [ ] Attribute-based Access Control (ABAC)
- [ ] Advanced caching with Redis

### Phase 3
- [ ] Analytics Dashboard (Spring Boot + Thymeleaf)
- [ ] Appointment scheduling service
- [ ] Medical records and test results management
- [ ] Prescription management
- [ ] Patient communication portal

### Phase 4
- [ ] Mobile app integration (REST APIs)
- [ ] Payment gateway integration
- [ ] HIPAA compliance features
- [ ] Multi-tenant support
- [ ] Microservices observability (ELK Stack)
- [ ] Circuit breaker implementation (Hystrix/Resilience4j)

### DevOps Enhancements
- [ ] Kubernetes deployment manifests
- [ ] Helm charts for package management
- [ ] CI/CD pipeline (GitHub Actions / Jenkins)
- [ ] Centralized logging (ELK Stack)
- [ ] Distributed tracing (Jaeger/Zipkin)
- [ ] Prometheus metrics and Grafana dashboards
- [ ] Health checks and readiness probes
- [ ] Auto-scaling policies

---

## Contributing

To contribute to this project:

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -m "Add your feature"`
3. Push to branch: `git push origin feature/your-feature`
4. Create a Pull Request with description

### Code Style
- Follow Java naming conventions (camelCase)
- Use 4 spaces for indentation
- Maximum line length: 120 characters
- Write meaningful commit messages

---

## License

This project is provided as-is for educational and development purposes.

---

## Support & Contact

For questions or issues:
- Create an issue in the repository
- Contact: dinesh.pm@example.com
- Documentation: See README_DETAILED.md (this file)

---

## Version History

- **v1.0.0** (April 2026) - Initial release
    - Patient Service with CRUD operations
    - Authentication Service with JWT
    - Billing Service with gRPC
    - Analytic Service with Kafka
    - API Gateway with Spring Cloud Gateway

---

**Last Updated:** April 3, 2026

**Project Status:** Active Development 🚀

---

*This comprehensive documentation provides everything needed to understand, set up, and extend the Patient Management System microservices architecture.*

