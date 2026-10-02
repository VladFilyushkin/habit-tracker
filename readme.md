# Habit Tracker

A REST API for tracking habits: creating habits, marking them as completed, calculating streaks, and viewing completion statistics. Each user's data is isolated via JWT authentication — a user can only see and modify their own habits.

## Tech Stack

- Java 21, Spring Boot 4.1
- Spring Security + JWT (jjwt)
- Spring Data JPA + PostgreSQL
- Liquibase (database migrations)
- MapStruct (DTO mapping)
- springdoc-openapi (Swagger UI)

## Running with Docker

### 1. Requirements

- Docker and Docker Compose installed

### 2. Set up environment variables

Create a `.env` file in the project root (next to `docker-compose.yaml`):

```env
JWT_SECRET=replace-with-a-long-random-string-at-least-32-characters
DB_PASSWORD=secret
```

`.env` is not committed to git — it's already listed in `.gitignore`.

### 3. Start the application

```bash
docker compose up -d --build
```

This starts:
- a `habit-postgres` container with the `habit_tracker` database
- the application container on port `8081`

Liquibase automatically applies all migrations on first startup.

### 4. Verify it's running

Application: `http://localhost:8081`
Swagger UI: `http://localhost:8081/swagger-ui.html`

### 5. Useful commands

```bash
docker compose logs -f app     # application logs
docker compose ps              # container status
docker compose down            # stop, keep database data
docker compose down -v         # stop and remove database data
```

## Endpoints

### Authentication (public)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Log in, receive a JWT token |

### Habits (authentication required)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/habits` | Create a habit |
| GET | `/api/habits` | Get all your habits |
| GET | `/api/habits/{id}` | Get a habit by id |
| PUT | `/api/habits/{id}` | Update a habit |
| DELETE | `/api/habits/{id}` | Delete a habit |

### Records (authentication required)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/habits/{habitId}/records` | Mark a habit as completed today |
| GET | `/api/habits/{habitId}/records` | Get all records for a habit |

### Stats (authentication required)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/habits/{id}/stats` | Current streak, best streak, and completion rate for one habit |
| GET | `/api/stats/daily` | Today's completion summary across all habits |
| GET | `/api/stats/week` | Completion progress for the last 7 days |

## Working with Authentication

### 1. Register

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "vlad213", "password": "password123"}'
```

### 2. Log in and get a token

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "vlad213", "password": "password123"}'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 3. Using the token

Pass the token in the `Authorization` header for every protected endpoint:

```bash
curl -X GET http://localhost:8081/api/habits \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

Without the header (or with an invalid/expired token), the server responds with `401 Unauthorized`.

### Via Swagger UI

1. Open `http://localhost:8081/swagger-ui.html`
2. Register via `/api/auth/register`, then log in via `/api/auth/login`
3. Copy the `token` value from the response
4. Click the **Authorize** button in the top-right corner
5. Paste only the token itself, without the word `Bearer` — Swagger adds the prefix automatically
6. All subsequent requests made from Swagger UI will be authorized automatically

## Testing

The project includes unit tests (JUnit 5 + Mockito) and integration tests (RestAssured + Zonky embedded PostgreSQL).

- **Unit tests** mock repositories and dependencies to verify service-level logic in isolation (streak calculation, ownership checks, exception handling) without touching a real database.
- **Integration tests** spin up a real Spring context together with a Zonky embedded PostgreSQL instance, applying the actual Liquibase migrations, and exercise the full HTTP stack — including the JWT security filter — through RestAssured requests against the running application.

Run all tests:

```bash
./mvnw test
```
