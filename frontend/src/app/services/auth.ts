import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, catchError, of, map } from 'rxjs';

export interface LoginCredentials {
  username: string;
  password: string;
}

export interface AuthResponse {
  success: boolean;
  message?: string;
  username?: string | null;
}

export interface UserInfo {
  username: string | null;
  authenticated: boolean;
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
        if (response.success && response.username) {
          this.isAuthenticatedSignal.set(true);
          this.currentUserSignal.set(response.username);
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

  logout(): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/logout`, {}).pipe(
      tap(() => {
        this.isAuthenticatedSignal.set(false);
        this.currentUserSignal.set(null);
      }),
      catchError((error) => {
        console.error('Logout error:', error);
        // Clear local state even if server request fails
        this.isAuthenticatedSignal.set(false);
        this.currentUserSignal.set(null);
        return of({
          success: false,
          message: 'Logout failed, but local session cleared.',
        });
      })
    );
  }

  getUserInfo(): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.apiUrl}/user`).pipe(
      tap((userInfo) => {
        this.isAuthenticatedSignal.set(userInfo.authenticated);
        this.currentUserSignal.set(userInfo.username);
      }),
      catchError((error) => {
        console.error('Get user info error:', error);
        this.isAuthenticatedSignal.set(false);
        this.currentUserSignal.set(null);
        return of({ username: null, authenticated: false });
      })
    );
  }

  checkAuthStatus(): Observable<boolean> {
    return this.getUserInfo().pipe(
      map(userInfo => userInfo.authenticated)
    );
  }
}
