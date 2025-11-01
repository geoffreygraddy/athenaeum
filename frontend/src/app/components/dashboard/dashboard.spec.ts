import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { signal } from '@angular/core';
import { of } from 'rxjs';
import { Dashboard } from './dashboard';
import { Auth } from '../../services/auth';

describe('Dashboard', () => {
  let component: Dashboard;
  let fixture: ComponentFixture<Dashboard>;
  let mockAuthService: jasmine.SpyObj<Auth>;
  let mockRouter: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    mockAuthService = jasmine.createSpyObj('Auth', ['logout']);
    Object.defineProperty(mockAuthService, 'currentUser', {
      get: () => signal('testuser')
    });
    Object.defineProperty(mockAuthService, 'isAuthenticated', {
      get: () => signal(true)
    });
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [Dashboard],
      providers: [
        { provide: Auth, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Dashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display current user', () => {
    expect(component.currentUser()).toBe('testuser');
  });

  it('should display authentication status', () => {
    expect(component.isAuthenticated()).toBe(true);
  });

  it('should logout and navigate to login page', () => {
    mockAuthService.logout.and.returnValue(of({ success: true, message: 'Logout successful', username: null }));
    
    component.logout();
    
    expect(mockAuthService.logout).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should render dashboard header', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const header = compiled.querySelector('h1');
    
    expect(header?.textContent).toBe('Dashboard');
  });

  it('should render protected content message', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const content = compiled.textContent;
    
    expect(content).toContain('Protected Content');
    expect(content).toContain('only authenticated users can access');
  });

  it('should render logout button', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const button = compiled.querySelector('.logout-btn');
    
    expect(button).toBeTruthy();
    expect(button?.textContent).toBe('Logout');
  });
});
