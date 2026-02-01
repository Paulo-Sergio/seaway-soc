import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AuthInterceptorProvider } from './auth.interceptor';
import { ButtonModule } from 'primeng/button';
//import { NewUserComponent } from './new-user/new-user.component';

@NgModule({
  declarations: [
    //NewUserComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HttpClientModule,
    
    ButtonModule
  ],
  providers: [AuthInterceptorProvider]
})
export class AuthModule { }