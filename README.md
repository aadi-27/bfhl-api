# BFHL REST API — Spring Boot

Bajaj Finserv Health Challenge submission by **Aadi Attrey** (2310991753)

## Endpoints

| Method | Route    | Description           |
|--------|----------|-----------------------|
| POST   | /bfhl    | Main processing route |
| GET    | /health  | Health check          |

## Request

```json
POST /bfhl
Content-Type: application/json

{
  "data": ["a", "1", "334", "4", "R", "$"]
}
```

## Response

```json
{
  "is_success": true,
  "user_id": "aadi_attrey_27082055",
  "email": "aadi1753.be23@chitkara.edu.in",
  "roll_number": "2310991753",
  "odd_numbers": ["1"],
  "even_numbers": ["334", "4"],
  "alphabets": ["A", "R"],
  "special_characters": ["$"],
  "sum": "339",
  "concat_string": "Ra"
}
```

## Run Locally

```bash
mvn spring-boot:run
```

## Run Tests

```bash
mvn test
```

## Deploy on Render

1. Push this repo to GitHub
2. Go to [render.com](https://render.com) → New → Web Service
3. Connect your GitHub repo
4. Set:
   - **Environment**: Docker
   - **Port**: 8080
5. Deploy — Render auto-detects the Dockerfile

## Project Structure

```
src/
├── main/java/com/aadi/bfhl/
│   ├── BfhlApplication.java
│   ├── controller/BfhlController.java
│   ├── service/BfhlService.java          ← interface
│   ├── service/impl/BfhlServiceImpl.java ← implementation
│   ├── dto/BfhlRequestDTO.java
│   ├── dto/BfhlResponseDTO.java
│   ├── dto/ErrorResponseDTO.java
│   └── exception/GlobalExceptionHandler.java
└── test/java/com/aadi/bfhl/
    ├── controller/BfhlControllerTest.java
    └── service/BfhlServiceImplTest.java
```
