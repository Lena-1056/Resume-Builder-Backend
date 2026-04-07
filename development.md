# Backend Development Guide & API Summary

## API Overview
The backend is powered by Spring Boot (Java 17+). It relies on JWT mapped endpoints to create/view/update/delete AI Resumes.

### 1. Authentication Endpoints (Base `http://localhost:8080/api/auth`)
| Method | Endpoint | Description | Payload Body Expected | Auth Required? |
|--------|---------|-------------|-----------------------|----------------|
| `POST` | `/register` | Register an account | `{ "name": "...", "email": "...", "password": "..." }` | No |
| `POST` | `/login` | Sign in / Authenticate | `{ "email": "...", "password": "..." }` | No (Returns JWT) |
| `GET` | `/verify-email?token={token}` | Verify user email | `Query Param: token` | No |
| `POST` | `/resend-verification` | Resend verify link | `{ "email": "..." }` | No |
| `GET` | `/profile` | Retrieve user profile | - | Yes |

### 2. Resume Operation Endpoints (Base `http://localhost:8080/api/resumes`)
*Note: All endpoints require a valid JWT token sent in headers: `Authorization: Bearer <TOKEN>`*

| Method | Endpoint | Description | Expected Request Body |
|--------|---------|-------------|-----------------------|
| `GET` | `/` | Fetch all user resumes | - |
| `POST` | `/` | Create a new resume | Fully nested `Resume` JSON payload (e.g., title, profileInfo mapping) |
| `GET` | `/{id}` | Fetch a single resume | - |
| `PUT` | `/{id}` | Update an existing resume | Fully nested modified `Resume` JSON payload |
| `DELETE` | `/{id}` | Delete a specific resume | - |

## Setup & Running the Application Locally

1. **Verify JDK**: Ensure Java 17+ is installed.
2. **Setup Maven**: The project includes a maven wrapper (`mvnw`).
3. **Execute Spring Boot Application**:
   ```bash
   cd Backend
   ./mvnw spring-boot:run
   ```
4. **Database Configuration**:
   Verify the `src/main/resources/application.properties` or `application.yml` has the correct SQL credentials pointing to a live local/hosted instance before launching.

## Recommended Fixes / Enhancements
- **AI Endpoints**: The UI explicitly caters to an AI-powered flow. There must be an integration later (e.g., `/api/ai/generate-summary` payload) relying on an LLM to generate custom professional summaries based on inputs.
- **Image Handling**: File uploads `/upload-images` appear to be commented out in controllers. Enable AWS S3 or Local File System configurations safely if image storage is necessary.
