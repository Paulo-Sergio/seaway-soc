import { CUSTOM_ELEMENTS_SCHEMA, LOCALE_ID, NgModule } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ReactiveFormsModule } from '@angular/forms';
import localePt from '@angular/common/locales/pt';
import { VitrineComponent } from './vitrine.component';
import { TableModule } from 'primeng/table';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { DropdownModule } from 'primeng/dropdown';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { SplitterModule } from 'primeng/splitter';
import { WindowService } from './window.service';
import { ImageModule } from 'primeng/image';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { SocService } from './soc.service';

registerLocaleData(localePt);

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    ReactiveFormsModule,
    TableModule,
    DropdownModule,
    AutoCompleteModule,
    ButtonModule,
    DividerModule,
    ToastModule,
    DialogModule,
    SplitterModule,
    ImageModule,
    ProgressSpinnerModule
  ],
  declarations: [
    VitrineComponent
  ],
  exports: [],
  providers: [
    {
      provide: LOCALE_ID,
      useValue: "pt-BR"
    },
    MessageService,
    ConfirmationService,
    SocService,
    WindowService
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class VitrineModule { }
