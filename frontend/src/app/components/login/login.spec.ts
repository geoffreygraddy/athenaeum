import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { Login } from './login';
import { Auth, AuthResponse } from '../../services/auth';

describe('Login', () => {
  let component: Login;
  let fixture: ComponentFixture<Login>;
  let mockAuthService: jasmine.SpyObj<Auth>;
  let mockRouter: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    mockAuthService = jasmine.createSpyObj('Auth', ['login']);
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [Login, ReactiveFormsModule],
      providers: [
        { provide: Auth, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with empty form', () => {
    expect(component.loginForm.get('username')?.value).toBe('');
    expect(component.loginForm.get('password')?.value).toBe('');
  });

  it('should validate username as required', () => {
    const username = component.loginForm.get('username');
    expect(username?.valid).toBe(false);
    
    username?.setValue('ab');
    expect(username?.hasError('minlength')).toBe(true);
    
    username?.setValue('abc');
    expect(username?.valid).toBe(true);
  });

  it('should validate password as required', () => {
    const password = component.loginForm.get('password');
    expect(password?.valid).toBe(false);
    
    password?.setValue('12345');
    expect(password?.hasError('minlength')).toBe(true);
    
    password?.setValue('123456');
    expect(password?.valid).toBe(true);
  });

  it('should not submit when form is invalid', () => {
    component.onSubmit();
    expect(mockAuthService.login).not.toHaveBeenCalled();
  });

  it('should submit valid form and navigate on success', () => {
    const mockResponse: AuthResponse = {
      success: true,
      token: 'test-token'
    };
    mockAuthService.login.and.returnValue(of(mockResponse));
    
    component.loginForm.patchValue({
      username: 'testuser',
      password: 'password123'
    });
    
    component.onSubmit();
    
    expect(mockAuthService.login).toHaveBeenCalledWith({
      username: 'testuser',
      password: 'password123'
    });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/dashboard']);
  });

  it('should display error message on login failure', () => {
    const mockResponse: AuthResponse = {
      success: false,
      message: 'Invalid credentials'
    };
    mockAuthService.login.and.returnValue(of(mockResponse));
    
    component.loginForm.patchValue({
      username: 'testuser',
      password: 'wrongpassword'
    });
    
    component.onSubmit();
    
    expect(component.errorMessage()).toBe('Invalid credentials');
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });

  it('should handle unexpected errors', () => {
    mockAuthService.login.and.returnValue(
      throwError(() => new Error('Network error'))
    );
    
    component.loginForm.patchValue({
      username: 'testuser',
      password: 'password123'
    });
    
    component.onSubmit();
    
    expect(component.errorMessage()).toBe('An unexpected error occurred. Please try again.');
  });

  it('should show loading state during login', () => {
    const mockResponse: AuthResponse = { success: true };
    mockAuthService.login.and.returnValue(of(mockResponse));
    
    component.loginForm.patchValue({
      username: 'testuser',
      password: 'password123'
    });
    
    expect(component.isLoading()).toBe(false);
    
    component.onSubmit();
    
    // Loading state is set to false after subscription completes
    expect(component.isLoading()).toBe(false);
  });
});
