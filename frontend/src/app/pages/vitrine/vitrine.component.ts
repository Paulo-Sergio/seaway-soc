import { Component, OnInit, ViewChild } from '@angular/core';
import { BreadcrumbService } from '../../app.breadcrumb.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { Previsao } from '../models/previsao.model';
import { UtilsSeaway } from '../../utilities/utils-seaway';
import { debounceTime, distinctUntilChanged, tap, map } from 'rxjs/operators';
import { WindowService } from './window.service';
import { SocService } from './soc.service';
import { Cor01 } from '../models/cor01.model';
import { baseUrlImgs } from 'src/environments/environment';
import { Audit } from '../models/audit.model';
import { AuditSummary } from '../models/audit-summary.model';
import { Parametro } from '../models/parametro.model';
import { OverlayPanel } from 'primeng/overlaypanel';
import { Cor03 } from '../models/cor03.model';

interface LojaInfo {
  codigoLoja: string;
  nomeLoja: string;
}

@Component({
  templateUrl: './vitrine.component.html',
  styleUrls: ['./vitrine.component.scss']
})
export class VitrineComponent implements OnInit {

  public form: FormGroup
  public remanejarValue: string // Propriedade para armazenar o valor do botão de rádio

  public previsoes: Previsao[]
  public totalRecords: number
  public loading: boolean = true
  public previsaoSelecionado: Previsao
  public expandedRows: { [key: string]: boolean } = {};

  public cores: Cor01[]
  public corSelecionada: Cor01

  public diplayModalEstoque: boolean = false
  public cores03: Cor03[]
  public lojasList: LojaInfo[] = [];
  public tamanhosList: string[] = ['34', '36', '38', '40', '42', '44', '46', '48', '50'];

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
      descricaoGrupo: [null]
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

  public changeRadioButton(referencia: string): void {
    this.socService.findCoresByReferencia(referencia)
      .subscribe({
        next: (res: Cor01[]) => {
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

  public onRemanejarChange(value: string) {
    console.log('Novo valor de remanejar:', value);
    this.socService.updatedRemanejar(this.previsaoSelecionado.referencia, value)
      .subscribe({
        next: (res: any) => {
          console.log(res)
          const index = this.previsoes.findIndex(previsao => previsao.referencia === this.previsaoSelecionado.referencia)
          if (index !== -1) {
            // Atualize o registro
            this.previsoes[index].remanejar = value === 'NA' ? null : value
          }
        },
        error: (err) => {
          console.log(err)
          this.showNotificationToast('error', 'Conexão Falhou!')
        },
      });
  }

  public onClasseChange(cor: Cor01, event: any) {
    console.log('Classe alterada para', cor.codigoCor, 'para', event.target.value);
    this.socService.updateClasse(this.previsaoSelecionado.referencia, cor.codigoCor, event.target.value)
      .subscribe({
        next: (res: any) => {
          console.log(res)
        },
        error: (err) => {
          console.log(err)
          this.showNotificationToast('error', 'Conexão Falhou!')
        },
      });
  }

  public updateSugestaoOc(prev: Previsao) {
    console.log('Valor S.OC:', prev.sugestaoOc);
    if (!prev.sugestaoOc || prev.sugestaoOc < 0) prev.sugestaoOc = 0
    this.socService.updateSugestaoOc(prev.referencia, prev.sugestaoOc)
      .subscribe({
        next: (res: any) => {
          console.log(res)
          const index = this.previsoes.findIndex(previsao => previsao.referencia === prev.referencia)
          if (index !== -1) {
            // Atualize o registro
            this.previsoes[index].dataSugestao = !prev.sugestaoOc ? null : new Date()
          }
        },
        error: (err) => {
          console.log(err)
          this.showNotificationToast('error', 'Conexão Falhou!')
        },
      });
  }

  public onPrioridadeChange(prev: Previsao, event: any) {
    console.log('Prioridade da ref', prev.referencia, 'alterada para', event.target.value);
    this.socService.updatePrioridade(prev.referencia, event.target.value)
      .subscribe({
        next: (res: any) => {
          console.log(res)
        },
        error: (err) => {
          console.log(err)
          this.showNotificationToast('error', 'Conexão Falhou!')
        },
      });
  }

  public showEstoquePorCor(cor: Cor01) {
    // Salva a cor selecionada
    this.corSelecionada = cor;

    // Chama o serviço para obter os dados
    this.socService.showEstoquePorCor(this.previsaoSelecionado.referencia, cor.codigoCor)
      .subscribe({
        next: (res: Cor03[]) => {
          this.cores03 = res
          console.log(this.cores03)
          this.processarDadosEstoque();
        },
        error: (err) => {
          console.log(err)
          this.showNotificationToast('error', 'Conexão Falhou!')
        },
      });
  }

  // Processa os dados recebidos da API
  private processarDadosEstoque() {
    // Extrair lojas únicas do array de cores03
    const lojasMap = new Map<string, LojaInfo>();

    this.cores03.forEach(item => {
      if (item.codigoLoja && !lojasMap.has(item.codigoLoja)) {
        lojasMap.set(item.codigoLoja, {
          codigoLoja: item.codigoLoja,
          nomeLoja: item.nomeLoja || ''
        });
      }
    });

    // Converter o Map para array
    this.lojasList = Array.from(lojasMap.values());
  }

  // Obtém o estoque para uma loja e tamanho específicos
  getEstoque(codigoLoja: string, tamanho: string): number {
    const item = this.cores03.find(
      item => item.codigoLoja === codigoLoja && item.tamanho === tamanho
    );
    return item?.estoque || 0;
  }

  // Calcula o total de estoque para uma loja
  getTotalEstoquePorLoja(codigoLoja: string): number {
    return this.cores03
      .filter(item => item.codigoLoja === codigoLoja)
      .reduce((total, item) => total + (item.estoque || 0), 0);
  }

  public exportOutputs() {
    this.loading = true
    this.socService.exportOutputs()
      .subscribe({
        next: (res: any) => {
          console.log(res)
          this.loading = false
          this.showNotificationToast('success', 'Arquivos exportados com sucesso!')
        },
        error: (err) => {
          console.log(err)
          this.loading = false
          this.showNotificationToast('error', 'Conexão Falhou!')
        },
      });
  }

  public onRowSelect(event: any): void {
    console.log("onRowSelect: " + event?.data?.referencia)
    this.gerenciarRadioButtonRemanejar()
    this.previsaoSelecionado = event?.data

    this.changeRadioButton(event?.data?.referencia)
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

  public showDialogEstoque(cor: Cor01) {
    this.diplayModalEstoque = true
    this.showEstoquePorCor(cor)
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
    this.socService.exportReport()
      .subscribe({
        next: (blob) => {
          this.loadingBtnImprimir = false;
          this.downloadFile(blob, `ordem-corte.pdf`);
        },
        error: (err) => {
          console.log(err)
          if (err.status == 400) {
            this.showNotificationToast('warn', 'Sem Ordem de Corte na data de Hoje!')
          } else {
            this.showNotificationToast('error', 'Erro genérico ao gerar relatório!')
          }
          this.loadingBtnImprimir = false
        }
      })
  }

  private downloadFile(blob: Blob, filename: string): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    link.click();
    window.URL.revokeObjectURL(url);
  }

  private gerenciarRadioButtonRemanejar(): void {
    if (this.previsaoSelecionado.remanejar === 'LA') {
      this.remanejarValue = 'LA'
    } else if (this.previsaoSelecionado.remanejar === 'L') {
      this.remanejarValue = 'L'
    } else {
      this.remanejarValue = 'NA'
    }
  }

  public selecionarConteudo(event: any) {
    event.target.select();
  }

  private updateWindowHeight(): void {
    this.windowHeight = this.windowService.getWindowHeight();
    this.scrollHeight = (this.windowHeight - 375).toString() + 'px'
  }

  private showNotificationToast(severity: string, msg: string) {
    this.messageService.add({ severity: severity, summary: `${UtilsSeaway.capitalize(severity)} Messagem`, detail: msg });
  }
}
