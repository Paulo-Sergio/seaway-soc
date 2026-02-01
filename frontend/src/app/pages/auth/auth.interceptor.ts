import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HTTP_INTERCEPTORS,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request);
    
    if (request.url.includes('login') || request.url.includes('new-user')) {
      return next.handle(request);
    }

    if (this.authService.getToken()) {
      request = request.clone({
        headers: request.headers.set('authorization', `Bearer ${this.authService.getToken()}`)
      })
    } else {
      if (!this.router.url?.includes('login')) {
        localStorage.setItem('urlRedirect', this.router.url)
      }
      this.router.navigate(['/login'])
      return next.handle(request)
    }

    return next.handle(request).pipe(tap(() => { },
      (err: any) => {
        if (err instanceof HttpErrorResponse) {
          if (err.status === 401) {
            this.authService.logout()
            this.router.navigate(['/login'])
            return
          }
          if (err.status === 403) {
            this.router.navigate(['/access'])
            return
          }
        }
      }))
  }
}


export const AuthInterceptorProvider = {
  provide: HTTP_INTERCEPTORS,
  useClass: AuthInterceptor,
  multi: true
}