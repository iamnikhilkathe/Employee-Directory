# Employee Directory API

Spring Boot REST API for managing departments, employees, and skills. Data is stored in an in-memory H2 database, so no external database is required.

## Requirements

- Java 25 (JDK), with `JAVA_HOME` configured and `java` available on `PATH`
- Internet access the first time Maven downloads project dependencies
- No separate Maven installation is required; the project includes the Maven Wrapper

To run the container instead, install Docker Desktop (Windows/macOS) or Docker Engine (Linux). A local Java or Maven installation is not needed for the Docker workflow.

Check Java is installed:

```shell
java -version
```

The output should report Java 25.

## Run the application

1. Open a terminal in the project root, the folder containing `pom.xml`.
2. Start the application with the Maven Wrapper.

   Windows PowerShell:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   macOS/Linux:

   ```shell
   ./mvnw spring-boot:run
   ```

3. Wait until Spring Boot reports that the application has started. It listens on port `8086` by default.
4. Open Swagger UI at <http://localhost:8086/swagger-ui.html>. The UI loads the specification from `/openapi.yaml`.

To build a runnable JAR:

```shell
./mvnw clean package
```

On Windows, use `.\mvnw.cmd clean package`. The JAR is created under `target/`.

## Run with Docker

1. Install and start Docker Desktop or Docker Engine.
2. Open a terminal in the project root, next to the `Dockerfile`.
3. Build the image:

   ```shell
   docker build -t employee-directory-api .
   ```

4. Start the container and publish its port:

   ```shell
   docker run --rm --name employee-directory -p 8086:8086 employee-directory-api
   ```

5. Open Swagger UI at <http://localhost:8086/swagger-ui.html>. You can check the running service at <http://localhost:8086/api/info>.

The Docker commands above work in Windows PowerShell, macOS, and Linux terminals. Press `Ctrl+C` in the terminal running the container to stop it; `--rm` removes the stopped container automatically.

The first Docker build downloads the Java/Maven base images and project dependencies. The H2 database runs in memory inside the container, so its data is cleared when the container stops. You can override the same settings used for a local run with `docker run -e`, for example:

```shell
docker run --rm --name employee-directory -p 8086:8086 -e APP_NAME="HR Directory" -e MAX_SKILLS_PER_EMPLOYEE=12 employee-directory-api
```

## Run tests

Run unit and integration tests with:

```shell
./mvnw test
```

On Windows PowerShell:

```powershell
.\mvnw.cmd test
```

The integration tests use `@SpringBootTest`, `MockMvc`, and the in-memory H2 database.

## Configuration

Application settings are in `src/main/resources/application.yaml` and can be overridden with environment variables:

| Setting | Environment variable | Default |
| --- | --- | --- |
| Server port | `SERVER_PORT` | `8086` |
| App name | `APP_NAME` | `employee-directory` |
| App version | `APP_VERSION` | `1.0` |
| Maximum skills per employee | `MAX_SKILLS_PER_EMPLOYEE` | `10` |
| Default pagination page size | `PAGINATION_DEFAULT_PAGE_SIZE` | `10` |

The H2 database is in-memory and resets when the application stops. The H2 console is available at <http://localhost:8086/h2-console> while the application is running. Use JDBC URL `jdbc:h2:mem:employeedirectory_h2_db`, username `sa`, and leave the password empty.

## API endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/info` | Read configured application name and version |
| `POST`, `GET` | `/api/departments` | Create or page through departments |
| `GET`, `PUT`, `DELETE` | `/api/departments/{id}` | Read, update, or delete a department |
| `GET` | `/api/departments/{id}/employees` | Page through a department's employees |
| `POST`, `GET` | `/api/skills` | Create or page through skills |
| `POST`, `GET` | `/api/employees` | Create employees or list/filter employees |
| `GET`, `PUT`, `DELETE` | `/api/employees/{id}` | Read, update, or delete an employee |
| `POST`, `DELETE` | `/api/employees/{id}/skills/{sid}` | Assign or remove an employee skill |

Paged list endpoints accept `page` (zero-based), `size`, and `sort` query parameters. Example: `/api/departments?page=0&size=10&sort=name,asc`.

## Try the API with curl

The following steps create a department, a skill, and an employee who has that skill. Replace IDs in later requests with the IDs returned by your API.

The commands below use a POSIX shell. In Windows PowerShell, use `curl.exe` instead of `curl` (PowerShell defines `curl` as an alias).

1. Create a department:

   ```shell
   curl -X POST http://localhost:8086/api/departments -H "Content-Type: application/json" -d '{"name":"Engineering","location":"London"}'
   ```

2. Create a skill:

   ```shell
   curl -X POST http://localhost:8086/api/skills -H "Content-Type: application/json" -d '{"name":"Java","category":"Backend"}'
   ```

3. Create an employee. Use the `id` values from steps 1 and 2:

   ```shell
   curl -X POST http://localhost:8086/api/employees -H "Content-Type: application/json" -d '{"name":"Alex Morgan","email":"alex@example.com","designation":"Software Engineer","joiningDate":"2024-01-15","departmentId":1,"skillIds":[1]}'
   ```

4. List employees in the department:

   ```shell
   curl "http://localhost:8086/api/employees?departmentId=1&page=0&size=10&sort=name,asc"
   ```

5. Get a department's employees:

   ```shell
   curl "http://localhost:8086/api/departments/1/employees?page=0&size=10"
   ```

6. Add a skill to an existing employee:

   ```shell
   curl -X POST http://localhost:8086/api/employees/1/skills/1
   ```

7. Remove a skill from an employee:

   ```shell
   curl -X DELETE http://localhost:8086/api/employees/1/skills/1
   ```

Validation and request errors return a JSON response containing a timestamp, HTTP status, message, and any field-specific validation details.

## OpenAPI specification

The maintained OpenAPI 3.0 YAML file is at the project root: [`openapi.yaml`](openapi.yaml). A copy under `src/main/resources/static/openapi.yaml` is served to Swagger UI at `/openapi.yaml`; keep both files in sync when updating the API documentation.
