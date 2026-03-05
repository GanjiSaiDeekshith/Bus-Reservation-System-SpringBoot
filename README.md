# 🚌 BusReservationApplication

A full-stack bus booking web application built with **Java 17**, **Spring Boot 3.4.1**, **Spring MVC**, **JPA/Hibernate**, **MySQL**, **JSP**, and **Bootstrap**. Supports role-based access for Admins and Users with full reservation, route, bus, and feedback management.

---

## 📑 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Database Schema](#database-schema)
- [Tables](#tables)
- [Sample Data](#sample-data)
- [Setup Instructions](#setup-instructions)
- [Environment Variables](#environment-variables)
- [Web Routes / Endpoints](#web-routes--endpoints)
- [Running Locally](#running-locally)
- [Testing](#testing)
- [Security Notes](#security-notes)
- [Migrations & Backups](#migrations--backups)
- [FAQ](#faq)

---

## Overview

BusReservationApplication allows:

- **Admins** to manage routes, buses, reservations, users, and view feedback.
- **Users** to register, search buses, book/cancel tickets, make payments, and submit feedback.

The application follows the **MVC layered architecture** (Controller → Service → Repository → Entity) and uses Hibernate's `ddl-auto=update` to auto-manage schema changes.

---

## Tech Stack

| Layer         | Technology                          |
|---------------|-------------------------------------|
| Language      | Java 17                             |
| Framework     | Spring Boot 3.4.1, Spring MVC       |
| ORM           | Spring Data JPA / Hibernate         |
| Database      | MySQL 8+                            |
| View Layer    | JSP + JSTL                          |
| Frontend      | Bootstrap 5                         |
| Build Tool    | Maven 3.9+                          |
| Dev Utilities | Lombok, Spring DevTools             |
| Server Port   | `7383` (configurable)               |

---

## Database Schema

The application uses a database named **`busdb`** (auto-created on startup via `createDatabaseIfNotExist=true`).

### Entity Relationship Summary

```
route (1) ──────< bus (N)
bus   (1) ──────< reservation (N)
user  (1) ──────< reservation (N)
user  (1) ──────< feedback (N)
bus   (1) ──────< feedback (N)
reservation (1) < feedback (1)
admin            -- standalone auth entity
```

---

## Tables

### `admin`
| Column           | Type         | Constraints       |
|------------------|--------------|-------------------|
| admin_id         | BIGINT       | PK, AUTO_INCREMENT|
| admin_username   | VARCHAR(255) | NOT NULL          |
| admin_password   | VARCHAR(255) | NOT NULL          |

### `user`
| Column      | Type         | Constraints        |
|-------------|--------------|--------------------|
| user_id     | BIGINT       | PK, AUTO_INCREMENT |
| username    | VARCHAR(255) | NOT NULL           |
| password    | VARCHAR(255) | NOT NULL           |
| first_name  | VARCHAR(255) |                    |
| last_name   | VARCHAR(255) |                    |
| contact     | VARCHAR(255) |                    |
| email       | VARCHAR(255) |                    |

### `route`
| Column      | Type         | Constraints        |
|-------------|--------------|--------------------|
| route_id    | BIGINT       | PK, AUTO_INCREMENT |
| route_from  | VARCHAR(255) | NOT NULL           |
| route_to    | VARCHAR(255) | NOT NULL           |
| distance    | INT          | NOT NULL           |

### `bus`
| Column          | Type         | Constraints        |
|-----------------|--------------|--------------------|
| bus_id          | BIGINT       | PK, AUTO_INCREMENT |
| bus_name        | VARCHAR(255) |                    |
| driver_name     | VARCHAR(255) |                    |
| bus_type        | VARCHAR(255) |                    |
| arrival_time    | TIME         |                    |
| departure_time  | TIME         |                    |
| seats           | INT          |                    |
| available_seats | INT          |                    |
| price           | DOUBLE       |                    |
| route_id        | BIGINT       | FK → route(route_id)|

### `reservation`
| Column               | Type         | Constraints         |
|----------------------|--------------|---------------------|
| reservation_id       | BIGINT       | PK, AUTO_INCREMENT  |
| reservation_status   | VARCHAR(255) |                     |
| reservation_type     | VARCHAR(255) |                     |
| reservation_date     | DATE         |                     |
| reservation_time     | VARCHAR(255) |                     |
| source               | VARCHAR(255) |                     |
| destination          | VARCHAR(255) |                     |
| journey_date         | DATETIME     |                     |
| journey_started      | BOOLEAN      |                     |
| journey_ended        | BOOLEAN      |                     |
| seats_requested      | INT          |                     |
| bus_id               | BIGINT       | FK → bus(bus_id)    |
| user_id              | BIGINT       | FK → user(user_id)  |

### `feedback`
| Column          | Type          | Constraints              |
|-----------------|---------------|--------------------------|
| feedback_id     | BIGINT        | PK, AUTO_INCREMENT       |
| driver_rating   | INT           |                          |
| service_rating  | INT           |                          |
| overall_rating  | INT           |                          |
| rating          | INT           |                          |
| comments        | VARCHAR(1000) |                          |
| feedback_date   | DATETIME      |                          |
| submitted_at    | DATETIME      |                          |
| user_id         | BIGINT        | FK → user(user_id)       |
| bus_id          | BIGINT        | FK → bus(bus_id)         |
| reservation_id  | BIGINT        | FK → reservation(reservation_id)|

---

## Sample Data

> ⚠️ **Security Warning:** The original `data.sql` stores plain-text passwords. In production, always hash passwords using **BCrypt** before inserting. Use placeholders below and replace with hashed values.

### Complete SQL Setup Script

```sql
-- ============================================================
-- BusReservationApplication - Database Setup Script
-- ============================================================

-- 1. Create and select database
DROP DATABASE IF EXISTS busdb;
CREATE DATABASE busdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE busdb;

-- ============================================================
-- 2. Create Tables (Hibernate handles this via ddl-auto=update,
--    but included here for manual setup / CI environments)
-- ============================================================

DROP TABLE IF EXISTS feedback;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS bus;
DROP TABLE IF EXISTS route;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS admin;

CREATE TABLE admin (
    admin_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    admin_username VARCHAR(255) NOT NULL,
    admin_password VARCHAR(255) NOT NULL
);

CREATE TABLE user (
    user_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    contact    VARCHAR(255),
    email      VARCHAR(255)
);

CREATE TABLE route (
    route_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_from VARCHAR(255) NOT NULL,
    route_to   VARCHAR(255) NOT NULL,
    distance   INT NOT NULL
);

CREATE TABLE bus (
    bus_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    bus_name        VARCHAR(255),
    driver_name     VARCHAR(255),
    bus_type        VARCHAR(255),
    arrival_time    TIME,
    departure_time  TIME,
    seats           INT,
    available_seats INT,
    price           DOUBLE,
    route_id        BIGINT,
    CONSTRAINT fk_bus_route FOREIGN KEY (route_id) REFERENCES route(route_id)
);

CREATE TABLE reservation (
    reservation_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_status VARCHAR(255),
    reservation_type   VARCHAR(255),
    reservation_date   DATE,
    reservation_time   VARCHAR(255),
    source             VARCHAR(255),
    destination        VARCHAR(255),
    journey_date       DATETIME,
    journey_started    BOOLEAN DEFAULT FALSE,
    journey_ended      BOOLEAN DEFAULT FALSE,
    seats_requested    INT,
    bus_id             BIGINT,
    user_id            BIGINT,
    CONSTRAINT fk_res_bus  FOREIGN KEY (bus_id)  REFERENCES bus(bus_id),
    CONSTRAINT fk_res_user FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE feedback (
    feedback_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    driver_rating  INT,
    service_rating INT,
    overall_rating INT,
    rating         INT,
    comments       VARCHAR(1000),
    feedback_date  DATETIME,
    submitted_at   DATETIME,
    user_id        BIGINT,
    bus_id         BIGINT,
    reservation_id BIGINT,
    CONSTRAINT fk_fb_user FOREIGN KEY (user_id)        REFERENCES user(user_id),
    CONSTRAINT fk_fb_bus  FOREIGN KEY (bus_id)         REFERENCES bus(bus_id),
    CONSTRAINT fk_fb_res  FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id)
);

-- ============================================================
-- 3. Sample Data (replace password placeholders with BCrypt hash)
-- ============================================================

-- Admin (replace <HASHED_ADMIN_PASSWORD> with BCrypt hash of your password)
INSERT INTO admin (admin_username, admin_password)
VALUES ('admin', '<HASHED_ADMIN_PASSWORD>');

-- Sample User (replace <HASHED_USER_PASSWORD> with BCrypt hash)
INSERT INTO user (username, password, first_name, last_name, contact, email)
VALUES ('sathya', '<HASHED_USER_PASSWORD>', 'Sathya', 'Kumar', '9876543210', 'sathya@example.com');

-- Routes
INSERT INTO route (route_from, route_to, distance) VALUES ('Hyderabad', 'Bengaluru', 500);
INSERT INTO route (route_from, route_to, distance) VALUES ('Hyderabad', 'Chennai',   630);
INSERT INTO route (route_from, route_to, distance) VALUES ('Hyderabad', 'Mumbai',    710);

-- Bus (linked to route_id = 1: Hyderabad → Bengaluru)
INSERT INTO bus (bus_name, driver_name, bus_type, arrival_time, departure_time, seats, available_seats, price, route_id)
VALUES ('Volvo Express', 'Ramesh', 'AC Sleeper', '08:00:00', '20:00:00', 40, 40, 1200.00, 1);

-- ============================================================
-- 4. Verification Queries
-- ============================================================

SELECT * FROM admin;
SELECT * FROM user;
SELECT * FROM route;
SELECT * FROM bus;
SELECT b.bus_name, b.bus_type, b.price, r.route_from, r.route_to
FROM bus b JOIN route r ON b.route_id = r.route_id;
```

> 💡 **Generating a BCrypt hash** (use this in a quick Java snippet or online tool):
> ```java
> System.out.println(new BCryptPasswordEncoder().encode("yourPassword"));
> ```

---

## Setup Instructions

### Prerequisites

- Java 17+
- Maven 3.9+
- MySQL 8.0+
- Git

### Step 1 — Clone the Repository

```bash
git clone https://github.com/SaiDeekshith/BusReservationSystem.git
cd BusReservationSystem
```

### Step 2 — Create MySQL User (Recommended)

Avoid using `root` in production. Create a dedicated DB user:

```sql
CREATE USER 'busapp_user'@'localhost' IDENTIFIED BY '<YOUR_SECURE_PASSWORD>';
GRANT ALL PRIVILEGES ON busdb.* TO 'busapp_user'@'localhost';
FLUSH PRIVILEGES;
```

### Step 3 — Configure Environment Variables

Copy the example env file and fill in your values:

```bash
cp .env.example .env
```

Then update `src/main/resources/application.properties` to use environment variables (see [Environment Variables](#environment-variables) section).

### Step 4 — Run Database Script (Optional)

If you want to pre-seed the database manually:

```bash
mysql -u busapp_user -p busdb < src/main/resources/setup.sql
```

Otherwise, Hibernate will auto-create tables on first startup via `ddl-auto=update`, and `data.sql` will seed initial rows.

### Step 5 — Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

Application starts at: **http://localhost:7383**

---

## Environment Variables

Create a `.env.example` file at the project root (copy to `.env` and populate):

```dotenv
# .env.example — copy to .env and fill in real values. Never commit .env to Git.

# Database
DB_URL=jdbc:mysql://localhost:3306/busdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
DB_USERNAME=busapp_user
DB_PASSWORD=your_secure_password_here

# Server
SERVER_PORT=7383

# JPA
JPA_DDL_AUTO=update
JPA_SHOW_SQL=false
```

Update `application.properties` to reference these:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
server.port=${SERVER_PORT:7383}
spring.jpa.hibernate.ddl-auto=${JPA_DDL_AUTO:update}
spring.jpa.show-sql=${JPA_SHOW_SQL:false}
```

> ⚠️ Add `.env` to your `.gitignore`. Never commit real credentials.

---

## Web Routes / Endpoints

### Auth Routes (`/auth`)

| Method | Path                    | Description              | Access |
|--------|-------------------------|--------------------------|--------|
| GET    | `/auth/register`        | Show user registration   | Public |
| POST   | `/auth/register`        | Submit registration      | Public |
| GET    | `/auth/login`           | Show login page          | Public |
| POST   | `/auth/login-user`      | User login               | Public |
| POST   | `/auth/login-admin`     | Admin login              | Public |
| GET    | `/auth/logout`          | Logout                   | All    |

### User Routes (`/user`)

| Method | Path                     | Description              | Access |
|--------|--------------------------|--------------------------|--------|
| GET    | `/user/search`           | Search available buses   | User   |
| GET    | `/user/reservations`     | View my reservations     | User   |
| GET    | `/user/login`            | User login (alternate)   | Public |
| POST   | `/user/register`         | Register user            | Public |

### Reservation Routes (`/reservations`)

| Method | Path                          | Description              | Access |
|--------|-------------------------------|--------------------------|--------|
| GET    | `/reservations`               | List all reservations    | User   |
| GET    | `/reservations/new`           | Show booking form        | User   |
| POST   | `/reservations`               | Submit booking           | User   |
| POST   | `/reservations/{id}/cancel`   | Cancel a reservation     | User   |
| GET    | `/reservations/{id}/payment`  | Show payment page        | User   |
| POST   | `/reservations/{id}/pay`      | Process payment          | User   |

### Admin Routes (`/admin`)

| Method | Path                       | Description              | Access |
|--------|----------------------------|--------------------------|--------|
| GET    | `/admin`                   | Admin dashboard          | Admin  |
| GET    | `/admin/buses`             | List all buses           | Admin  |
| GET    | `/admin/buses/new`         | Add new bus form         | Admin  |
| POST   | `/admin/buses`             | Save new bus             | Admin  |
| GET    | `/admin/buses/{id}`        | View bus details         | Admin  |
| POST   | `/admin/buses/{id}`        | Update bus               | Admin  |
| POST   | `/admin/buses/{id}/delete` | Delete bus               | Admin  |
| GET    | `/admin/routes`            | List all routes          | Admin  |
| GET    | `/admin/routes/new`        | Add new route form       | Admin  |
| POST   | `/admin/routes`            | Save new route           | Admin  |
| GET    | `/admin/routes/{id}`       | View route               | Admin  |
| POST   | `/admin/routes/{id}`       | Update route             | Admin  |
| POST   | `/admin/routes/{id}/delete`| Delete route             | Admin  |

### Bus Routes (`/buses`)

| Method | Path              | Description      | Access |
|--------|-------------------|------------------|--------|
| GET    | `/buses`          | List buses       | User   |
| GET    | `/buses/new`      | Add bus form     | Admin  |
| POST   | `/buses`          | Save bus         | Admin  |
| POST   | `/buses/delete`   | Delete bus       | Admin  |

### Feedback Routes (`/feedback`)

| Method | Path                              | Description           | Access |
|--------|-----------------------------------|-----------------------|--------|
| GET    | `/feedback/{reservationId}`       | Show feedback form    | User   |
| POST   | `/feedback/{reservationId}`       | Submit feedback       | User   |
| GET    | `/feedback/admin`                 | View all feedback     | Admin  |
| GET    | `/feedback/admin/{id}`            | View feedback detail  | Admin  |

---

## Running Locally

```bash
# 1. Start MySQL
mysql.server start   # macOS (Homebrew)
# or: sudo systemctl start mysql  (Linux)

# 2. Run the app
mvn spring-boot:run

# 3. Open browser
open http://localhost:7383

# Default admin credentials (change immediately after first login):
# Username: admin
# Password: admin123  ← replace with your hashed password in data.sql
```

---

## Testing

```bash
# Run all unit tests
mvn test

# Run a specific test class
mvn test -Dtest=BusReservationApplicationTests

# Skip tests during build
mvn clean install -DskipTests
```

The test class is located at:
```
src/test/java/com/demo/BusReservationApplicationTests.java
```

> Extend the test suite using `@SpringBootTest` for integration tests and Mockito for service-layer unit tests.

---

## Security Notes

| Risk                         | Current State               | Recommended Fix                              |
|------------------------------|-----------------------------|----------------------------------------------|
| Plain-text passwords         | ⚠️ Yes (in data.sql)        | Use `BCryptPasswordEncoder` in `AuthService` |
| Credentials in .properties   | ⚠️ Hardcoded                | Use `.env` + environment variable injection  |
| No Spring Security           | ⚠️ Session-based manual auth| Add `spring-boot-starter-security`           |
| SSL disabled                 | `useSSL=false`              | Enable SSL in production MySQL               |
| `ddl-auto=update` in prod    | ⚠️ Risk of data loss        | Switch to `validate` + use Flyway/Liquibase  |
| `.gitignore` for secrets     | Not confirmed               | Add `.env`, `*.properties` to `.gitignore`   |

---

## Migrations & Backups

### Schema Migrations (Recommended: Flyway)

Add to `pom.xml`:
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

Create migration files under `src/main/resources/db/migration/`:
```
V1__init_schema.sql
V2__add_feedback_table.sql
```

Set in `application.properties`:
```properties
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
```

### Database Backups

```bash
# Full backup
mysqldump -u busapp_user -p busdb > busdb_backup_$(date +%F).sql

# Restore from backup
mysql -u busapp_user -p busdb < busdb_backup_2025-01-01.sql

# Schedule daily backups (Linux cron)
0 2 * * * mysqldump -u busapp_user -p'<password>' busdb > /backups/busdb_$(date +\%F).sql
```

---

## FAQ

**Q: The app starts but shows no buses/routes — why?**
> `data.sql` runs only when `spring.sql.init.mode=always`. Confirm this is set in `application.properties`. Also check that `spring.sql.init.continue-on-error=true` is set to avoid silent failures.

**Q: I get `Access denied for user 'root'@'localhost'`**
> Update `spring.datasource.username` and `spring.datasource.password` in `application.properties` (or via environment variables) to match your local MySQL credentials.

**Q: How do I change the server port?**
> Update `server.port=7383` in `application.properties` or set `SERVER_PORT` in your `.env`.

**Q: Can I use H2 for local development instead of MySQL?**
> Yes. Replace the MySQL dependency with `h2` in `pom.xml` and update your datasource URL to `jdbc:h2:mem:busdb`. Good for quick testing without MySQL setup.

**Q: How do I add password hashing?**
> Inject `BCryptPasswordEncoder` as a Spring Bean, then call `.encode(rawPassword)` before saving any user or admin entity. Verify with `.matches(rawPassword, storedHash)` during login.

**Q: Where are the JSP view files?**
> All views are under `src/main/webapp/WEB-INF/jsp/` and are resolved via the prefix/suffix configured in `application.properties`.

---

## Project Structure

```
BusReservationApplication/
├── src/
│   ├── main/
│   │   ├── java/com/bus/
│   │   │   ├── controller/       # MVC Controllers
│   │   │   ├── service/          # Business Logic
│   │   │   ├── repository/       # Spring Data JPA Repos
│   │   │   ├── model/            # JPA Entities
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── exception/        # Custom Exceptions
│   │   │   └── config/           # MVC Config
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   └── data.sql          # Seed data
│   │   └── webapp/WEB-INF/jsp/   # JSP Views
│   └── test/
├── .env.example
├── pom.xml
└── README.md
```

---

> Built by [Ganji Sai Deekshith](https://portfolio-main-deekshith.vercel.app/) · Java Full Stack Developer
