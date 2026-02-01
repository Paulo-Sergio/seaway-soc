import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { baseUrl } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private applicationJson: string = 'application/json;charset=UTF-8'

  constructor(
    private http: HttpClient
  ) { }

  public login(username: string, password: string): Observable<any> {
    return this.http.post(`${baseUrl}/auth`, { username, password },
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }

  public logout() {
    localStorage.setItem('userInfoVitrine', null)
  }

  public getToken() {
    let user = JSON.parse(localStorage.getItem('userInfoVitrine') || '{}')
    return user?.token
  }

  public getUsername() {
    let user = JSON.parse(localStorage.getItem('userInfoVitrine') || '{}')
    return user?.username
  }

  public getFullname() {
    let user = JSON.parse(localStorage.getItem('userInfoVitrine') || '{}')
    return user?.fullname
  }

  public getRoles(): any[] {
    let user = JSON.parse(localStorage.getItem('userInfoVitrine') || '[]')
    return user?.roles
  }

  public getPhoto() {
    let user = JSON.parse(localStorage.getItem('userInfoVitrine') || '{}')
    return user?.photo
  }

  public getTypePhoto() {
    let user = JSON.parse(localStorage.getItem('userInfoVitrine') || '{}')
    return user?.typePhoto
  }
}
