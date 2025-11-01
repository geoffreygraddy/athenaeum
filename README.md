# Athenaeum

A modern full-stack web application built with Angular frontend and Spring Boot backend.

## Project Structure

```
athenaeum/
├── frontend/           # Angular application
│   ├── src/           # Source code
│   ├── public/        # Static assets
│   └── dist/          # Production build output
├── backend/            # Spring Boot API
│   ├── src/           # Java source code
│   ├── pom.xml        # Maven configuration
│   └── README.md      # Backend documentation
├── nginx.conf         # nginx configuration
├── Dockerfile         # Docker configuration for deployment
└── DEPLOYMENT.md      # Deployment guide
```

## Quick Start

### Frontend Development

```bash
cd frontend
npm install
npm start
```

Visit `http://localhost:4200` to view the application.

### Backend Development

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Visit `http://localhost:8080/api/health` to verify the backend is running.

### Production Build

```bash
cd frontend
npm run build:prod
```

### Deployment

See [DEPLOYMENT.md](DEPLOYMENT.md) for detailed deployment instructions using Docker or nginx.

## Features

### Frontend
- Angular 20.x with TypeScript
- Routing configured for single-page application
- Production-ready build configuration
- nginx deployment setup with:
  - Gzip compression
  - Static asset caching
  - Security headers
  - Client-side routing support
- Docker support for containerized deployment

### Backend
- Spring Boot 3.2.0 with Java 17
- REST API endpoints
- Spring Security for authentication/authorization
- Spring Session for session management
- Health check endpoints via Spring Boot Actuator
- Maven build system
- Hot reload with Spring Boot DevTools

## Documentation

- [Deployment Guide](DEPLOYMENT.md) - Instructions for deploying the application
- [Frontend README](frontend/README.md) - Angular-specific documentation
- [Backend README](backend/README.md) - Spring Boot backend documentation

## Requirements

### Frontend
- Node.js 20.x or higher
- npm 10.x or higher
- nginx (for local deployment) or Docker (for containerized deployment)

### Backend
- Java 17 or higher
- Maven 3.6 or higher

## License

[Add your license here]

