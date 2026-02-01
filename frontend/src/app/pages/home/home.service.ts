import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from 'rxjs';
import { baseUrl } from 'src/environments/environment';

@Injectable()
export class HomeService {

  private applicationJson: string = 'application/json;charset=UTF-8'

  constructor(
    private http: HttpClient
  ) { }

  public consumirTXTs(): Observable<any> {
    return this.http.post<any>(`${baseUrl}/batch/consumir-txts`,
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }
}