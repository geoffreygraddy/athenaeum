# Athenaeum Backend

Spring Boot backend application for the Athenaeum project. This backend provides REST APIs, session management, and is structured for scalability.

## Technology Stack

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Maven**: 3.9.11
- **Key Dependencies**:
  - Spring Web (REST APIs)
  - Spring Security (Authentication/Authorization)
  - Spring Session (Session Management)
  - Spring Boot Actuator (Health Checks and Metrics)
  - Spring Boot DevTools (Development Hot Reload)

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- IDE with Java support (IntelliJ IDEA, Eclipse, VS Code with Java extensions)

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/athenaeum/backend/
│   │   │   ├── AtheneumBackendApplication.java   # Main application entry point
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java            # Security configuration
│   │   │   └── controller/
│   │   │       └── HealthCheckController.java     # Health check endpoint
│   │   └── resources/
│   │       └── application.yml                     # Application configuration
│   └── test/
│       └── java/com/athenaeum/backend/
│           ├── AtheneumBackendApplicationTest.java
│           └── controller/
│               └── HealthCheckControllerTest.java
└── pom.xml                                         # Maven configuration
```

## Local Development Setup

### 1. Clone the Repository

```bash
git clone https://github.com/geoffreygraddy/athenaeum.git
cd athenaeum/backend
```

### 2. Build the Application

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Verify the Application

Check the health endpoint:
```bash
curl http://localhost:8080/api/health
```

Expected response:
```json
{
  "status": "UP",
  "message": "Athenaeum Backend is running",
  "timestamp": "2025-11-01T16:08:33.833"
}
```

You can also check the actuator health endpoint:
```bash
curl http://localhost:8080/actuator/health
```

## Running Tests

Run all tests:
```bash
mvn test
```

Run tests with coverage:
```bash
mvn clean test jacoco:report
```

## API Endpoints

### Health Check
- **URL**: `/api/health`
- **Method**: `GET`
- **Description**: Check if the backend service is running
- **Response**:
  ```json
  {
    "status": "UP",
    "message": "Athenaeum Backend is running",
    "timestamp": "2025-11-01T16:08:33.833"
  }
  ```

### Actuator Health
- **URL**: `/actuator/health`
- **Method**: `GET`
- **Description**: Spring Boot actuator health check
- **Response**:
  ```json
  {
    "status": "UP"
  }
  ```

## Configuration

The application can be configured through `src/main/resources/application.yml`:

- **Server Port**: Default is 8080
- **Session Timeout**: Default is 30 minutes
- **Logging Level**: INFO for root, DEBUG for application packages

### Environment-Specific Configuration

Create additional configuration files for different environments:
- `application-dev.yml` for development
- `application-prod.yml` for production

Activate a profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Security

The application uses Spring Security with a basic configuration:
- All `/api/**` endpoints are currently accessible without authentication (for initial development)
- The `/actuator/health` endpoint is publicly accessible
- Other endpoints require authentication

**Note**: Before deploying to production, properly configure authentication and authorization mechanisms.

## Development Guidelines

### Hot Reload

Spring Boot DevTools is included for automatic application restart during development. Simply make changes to your code and save - the application will automatically restart.

### Adding New Endpoints

1. Create a new controller in `src/main/java/com/athenaeum/backend/controller/`
2. Use `@RestController` and `@RequestMapping` annotations
3. Create corresponding tests in `src/test/java/com/athenaeum/backend/controller/`

Example:
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        // Implementation
    }
}
```

### Database Integration

To add database support:
1. Add JPA/JDBC dependencies to `pom.xml`
2. Configure datasource in `application.yml`
3. Create entity classes and repositories
4. Use Spring Data JPA for data access

## Building for Production

Create a production-ready JAR:
```bash
mvn clean package -DskipTests
```

The JAR file will be in `target/backend-0.0.1-SNAPSHOT.jar`

Run the JAR:
```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## Docker Support

A Dockerfile can be added for containerized deployment:

```dockerfile
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t athenaeum-backend .
docker run -p 8080:8080 athenaeum-backend
```

## Troubleshooting

### Port Already in Use
If port 8080 is already in use, change it in `application.yml`:
```yaml
server:
  port: 8081
```

### Build Failures
- Ensure Java 17 is installed: `java -version`
- Ensure Maven is installed: `mvn -version`
- Clear Maven cache: `mvn clean`

### Tests Failing
- Check logs in the console output
- Ensure all required dependencies are downloaded
- Run `mvn clean test` to rebuild and test

## Next Steps

- [ ] Add database integration (PostgreSQL, MySQL, etc.)
- [ ] Implement user authentication and authorization
- [ ] Add API documentation with Swagger/OpenAPI
- [ ] Implement session management with Redis or database
- [ ] Add CORS configuration for frontend integration
- [ ] Implement logging and monitoring
- [ ] Add more comprehensive tests
- [ ] Configure CI/CD pipeline

## Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [Spring Session Documentation](https://spring.io/projects/spring-session)
- [Maven Documentation](https://maven.apache.org/guides/)

## License

[Add your license here]
