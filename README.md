# EntityUSSD - USSD Banking Platform

**EntityUSSD** is a database-driven USSD platform built with Spring Boot.  
It supports:

- Menu-driven USSD flows loaded from the database
- Redis-based session management with automatic TTL (10 minutes)
- PIN login
- Mock banking operations: balance check, money transfer
- Exit flow with session cleanup
- Integration-ready for telco aggregators (Safaricom,MTN,Africa’s Talking, Infobip, Huawei, etc.)

---

## **Table of Contents**

1. [Prerequisites](#prerequisites)
2. [Project Structure](#project-structure)
3. [Configuration](#configuration)
4. [Docker Setup](#docker-setup)
5. [Running the Application](#running-the-application)
6. [USSD Flows](#ussd-flows)
7. [Postman Requests](#postman-requests)

---

## **Prerequisites**

- Java 8 or higher
- Maven 3.x
- Docker & Docker Compose
- Download, install, and start a local Redis instance

---

## **Project Structure**



ussd-entity-bank/
├── src/
│ ├── main/
│ │ ├── java/com/entitybank/digital/ussd
│ │ │ ├── controller
│ │ │ ├── entity
│ │ │ ├── model
│ │ │ ├── repository
│ │ │ └── service
│ │ └── resources
│ │ ├── application.yml
│ │ ├── schema.sql
│ │ └── data.sql
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md

Visit http://localhost:9000/swagger-ui.html to view the UI.
---

## **Configuration**

### **application.yml** (Docker-ready)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/ussd_db
    username: ussd_user
    password: ussd_pass
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

  redis:
    host: redis
    port: 6379

ussd:
  session:
    ttl-seconds: 300  # 5 minutes


Note: postgres and redis refer to service names in docker-compose.yml.

Docker Setup
docker-compose.yml
version: '3.8'

services:

  postgres:
    image: postgres:15
    container_name: ussd-postgres
    environment:
      POSTGRES_USER: ussd_user
      POSTGRES_PASSWORD: ussd_pass
      POSTGRES_DB: ussd_db
    ports:
      - "5432:5432"
    volumes:
      - ussd_postgres_data:/var/lib/postgresql/data
    networks:
      - ussd-network

  redis:
    image: redis:7
    container_name: ussd-redis
    ports:
      - "6379:6379"
    networks:
      - ussd-network
    command: ["redis-server", "--appendonly", "yes"]

volumes:
  ussd_postgres_data:

networks:
  ussd-network:
    driver: bridge

Dockerfile
# Use OpenJDK 17 base image
FROM openjdk:17-jdk-slim

VOLUME /tmp

# Copy built jar
ARG JAR_FILE=target/ussd-entity-bank-1.0.0.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]

Running the Application

Build the Spring Boot JAR:

mvn clean package


Start Docker services:

docker-compose up -d


Build and run the USSD app container:

docker build -t entity-ussd .
docker run --network ussd-network -p 8080:8080 entity-ussd


Test the USSD endpoint:

POST http://localhost:8080/ussd
Content-Type: application/x-www-form-urlencoded

sessionId=ABC123
phoneNumber=254712345678
text=

USSD Flows

Welcome / PIN Login

User dials USSD

Prompt: Enter PIN (Forgot PIN reply 1)

Main Menu

1. Balance
2. Send Money
3. Change PIN
0. Exit


Balance → BalanceAction → Returns mocked balance

Send Money → TransferAction → Enter recipient, amount → Success message

Change PIN → PinChangeAction → Enter old PIN → Enter new PIN → Success

Exit → ExitAction → Session cleanup

Postman Requests (Examples)
1. Dial USSD
POST http://localhost:8080/ussd
Content-Type: application/x-www-form-urlencoded

sessionId=ABC123
phoneNumber=254712345678
text=

2. Enter PIN
POST http://localhost:8080/ussd
Content-Type: application/x-www-form-urlencoded

sessionId=ABC123
phoneNumber=254712345678
text=1234

3. Select Balance
POST http://localhost:8080/ussd
Content-Type: application/x-www-form-urlencoded

sessionId=ABC123
phoneNumber=254712345678
text=1

4. Exit
POST http://localhost:8080/ussd
Content-Type: application/x-www-form-urlencoded

sessionId=ABC123
phoneNumber=254712345678
text=0

Notes

The USSD menus are fully DB-driven; you can add/change menus in USSD_MENU table.

All actions (balance check, transfer, PIN operations) are Java classes referenced in actionBean column.

Redis handles session state with TTL of 5 minutes.

Replace mock banking logic with real core banking APIs as needed.