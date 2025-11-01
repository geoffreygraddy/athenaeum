# Athenaeum

A modern web application built with Angular and deployable via nginx.

## Project Structure

```
athenaeum/
├── frontend/           # Angular application
│   ├── src/           # Source code
│   ├── public/        # Static assets
│   └── dist/          # Production build output
├── nginx.conf         # nginx configuration
├── Dockerfile         # Docker configuration for deployment
└── DEPLOYMENT.md      # Deployment guide
```

## Quick Start

### Development

```bash
cd frontend
npm install
npm start
```

Visit `http://localhost:4200` to view the application.

### Production Build

```bash
cd frontend
npm run build:prod
```

### Deployment

See [DEPLOYMENT.md](DEPLOYMENT.md) for detailed deployment instructions using Docker or nginx.

## Features

- Angular 20.x with TypeScript
- Routing configured for single-page application
- Production-ready build configuration
- nginx deployment setup with:
  - Gzip compression
  - Static asset caching
  - Security headers
  - Client-side routing support
- Docker support for containerized deployment

## Documentation

- [Deployment Guide](DEPLOYMENT.md) - Instructions for deploying the application
- [Frontend README](frontend/README.md) - Angular-specific documentation

## Requirements

- Node.js 20.x or higher
- npm 10.x or higher
- nginx (for local deployment) or Docker (for containerized deployment)

## License

[Add your license here]

