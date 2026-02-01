import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from 'rxjs';
import { baseUrl } from 'src/environments/environment';
import { Cor } from "../models/cor.model";
import { Previsao } from "../models/previsao.model";
import { Audit } from "../models/audit.model";
import { AuditSummary } from "../models/audit-summary.model";
import { Parametro } from "../models/parametro.model";

@Injectable()
export class SocService {

  private applicationJson: string = 'application/json;charset=UTF-8'

  constructor(
    private http: HttpClient
  ) { }

  public findAllGrupos(): Observable<string[]> {
    return this.http.get<string[]>(`${baseUrl}/api/previsoes/grupos`,
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }

  public findByDescricaoGrupo(search: any): Observable<Previsao[]> {
    let params = new HttpParams()

    if (search.descricaoGrupo != null) {
      params = params.set('descricaoGrupo', search.descricaoGrupo)
    }

    return this.http.get<Previsao[]>(`${baseUrl}/api/previsoes/byDescricaoGrupo`,
      {
        params,
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }

  public findCoresByReferencia(referencia: string): Observable<Cor[]> {
    return this.http.get<Cor[]>(`${baseUrl}/api/cores01/${referencia}`,
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }

  public findGruposMaisVendidos(): Observable<object[]> {
    return this.http.get<object[]>(`${baseUrl}/api/previsoes/gruposMaisVendidos`,
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }

  public findAuditsByReferencia(referencia: string): Observable<Audit[]> {
    return this.http.get<Audit[]>(`${baseUrl}/api/audits/${referencia}`,
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }  

  public findAuditSummaryByReferencia(referencia: string): Observable<AuditSummary> {
    return this.http.get<AuditSummary>(`${baseUrl}/api/audits/summary/${referencia}`,
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }  

  public findParametros(): Observable<Parametro> {
    return this.http.get<Parametro>(`${baseUrl}/api/parametros`,
      {
        headers: new HttpHeaders({ contentType: this.applicationJson })
      }
    )
  }  

  public exportReport(codColecao: string): Observable<any> {
    let params = new HttpParams()
    return this.http.get<any>(`${baseUrl}/report/pdf/VitrineHorizontal/codColecao/${codColecao}`,
      {
        params: params,
        observe: 'response',
        responseType: 'blob' as 'json',
        headers: new HttpHeaders({ contentType: "application/pdf" })
      }
    )
  }
}