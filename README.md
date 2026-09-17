# DeliveryMatch

A Spring Boot backend that matches delivery agents to pending orders using geospatial indexing (Uber's H3 library) and a greedy nearest-match algorithm, secured with JWT-based authentication and role-based authorization.

## How it works

- **Users** register as `USER`, `AGENT`, or (internally only) `ADMIN`.
- **Agents** report their location and availability. Each location is bucketed into an H3 cell at a fixed resolution for fast spatial lookups.
- **Orders** are placed by users, also bucketed into an H3 cell.
- On dispatch, the system looks up all H3 cells within a configurable ring (`k`) around each pending order, collects nearby idle agents as **candidates**, computes real distance (haversine formula), and hands the candidate list to a **matching engine**.
- The current matching engine (`GreedyMatchingEngine`) sorts all candidates by distance and greedily assigns each order to the nearest agent with spare capacity, so no order or agent gets double-booked in a single dispatch run.
- Matches are persisted as **assignments**, and the matched order/agent are updated accordingly.

## Tech stack

- Java 25, Spring Boot 4.1.1
- Spring Web MVC, Spring Data JPA, Spring Security (JWT-based, stateless)
- PostgreSQL
- [Uber H3](https://h3geo.org/) for geospatial indexing
- JJWT for token generation/validation
- Maven (via the included wrapper, `./mvnw`)

## Project structure

```
controllers/   REST endpoints — routing, request/response shaping, @PreAuthorize checks
services/      Business logic (AgentService, OrderService, UserService, DispatchService)
matching/      Pluggable matching engine interface + greedy implementation
models/        JPA entities (User, Agent, Order, Assignment)
repositories/  Spring Data JPA repositories
filters/       JwtFilter — extracts and validates the bearer token per request
configs/       Spring Security configuration
```

## Running locally

1. Start Postgres (schema is auto-applied via `db/schema.sql`):

   ```
   docker compose up -d
   ```

2. Set the required environment variables (example values below — change them for anything beyond local dev):

   ```
   export DB_URL=jdbc:postgresql://localhost:5432/dispatch
   export DB_USERNAME=dispatch_user
   export DB_PASSWORD=dispatch_pass
   export JWT_SECRET=TNiy9WpdrzadMbBZ6/8uE/HhWhpddN0m1n02DtGJQDqJSZgo0JgCRNb3QcEj8AJgaEe2tt2JydVMs+AGqbYevQ==
   ```

3. Run the app:

   ```
   ./mvnw spring-boot:run
   ```

## API

| Method | Endpoint          | Role required | Description                              |
|--------|-------------------|---------------|------------------------------------------|
| GET    | `/`                | none          | Health check                             |
| POST   | `/users/register`  | none          | Register as `USER` or `AGENT`            |
| POST   | `/users/login`     | none          | Authenticate, returns a JWT              |
| POST   | `/agents/add`      | `AGENT`       | Register the current user as an agent    |
| POST   | `/agents/status`   | `AGENT`       | Update agent status (`IDLE`/`BUSY`/`INACTIVE`) |
| POST   | `/agents/location` | `AGENT`       | Update agent's current location          |
| POST   | `/orders/add`      | `USER`        | Place a new delivery order                |
| POST   | `/dispatch/run`    | `ADMIN`       | Trigger a dispatch pass across all pending orders |

All protected endpoints expect `Authorization: Bearer <token>`, using the token returned from `/users/login`.

## Testing

Tests are layered by scope, from fastest/narrowest to broadest, and each layer intentionally checks something the others can't:

| Layer | Example | What it verifies |
|---|---|---|
| **Unit test** | `GreedyMatchingEngineTest` | Pure algorithm correctness (nearest-match, capacity limits) |
| **Mockito service test** | `AgentServiceTest` | Business rules (e.g. rejecting a duplicate agent registration) |
| **`@WebMvcTest` (web slice)** | `AgentControllerTest` | HTTP routing, JSON (de)serialization, and status-code mapping (`200` vs `409`) |

Run all tests:

```
./mvnw test
```

Run a single test class:

```
./mvnw test -Dtest=AgentServiceTest
```

**Decisions:**
- `@WebMvcTest` here runs with `@PreAuthorize` filters disabled, so it currently checks request/response shape rather than authorization itself.

