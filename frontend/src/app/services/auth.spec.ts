import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { Auth, LoginCredentials, AuthResponse, UserInfo } from './auth';

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
  });

  afterEach(() => {
    httpMock.verify();
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
      username: 'testuser'
    };
    const mockUserInfo: UserInfo = {
      username: 'testuser',
      authenticated: true
    };

    service.login(credentials).subscribe((response) => {
      expect(response.success).toBe(true);
      expect(response.username).toBe('testuser');
      expect(service.isAuthenticated()).toBe(true);
      expect(service.currentUser()).toBe('testuser');
      done();
    });

    const loginReq = httpMock.expectOne('/api/auth/login');
    expect(loginReq.request.method).toBe('POST');
    expect(loginReq.request.body).toEqual(credentials);
    loginReq.flush(mockResponse);

    // Expect getUserInfo to be called after successful login
    const userInfoReq = httpMock.expectOne('/api/auth/user');
    expect(userInfoReq.request.method).toBe('GET');
    userInfoReq.flush(mockUserInfo);
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

  it('should logout user', (done) => {
    const mockResponse: AuthResponse = {
      success: true,
      message: 'Logout successful',
      username: null
    };

    service.logout().subscribe((response) => {
      expect(response.success).toBe(true);
      expect(service.isAuthenticated()).toBe(false);
      expect(service.currentUser()).toBeNull();
      done();
    });

    const req = httpMock.expectOne('/api/auth/logout');
    expect(req.request.method).toBe('POST');
    req.flush(mockResponse);
  });

  it('should get user info when authenticated', (done) => {
    const mockUserInfo: UserInfo = {
      username: 'testuser',
      authenticated: true
    };

    service.getUserInfo().subscribe((userInfo) => {
      expect(userInfo.authenticated).toBe(true);
      expect(userInfo.username).toBe('testuser');
      expect(service.isAuthenticated()).toBe(true);
      expect(service.currentUser()).toBe('testuser');
      done();
    });

    const req = httpMock.expectOne('/api/auth/user');
    expect(req.request.method).toBe('GET');
    req.flush(mockUserInfo);
  });

  it('should get user info when not authenticated', (done) => {
    const mockUserInfo: UserInfo = {
      username: null,
      authenticated: false
    };

    service.getUserInfo().subscribe((userInfo) => {
      expect(userInfo.authenticated).toBe(false);
      expect(userInfo.username).toBeNull();
      expect(service.isAuthenticated()).toBe(false);
      expect(service.currentUser()).toBeNull();
      done();
    });

    const req = httpMock.expectOne('/api/auth/user');
    req.flush(mockUserInfo);
  });

  it('should check auth status', (done) => {
    const mockUserInfo: UserInfo = {
      username: 'testuser',
      authenticated: true
    };

    service.checkAuthStatus().subscribe((isAuthenticated) => {
      expect(isAuthenticated).toBe(true);
      done();
    });

    const req = httpMock.expectOne('/api/auth/user');
    req.flush(mockUserInfo);
  });
});
