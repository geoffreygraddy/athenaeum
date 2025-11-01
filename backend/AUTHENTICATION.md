# Authentication Flow Documentation

## Overview

The Athenaeum backend implements session-based authentication using Spring Security and Spring Session with JDBC storage. This document provides integration guidance for the Angular frontend.

## Authentication Endpoints

### Login
**POST** `/api/auth/login`

Authenticates a user and creates a session.

**Request:**
```json
{
  "username": "admin",
  "password": "changeme"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "username": "admin"
}
```

**Response (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Invalid username or password",
  "username": null
}
```

**Cookies Set:**
- `JSESSIONID`: HttpOnly session cookie (SameSite=Lax)
- `XSRF-TOKEN`: CSRF token cookie for subsequent requests

### Logout
**POST** `/api/auth/logout`

Invalidates the current session.

**Headers Required:**
- `X-XSRF-TOKEN`: CSRF token from cookie

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Logout successful",
  "username": null
}
```

### Get User Info
**GET** `/api/auth/user`

Returns information about the currently authenticated user.

**Response (authenticated):**
```json
{
  "username": "admin",
  "authenticated": true
}
```

**Response (not authenticated):**
```json
{
  "username": null,
  "authenticated": false
}
```

## Protected Endpoints

### Example Protected Resource
**GET** `/api/protected/resource`

Requires authentication. Returns 403 Forbidden if not authenticated.

**Response (200 OK):**
```json
{
  "message": "This is a protected resource",
  "user": "admin",
  "authorities": [{"authority": "ROLE_USER"}]
}
```

## Frontend Integration Guide

### 1. Configure HTTP Client

Enable credentials to include cookies in requests:

```typescript
// In Angular HttpClient
this.http.post('/api/auth/login', credentials, { withCredentials: true })
```

Or configure globally in `app.config.ts`:

```typescript
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(
      withInterceptors([
        (req, next) => {
          // Clone request with credentials
          const clonedReq = req.clone({ withCredentials: true });
          return next(clonedReq);
        }
      ])
    )
  ]
};
```

### 2. CSRF Protection

The backend uses cookie-based CSRF protection. The CSRF token is sent as a cookie and must be included in the request header for state-changing operations (POST, PUT, DELETE).

**Automatic Handling:**
```typescript
import { HttpClientXsrfModule } from '@angular/common/http';

// In app.config.ts or module
HttpClientXsrfModule.withOptions({
  cookieName: 'XSRF-TOKEN',
  headerName: 'X-XSRF-TOKEN'
})
```

Angular's HttpClient automatically reads the `XSRF-TOKEN` cookie and includes it in the `X-XSRF-TOKEN` header for non-GET requests.

### 3. Login Flow Example

```typescript
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = '/api/auth';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/login`,
      { username, password },
      { withCredentials: true }
    );
  }

  logout(): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/logout`,
      {},
      { withCredentials: true }
    );
  }

  getUserInfo(): Observable<any> {
    return this.http.get(
      `${this.apiUrl}/user`,
      { withCredentials: true }
    );
  }
}
```

### 4. Session Management

- Sessions are stored server-side in the database
- Session timeout: 30 minutes of inactivity
- Maximum 1 concurrent session per user
- `JSESSIONID` cookie is HttpOnly and cannot be accessed by JavaScript
- CSRF token cookie (`XSRF-TOKEN`) is accessible for including in request headers

### 5. Error Handling

```typescript
this.authService.login(username, password).subscribe({
  next: (response) => {
    if (response.success) {
      // Login successful, redirect to dashboard
      this.router.navigate(['/dashboard']);
    }
  },
  error: (error) => {
    if (error.status === 401) {
      // Invalid credentials
      this.errorMessage = 'Invalid username or password';
    } else if (error.status === 403) {
      // CSRF token missing or invalid
      this.errorMessage = 'Security validation failed. Please refresh and try again.';
    } else {
      // Other errors
      this.errorMessage = 'An error occurred. Please try again.';
    }
  }
});
```

### 6. Route Guards

Implement an auth guard to protect routes:

```typescript
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { map } from 'rxjs/operators';

export const authGuard = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.getUserInfo().pipe(
    map(user => {
      if (user.authenticated) {
        return true;
      }
      router.navigate(['/login']);
      return false;
    })
  );
};
```

## Security Considerations

### Production Configuration

1. **HTTPS**: Always use HTTPS in production
   - Update `SecurityConfig.cookieSerializer()` to set `setUseSecureCookie(true)`

2. **CORS**: Update allowed origins in `SecurityConfig.corsConfigurationSource()`
   ```java
   configuration.setAllowedOrigins(Arrays.asList(
     "https://yourdomain.com"
   ));
   ```

3. **User Credentials**: Use environment variables for default user
   ```bash
   export SPRING_SECURITY_USER_NAME=your_admin_username
   export SPRING_SECURITY_USER_PASSWORD=your_secure_password
   ```

4. **Session Store**: Consider using Redis for session storage in production for better scalability

### Testing Authentication

Use tools like Postman or curl to test the authentication flow:

```bash
# Login
curl -c cookies.txt -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"changeme"}'

# Access protected endpoint
curl -b cookies.txt http://localhost:8080/api/protected/resource

# Logout
curl -b cookies.txt -X POST http://localhost:8080/api/auth/logout \
  -H "X-XSRF-TOKEN: <token-from-cookie>"
```

## Default Credentials

**Development Only:**
- Username: `admin`
- Password: `changeme`

**Important:** Change these credentials in production using environment variables.
