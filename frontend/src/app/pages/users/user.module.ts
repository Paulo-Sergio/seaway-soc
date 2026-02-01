import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserComponent } from './user.component';
import { PrimengModule } from 'src/app/primeng.module';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { UserService } from './user.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { UserAccountComponent } from '../user-account/user-account.component';

@NgModule({
  declarations: [
    UserComponent,
    UserAccountComponent
  ],
  imports: [
    CommonModule,
    PrimengModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [UserService, ConfirmationService, MessageService],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UserModule { }
