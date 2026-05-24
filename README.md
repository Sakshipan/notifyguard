# NotifyGuard — Notification Orchestration Platform

## What is this
A backend platform that manages multi-channel notification campaigns with AI-powered routing, real-time behavioral tracking, and immutable audit trail for compliance.

## Architecture
Modular monolith with two internal domains:
- Notify domain — campaign management, notification delivery
- Audit domain — compliance logging, anomaly detection, dashboard

## Tech Stack
- Java 21 + Spring Boot 3.2
- PostgreSQL (Neon) — shared cloud database
- RabbitMQ (Docker) — async message delivery
- Redis (Docker) — user behavior tracking
- Spring AI + GPT-4o-mini — intelligent channel routing
- Spring Security + JWT — authentication and authorization
- Swagger/OpenAPI — API documentation
- Maven — build tool

## Key Features
- Multi-channel delivery: Email, SMS, Push, WhatsApp
- Discovery phase → sends all channels to find best one per user
- Optimized phase → sends only on best channel per user
- AI decides channel based on real behavioral data from Redis
- AI assigns priority from message content automatically
- AI personalizes message per channel (SMS shorter, email warmer)
- Immutable audit logs with before/after state diffing
- Anomaly detection with configurable rules
- Compliance dashboard with campaign analytics
- Role based access: ADMIN, USER, AUDITOR

## How to run locally
1. Clone the repo
   git clone https://github.com/Sakshipan/notifyguard.git

2. Start infrastructure
   docker run -d --name notifyguard-rabbitmq \
     -p 5672:5672 -p 15672:15672 \
     -e RABBITMQ_DEFAULT_USER=admin \
     -e RABBITMQ_DEFAULT_PASS=admin123 \
     rabbitmq:3.12-management

   docker run -d --name notifyguard-redis \
     -p 6379:6379 redis:7.2

3. Configure application.properties
   Copy application.properties.example
   Fill in your Neon DB credentials
   Fill in your OpenAI API key

4. Run the application
   mvn spring-boot:run

5. Open Swagger UI
   http://localhost:8080/swagger-ui.html

## API Documentation
Full API available at http://localhost:8080/swagger-ui.html after running

### Key endpoints
POST /user/register         — create account
POST /user/login            — get JWT token
POST /api/campaigns         — create campaign (ADMIN)
POST /api/campaigns/{id}/start — start campaign
POST /api/campaigns/{id}/users/enroll — enroll users
POST /api/notifications/send — send notification
POST /api/notifications/{id}/respond — track user response
GET  /api/dashboard/campaign/{id}/summary — campaign analytics
GET  /api/audit/logs        — compliance logs

## Database Schema
8 tables:
users, campaigns, campaign_users, notifications,
delivery_attempts, audit_logs, anomaly_rules, anomaly_alerts

## Project Structure
src/main/java/com/notifyguard/
├── Notify/          — Notification domain (Ayush)
│   ├── entity/
│   ├── repository/
│   ├── service/
│   ├── controller/
│   ├── worker/      — RabbitMQ workers
│   ├── redis/       — behavior tracking
│   └── ai/          — routing + priority
└── Audit/           — Audit domain (Sakshi)
    ├── Entity/
    ├── Repository/
    ├── Service/
    ├── controller/
    └── ai/          — template + anomaly explainer
