import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, catchError, of } from 'rxjs';

export interface LoginCredentials {
  username: string;
  password: string;
}

export interface AuthResponse {
  success: boolean;
  message?: string;
  token?: string;
}

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/auth';
  
  private readonly isAuthenticatedSignal = signal<boolean>(false);
  private readonly currentUserSignal = signal<string | null>(null);
  
  readonly isAuthenticated = this.isAuthenticatedSignal.asReadonly();
  readonly currentUser = this.currentUserSignal.asReadonly();

  login(credentials: LoginCredentials): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((response) => {
        if (response.success) {
          this.isAuthenticatedSignal.set(true);
          this.currentUserSignal.set(credentials.username);
          if (response.token) {
            localStorage.setItem('auth_token', response.token);
          }
        }
      }),
      catchError((error) => {
        console.error('Login error:', error);
        return of({
          success: false,
          message: error.error?.message || 'Login failed. Please try again.',
        });
      })
    );
  }

  logout(): void {
    this.isAuthenticatedSignal.set(false);
    this.currentUserSignal.set(null);
    localStorage.removeItem('auth_token');
  }

  checkAuthStatus(): boolean {
    const token = localStorage.getItem('auth_token');
    const isAuth = !!token;
    this.isAuthenticatedSignal.set(isAuth);
    return isAuth;
  }
}
