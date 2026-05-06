# Academic Project Reference System (HIT)

A Spring Boot backend for managing and searching academic projects at HIT.

## What this project does
- Manages academic years and projects.
- Supports project document uploads (PDFs).
- Provides authentication with JWT.
- Allows project search by keyword, department, and level.

## Tech stack
- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

## Project status
- Backend API is available and in progress.
- Frontend is not available yet.
- Frontend will be added later in: `src/main/resources/static`.

## Run locally
1. Create a PostgreSQL database named `academic_refs`.
2. Update database credentials in `src/main/resources/application.properties`.
3. Start the app:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Default server URL: http://localhost:8080

## API base path
- Main API routes use `/api/...`

## Notes
This README will be updated again once the frontend is added.