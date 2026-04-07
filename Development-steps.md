## Section 1: Project Setup & Email Verification

**Step 1:** Initialize the Spring Boot project via [Spring Initializr](https://start.spring.io/).
Select the following dependencies:
- **Spring Web**: For REST APIs.
- **Spring Data JPA**: For database operations.
- **PostgreSQL Driver**: To connect with the PostgreSQL database.
- **Lombok**: To reduce boilerplate code (Getter, Setter, Builder).
- **Spring Boot Starter Validation**: For request body validation.
- **Spring Boot Starter Mail**: For sending verification emails.
- **Spring Boot Starter Security**: For authentication and JWT.
- **Spring Boot Starter Actuator**: For application health monitoring.

**Step 2:** Configure PostgreSQL in `application.properties`.
```properties
spring.datasource.url=jdbc:postgresql://{your-database-host}:{your-database-port}/{your-database-name}
spring.datasource.username={your-database-username}
spring.datasource.password={your-password}
spring.jpa.hibernate.ddl-auto=update
```

**Step 3:** Verify application health using Spring Actuator.
Access the health check endpoint at: `GET http://localhost:9090/actuator/health`

**Step 4:** Create the Register API `/api/auth/register`.
- Create `RegisterRequest` and `AuthResponse` DTOs.
- Implement the `User` entity and `UserRepository`.
- In `AuthService`, encode the password and generate a unique `verificationToken` (UUID).
- Save the user with `emailVerified = false`.

**Step 5:** Integrate Gmail SMTP for verification emails.
- Add Gmail SMTP details to `application.properties`.
- Implement `EmailService` to send HTML-formatted emails.
- In `AuthService`, trigger the verification email upon successful registration.

**Step 6:** Create the Email Verification API `/api/auth/verify-email`.
- Accept the `token` as a request parameter.
- Find the user by token and check if the token has expired (24-hour limit).
- Update `emailVerified = true` and clear the token fields.

---

## Section 2: Media Management (Cloudinary)

**Step 1:** Add the Cloudinary dependency to `pom.xml`.

**Step 2:** Configure Cloudinary credentials in `application.properties`.
- `cloudinary.cloud-name`, `api-key`, and `api-secret`.

**Step 3:** Create `CloudinaryConfig` to initialize the Cloudinary bean.

**Step 4:** Implement `FileUploadService` to handle image uploads to the cloud.

---

## Section 3: Spring Security & JWT Authentication

**Step 1:** Configure `SecurityConfig` to define the filter chain.
- Permit all requests to `/api/auth/**` and `/actuator/health`.
- Require authentication for all other `/api/**` endpoints.

**Step 2:** Implement `JwtUtil` for generating and validating JSON Web Tokens.

**Step 3:** Create the Login API `/api/auth/login`.
- Validate credentials using `PasswordEncoder`.
- Ensure `emailVerified` is `true` before allowing login.
- Return a JWT token in the `AuthResponse`.

**Step 4:** Implement `JwtAuthenticationFilter` to intercept requests and validate the JWT.

**Step 5:** Implement `JwtAuthenticationEntryPoint` to handle unauthorized access (HTTP 401).

**Step 6:** Add the **Resend Verification** API `/api/auth/resend-verification`.
- Allow users to request a new verification link if the previous one expired.

**Step 7:** Add the **Get Profile** API `/api/auth/profile`.
- Fetch the authenticated user's details using the security context principal.

---

## Section 4: Resume Management

**Step 1:** Create `Resume` entity to store resume details (Profile, Education, Experience, etc.)

**Step 2:** Create `ResumeRepository` for PostgreSQL operations

**Step 3:** Create `CreateResumeRequest` DTO for validating input data

**Step 4:** Implement `ResumeService` for logic to:
- Create a new resume and link it to the authenticated user
- Fetch all resumes for the logged-in user
- Fetch a specific resume by ID with ownership validation
- Update existing resume data
- Delete a resume

**Step 5:** Create `ResumeController` with the following endpoints:
- `POST /api/resume` - Create resume
- `GET /api/resume` - Get all user resumes
- `GET /api/resume/{id}` - Get resume by ID
- `PUT /api/resume/{id}` - Update resume
- `DELETE /api/resume/{id}` - Delete resume

**Step 6:** Test the CRUD operations using Postman or Swagger

## Section 5: Email Resume Feature

**Step 1:** Update `EmailService` to support sending emails with attachments (using `MimeMessageHelper`)

**Step 2:** Create `EmailController` with endpoint `/api/email/send-resume`

**Step 3:** Implement logic to:
- Accept `recipientEmail`, `subject`, `message`, and `pdfFile` (Multipart)
- Validate input parameters
- Send the PDF as an attachment via SMTP

**Step 4:** Test the email sending functionality with a sample PDF file
