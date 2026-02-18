// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { User } from '../models/user.model';
import { baseUrl } from 'src/environments/environment';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${baseUrl}/api`; // URL do seu backend
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasValidToken());

  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    // Verificar token expirado a cada hora
    setInterval(() => this.checkTokenExpiration(), 3600000);
  }

  login(user: User): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': 'Basic ' + btoa(user.username + ':' + user.password),
      'Content-Type': 'application/json'
    });

    return this.http.get<any>(`${this.apiUrl}/previsoes/grupos`, { headers }).pipe(
      tap(() => {
        // Armazena as credenciais no localStorage
        localStorage.setItem('auth_token_soc_seaway', btoa(user.username + ':' + user.password));

        // Define a expiração do token (24 horas a partir de agora)
        const expiryTime = Date.now() + (24 * 60 * 60 * 1000); // 24 horas em milissegundos
        localStorage.setItem('token_expiry_soc_seaway', expiryTime.toString());

        this.isAuthenticatedSubject.next(true);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('auth_token_soc_seaway');
    localStorage.removeItem('token_expiry_soc_seaway');
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/login']);
  }

  getAuthToken(): string | null {
    if (!this.hasValidToken()) {
      this.logout()
      return null;
    }
    return localStorage.getItem('auth_token_soc_seaway');
  }

  private hasValidToken(): boolean {
    const token = localStorage.getItem('auth_token_soc_seaway');
    if (!token) return false;

    const expiry = localStorage.getItem('token_expiry_soc_seaway');
    if (!expiry) return false;

    return parseInt(expiry) > Date.now();
  }

  private hasToken(): boolean {
    return !!localStorage.getItem('auth_token_soc_seaway');
  }

  private checkTokenExpiration(): void {
    if (this.isAuthenticatedSubject.value && !this.hasValidToken()) {
      console.log('Token expirado. Fazendo logout...');
      this.logout();
    }
  }
}