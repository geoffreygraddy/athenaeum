import { TestBed } from '@angular/core/testing';
import { Router, UrlTree } from '@angular/router';
import { of, isObservable, firstValueFrom } from 'rxjs';
import { authGuard } from './auth.guard';
import { Auth } from '../services/auth';

describe('authGuard', () => {
  let mockAuthService: jasmine.SpyObj<Auth>;
  let mockRouter: jasmine.SpyObj<Router>;

  beforeEach(() => {
    mockAuthService = jasmine.createSpyObj('Auth', ['checkAuthStatus']);
    mockRouter = jasmine.createSpyObj('Router', ['createUrlTree']);

    TestBed.configureTestingModule({
      providers: [
        { provide: Auth, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ]
    });
  });

  it('should allow access when user is authenticated', async () => {
    mockAuthService.checkAuthStatus.and.returnValue(of(true));

    const result = TestBed.runInInjectionContext(() => authGuard({} as any, {} as any));

    if (isObservable(result)) {
      const value = await firstValueFrom(result);
      expect(value).toBe(true);
    } else {
      expect(result).toBe(true);
    }
    expect(mockAuthService.checkAuthStatus).toHaveBeenCalled();
  });

  it('should redirect to login when user is not authenticated', async () => {
    const mockUrlTree = {} as UrlTree;
    mockAuthService.checkAuthStatus.and.returnValue(of(false));
    mockRouter.createUrlTree.and.returnValue(mockUrlTree);

    const result = TestBed.runInInjectionContext(() => authGuard({} as any, {} as any));

    if (isObservable(result)) {
      const value = await firstValueFrom(result);
      expect(value).toBe(mockUrlTree);
    } else {
      expect(result).toBe(mockUrlTree);
    }
    expect(mockAuthService.checkAuthStatus).toHaveBeenCalled();
    expect(mockRouter.createUrlTree).toHaveBeenCalledWith(['/login']);
  });
});
