import { Component, OnInit } from '@angular/core';
import { BreadcrumbService } from '../../app.breadcrumb.service';
import { ProductService } from 'src/app/demo/service/productservice';
import { Product } from 'src/app/demo/domain/product';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ConfirmationService, LazyLoadEvent, MessageService } from 'primeng/api';
import { debounceTime, distinctUntilChanged, switchMap, tap, map } from 'rxjs/operators';
import { baseUrlImgs } from 'src/environments/environment';
import { UtilsSeaway } from '../../utilities/utils-seaway';
import { HomeService } from './home.service';

@Component({
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  public form: FormGroup
  public submitLoading: boolean = false

  constructor(
    private breadcrumbService: BreadcrumbService,
    private fb: FormBuilder,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private homeService: HomeService
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      grupo: [null],
    })
  }

  confirmRefresh(event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Tem certeza que deseja atualizar os registros? <br> As marcações de SOC web anteriores serão perdidas!',
      header: 'Confirmação de atualização',
      icon: 'pi pi-exclamation-triangle',
      acceptIcon: 'pi pi-check mr-2',
      rejectIcon: 'pi pi-times mr-2',
      rejectButtonStyleClass: "p-button-text",
      accept: () => {
        this.submitLoading = true
        this.homeService.consumirTXTs()
          .subscribe({
            next: (res: any) => {
              console.log(res)
              this.submitLoading = false
              this.messageService.add({ severity: 'info', summary: 'Sucesso', detail: 'Atualização dos registro realizada com sucesso!' });
            },
            error: (err) => {
              console.log(err)
              this.submitLoading = false
              if (err?.error?.status == 404) {
                this.showNotificationToast('warn', err?.error?.msg)
              } else if (err?.error?.status == 400) {
                this.showNotificationToast('error', err?.error?.msg)
              } else {
                this.showNotificationToast('error', 'Falha ao atualizar os registros!!')
              }
            },
          });
      },
      reject: () => {
        this.messageService.add({ severity: 'warn', summary: 'Cancelado', detail: 'Solicitação de atualização cancelada!', life: 3000 });
      }
    });
  }

  private showNotificationToast(severity: string, msg: string) {
    let titulo = ''
    if (severity == 'warn') {
      titulo = 'Mensagem de Alerta'
    } else if (severity == 'info' || severity == 'success') {
      titulo = 'Mensagem de Sucesso'
    } else {
      titulo = 'Menssagem de Erro'
    }
    this.messageService.add({ severity: severity, summary: titulo, detail: msg });
  }
}
