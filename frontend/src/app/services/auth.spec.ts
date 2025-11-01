import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { Auth, LoginCredentials, AuthResponse } from './auth';

describe('Auth', () => {
  let service: Auth;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(Auth);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login successfully with valid credentials', (done) => {
    const credentials: LoginCredentials = {
      username: 'testuser',
      password: 'password123'
    };
    const mockResponse: AuthResponse = {
      success: true,
      message: 'Login successful',
      token: 'test-token-123'
    };

    service.login(credentials).subscribe((response) => {
      expect(response.success).toBe(true);
      expect(response.token).toBe('test-token-123');
      expect(service.isAuthenticated()).toBe(true);
      expect(service.currentUser()).toBe('testuser');
      expect(localStorage.getItem('auth_token')).toBe('test-token-123');
      done();
    });

    const req = httpMock.expectOne('/api/auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(credentials);
    req.flush(mockResponse);
  });

  it('should handle login failure', (done) => {
    const credentials: LoginCredentials = {
      username: 'testuser',
      password: 'wrongpassword'
    };

    service.login(credentials).subscribe((response) => {
      expect(response.success).toBe(false);
      expect(response.message).toBeTruthy();
      expect(service.isAuthenticated()).toBe(false);
      expect(service.currentUser()).toBeNull();
      done();
    });

    const req = httpMock.expectOne('/api/auth/login');
    req.flush(
      { message: 'Invalid credentials' },
      { status: 401, statusText: 'Unauthorized' }
    );
  });

  it('should logout user', () => {
    localStorage.setItem('auth_token', 'test-token');
    service.logout();
    
    expect(service.isAuthenticated()).toBe(false);
    expect(service.currentUser()).toBeNull();
    expect(localStorage.getItem('auth_token')).toBeNull();
  });

  it('should check auth status based on token', () => {
    expect(service.checkAuthStatus()).toBe(false);
    
    localStorage.setItem('auth_token', 'test-token');
    expect(service.checkAuthStatus()).toBe(true);
    expect(service.isAuthenticated()).toBe(true);
  });
});
