import { Component, OnInit } from '@angular/core';
import { BreadcrumbService } from '../../app.breadcrumb.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { Previsao } from '../models/previsao.model';
import { UtilsSeaway } from '../../utilities/utils-seaway';
import { debounceTime, distinctUntilChanged, tap, map } from 'rxjs/operators';
import { WindowService } from './window.service';
import { SocService } from './soc.service';
import { Cor } from '../models/cor.model';
import { baseUrlImgs } from 'src/environments/environment';
import { Audit } from '../models/audit.model';
import { AuditSummary } from '../models/audit-summary.model';
import { Parametro } from '../models/parametro.model';

@Component({
  templateUrl: './vitrine.component.html',
  styleUrls: ['./vitrine.component.scss']
})
export class VitrineComponent implements OnInit {

  public form: FormGroup

  public previsoes: Previsao[]
  public totalRecords: number
  public loading: boolean = true
  public previsaoSelecionado: Previsao

  public cores: Cor[]

  public parametro: Parametro

  public grupos: string[]
  public gruposMaisVendidos: object[]
  public diplayModalGruposMaisVendidos: boolean = false

  public audits: Audit[]
  public auditSummary: AuditSummary
  public diplayModalAudits: boolean = false

  public windowHeight: number = 0
  public scrollHeight: string = '400px'

  public imageUrl: string
  public loadingBtnImprimir: boolean = false

  constructor(
    private breadcrumbService: BreadcrumbService,
    private socService: SocService,
    private fb: FormBuilder,
    private messageService: MessageService,
    private windowService: WindowService
  ) {
    this.breadcrumbService.setItems([
      { label: 'Pages' },
      { label: 'SOC Web' }
    ]);
  }

  ngOnInit(): void {
    this.findAllGrupos()
    this.findParametros()

    this.form = this.fb.group({
      descricaoGrupo: [null],
    })

    this.updateWindowHeight();
    window.addEventListener('resize', () => this.updateWindowHeight());

    this.form.valueChanges
      .pipe(
        //map(value => value.trim()),
        //filter(value => value.length > 1),
        debounceTime(300),
        distinctUntilChanged(),
        tap(value => console.log(value)),
      ).subscribe({
        next: () => {
          this.getAllPrevisoes()
        },
        error: (err) => {
          console.log(err)
        }
      })
  }

  public getAllPrevisoes(): void {
    this.loading = true
    this.socService.findByDescricaoGrupo(this.form.value)
      .subscribe({
        next: (res: Previsao[]) => {
          this.previsoes = res
          console.log(this.previsoes)
          this.loading = false

          // Seleciona a primeira linha automaticamente se houver dados
          if (this.previsoes && this.previsoes.length > 0) {
            this.previsaoSelecionado = this.previsoes[0];
            // Opcional: disparar o evento de seleção manualmente
            this.onRowSelect({ data: this.previsoes[0] });
          }
        },
        error: (err) => {
          console.log(err)
          this.loading = false
          this.showNotificationToast('error', 'Conexão Falhou!')
        },
      });
  }

  public typeRadioButton(referencia: string): void {
    this.socService.findCoresByReferencia(referencia)
      .subscribe({
        next: (res: Cor[]) => {
          this.cores = res
          console.log(this.cores)
          this.imageUrl = `${baseUrlImgs}/${referencia}F.JPG`
        },
        error: (err) => {
          console.log(err)
          this.showNotificationToast('error', 'Conexão Falhou!')
        },
      });
  }

  public onRowSelect(event: any): void {
    console.log("onRowSelect: " + event?.data?.referencia)
    this.previsaoSelecionado = event?.data
    this.typeRadioButton(event?.data?.referencia)
  }

  public onRowUnselect(event: any): void {
    console.log("onRowUnselect:" + event?.data?.referencia)
  }

  public showDialogGruposMaisVendidos() {
    this.diplayModalGruposMaisVendidos = true;
    this.findGruposMaisVendidos()
  }

  public showDialogAudits() {
    this.diplayModalAudits = true;
    this.findAuditsByReferencia()
    this.findAuditSummaryByReferencia()
  }

  private findAllGrupos(): void {
    this.socService.findAllGrupos()
      .subscribe({
        next: (res: string[]) => {
          this.grupos = res
          console.log(this.grupos)
          this.form.patchValue({ descricaoGrupo: this.grupos[0] });
        },
        error: (err) => {
          console.log(err)
          this.showNotificationToast('error', 'Conexão Falhou!')
        },
      });
  }

  private findParametros(): void {
    this.socService.findParametros()
      .subscribe({
        next: (res: Parametro) => {
          this.parametro = res
          console.log(this.parametro)
        },
        error: (err) => {
          console.log(err)
          this.showNotificationToast('error', 'Conexão Falhou!')
        },
      });
  }

  private findGruposMaisVendidos(): void {
    this.socService.findGruposMaisVendidos()
      .subscribe({
        next: (res: object[]) => {
          this.gruposMaisVendidos = res
          console.log(this.gruposMaisVendidos)
        },
        error: (err) => {
          console.log(err)
          this.showNotificationToast('error', 'Conexão Falhou!')
        },
      });
  }

  private findAuditsByReferencia(): void {
    this.socService.findAuditsByReferencia(this.previsaoSelecionado?.referencia)
      .subscribe({
        next: (res: Audit[]) => {
          this.audits = res
          console.log(this.audits)
        },
        error: (err) => {
          console.log(err)
          this.showNotificationToast('error', 'Conexão Falhou!')
        },
      });
  }

  private findAuditSummaryByReferencia(): void {
    this.socService.findAuditSummaryByReferencia(this.previsaoSelecionado?.referencia)
      .subscribe({
        next: (res: AuditSummary) => {
          this.auditSummary = res
          console.log(this.auditSummary)
        },
        error: (err) => {
          console.log(err)
          this.showNotificationToast('error', 'Conexão Falhou!')
        },
      });
  }

  public exportReport() {
    this.loadingBtnImprimir = true
    this.socService.exportReport(this.form.get('colecao').value)
      .subscribe({
        next: (res: any) => {
          console.log(res)
          this.loadingBtnImprimir = false
          let blob: Blob = res.body as Blob
          let url = window.URL.createObjectURL(blob)
          window.open(url) //apenas abrir no navegador

          /* Fazer download
          let a = document.createElement('a')
          a.download = 'Produto Vitrine Relatório'
          a.href = url
          a.click()
          */

          this.showNotificationToast('info', 'Relatório gerado com sucesso!')
        },
        error: (err) => {
          console.log(err)
          this.loadingBtnImprimir = false
          this.showNotificationToast('error', 'Error exporting report!')
        }
      })
  }

  private updateWindowHeight(): void {
    this.windowHeight = this.windowService.getWindowHeight();
    this.scrollHeight = (this.windowHeight - 375).toString() + 'px'
  }

  private showNotificationToast(severity: string, msg: string) {
    this.messageService.add({ severity: severity, summary: `${UtilsSeaway.capitalize(severity)} Messagem`, detail: msg });
  }
}
