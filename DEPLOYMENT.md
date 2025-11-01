# Deployment Guide

This guide provides instructions for deploying the Athenaeum Angular application using nginx.

## Prerequisites

- Node.js 20.x or higher
- npm 10.x or higher
- nginx (for local deployment) or Docker (for containerized deployment)

## Development

### Running the Development Server

```bash
cd frontend
npm install
npm start
```

The application will be available at `http://localhost:4200`.

### Running Tests

```bash
cd frontend
npm test
```

## Production Build

### Building the Application

To create a production build:

```bash
cd frontend
npm install
npm run build:prod
```

This will generate optimized static files in the `frontend/dist/frontend/browser` directory.

## Deployment Options

### Option 1: Deploy with Docker (Recommended)

The easiest way to deploy the application is using Docker:

1. **Build the Docker image:**
   ```bash
   docker build -t athenaeum-frontend .
   ```

2. **Run the container:**
   ```bash
   docker run -d -p 80:80 --name athenaeum athenaeum-frontend
   ```

3. **Access the application:**
   Open your browser and navigate to `http://localhost`

4. **Stop the container:**
   ```bash
   docker stop athenaeum
   docker rm athenaeum
   ```

### Option 2: Deploy with nginx (Manual)

For manual deployment on a server with nginx:

1. **Build the application:**
   ```bash
   cd frontend
   npm install
   npm run build:prod
   ```

2. **Install nginx** (if not already installed):
   
   **Ubuntu/Debian:**
   ```bash
   sudo apt-get update
   sudo apt-get install nginx
   ```
   
   **CentOS/RHEL:**
   ```bash
   sudo yum install nginx
   ```
   
   **macOS:**
   ```bash
   brew install nginx
   ```

3. **Copy built files to nginx directory:**
   ```bash
   sudo cp -r frontend/dist/frontend/browser/* /usr/share/nginx/html/
   ```

4. **Copy nginx configuration:**
   ```bash
   sudo cp nginx.conf /etc/nginx/conf.d/athenaeum.conf
   ```
   
   Or update the default nginx configuration:
   ```bash
   sudo cp nginx.conf /etc/nginx/sites-available/default
   ```

5. **Test nginx configuration:**
   ```bash
   sudo nginx -t
   ```

6. **Restart nginx:**
   
   **Ubuntu/Debian:**
   ```bash
   sudo systemctl restart nginx
   ```
   
   **CentOS/RHEL:**
   ```bash
   sudo systemctl restart nginx
   ```
   
   **macOS:**
   ```bash
   sudo nginx -s reload
   ```

7. **Access the application:**
   Open your browser and navigate to `http://localhost` or your server's IP address

## nginx Configuration

The provided `nginx.conf` file includes:

- **Routing**: All routes are redirected to `index.html` for Angular's client-side routing
- **Compression**: gzip compression is enabled for optimal performance
- **Caching**: Static assets are cached for 1 year
- **Security Headers**: X-Frame-Options, X-Content-Type-Options, and X-XSS-Protection headers are set

### Customizing the Configuration

You can modify `nginx.conf` to:
- Change the server port (default: 80)
- Update the server name for your domain
- Adjust caching policies
- Add SSL/TLS configuration

Example SSL configuration:
```nginx
server {
    listen 443 ssl http2;
    server_name yourdomain.com;
    
    ssl_certificate /path/to/certificate.crt;
    ssl_certificate_key /path/to/private.key;
    
    # ... rest of configuration
}
```

## Production Checklist

Before deploying to production, ensure:

- [ ] All tests pass (`npm test`)
- [ ] Production build succeeds (`npm run build:prod`)
- [ ] Environment variables are configured correctly
- [ ] nginx configuration is tested (`nginx -t`)
- [ ] Security headers are in place
- [ ] SSL/TLS is configured (for production domains)
- [ ] Firewall rules allow traffic on port 80/443
- [ ] Application is accessible from the expected URL

## Troubleshooting

### Build Fails

If the build fails, check:
- Node.js and npm versions meet the requirements
- All dependencies are installed (`npm install`)
- Check the error message for specific issues

### nginx Fails to Start

If nginx fails to start:
- Check nginx configuration syntax: `sudo nginx -t`
- Verify file permissions on the html directory
- Check if port 80 is already in use: `sudo lsof -i :80`
- Review nginx error logs: `sudo tail -f /var/log/nginx/error.log`

### Application Not Loading

If the application doesn't load:
- Check browser console for errors
- Verify nginx is serving the correct directory
- Ensure `index.html` exists in the nginx html directory
- Check nginx access logs: `sudo tail -f /var/log/nginx/access.log`

## Additional Resources

- [Angular Documentation](https://angular.io/docs)
- [nginx Documentation](https://nginx.org/en/docs/)
- [Docker Documentation](https://docs.docker.com/)
