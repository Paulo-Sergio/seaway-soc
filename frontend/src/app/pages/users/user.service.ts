import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { baseUrl } from 'src/environments/environment';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private applicationJson: string = 'application/json;charset=UTF-8'

  constructor(
    private http: HttpClient
  ) { }

  public getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${baseUrl}/users`,
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }

  public getById(id: number): Observable<User> {
    return this.http.get<User>(`${baseUrl}/users/${id}`,
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }

  public getUserByUsername(username: string): Observable<User> {
    return this.http.get<User>(`${baseUrl}/users/findByUsername/${username}`,
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }

  public createUser(user: User): Observable<any> {
    return this.http.post<any>(`${baseUrl}/users/new-user`, user,
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }

  public lockOrUnlockUser(userId: number, lockOrUnlock: boolean): Observable<any> {
    return this.http.put<any>(`${baseUrl}/users/${userId}/lockOrUnlock/${lockOrUnlock}`,
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }

  public updateDepartament(userId: number, codDepartament: number): Observable<any> {
    return this.http.put<any>(`${baseUrl}/users/${userId}/updateDepartament/${codDepartament}`,
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }

  public resetPasswod(userId: number): Observable<any> {
    return this.http.put<any>(`${baseUrl}/users/${userId}/resetPassword`,
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }

  public updateInfoAccountUser(userId: number, password: string, photo: any): Observable<any> {
    let formData = new FormData();
    if (password != null) {
      formData.append('password', password);
    }
    if (photo != null) {
      formData.append('photo', photo);
    }
    return this.http.put<any>(`${baseUrl}/users/${userId}/update-info-account-user`, formData,
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }
}
