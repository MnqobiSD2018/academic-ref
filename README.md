# Academic Project Reference System (HIT)

A Spring Boot backend for managing and searching academic projects at HIT.

## What this project does
- Manages academic years and projects.
- Supports project document uploads (PDFs).
- Provides authentication with JWT.
- Allows project search by keyword, department, and level.
- Serves the built React frontend from Spring static resources.

## Tech stack
- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

## Project status
- Backend API is available.
- Frontend is in built in the `\academic-ref\src\main\resources\static` folder
- Backend and frontend are served together from the same Spring Boot app on one port.

## Frontend integration (React + Vite)

### Frontend source project
- Frontend code lives in: `https://github.com/MnqobiSD2018/academic-ref-fe`


### Routing behavior (SPA refresh support)
- API endpoints remain under `/api/...`.
- Frontend routes such as `/login`, `/years`, `/projects/...`, `/search`, and `/admin` are forwarded to `index.html` by `SpaForwardController`.
- This prevents `404 No static resource ...` errors when refreshing frontend pages.

## Run locally
1. Create a PostgreSQL database named `academic_refs`.
2. Update database credentials in `src/main/resources/application.properties`.
3. Ensure frontend build is copied to `src/main/resources/static` .
4. Start the app:

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

