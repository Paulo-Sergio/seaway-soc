import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Message, MessageService } from 'primeng/api';
import { AuthService } from '../auth.service';
import { UserService } from '../../users/user.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-login',
  templateUrl: './app.login.component.html',
})
export class AppLoginComponent {

  public user: User = { username: 'macaco', password: '' };
  public errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  onSubmit(): void {
    this.authService.login(this.user).subscribe({
      next: () => {
        this.router.navigate(['/produtos']);
      },
      error: (error) => {
        this.errorMessage = 'Usuário ou senha inválidos';
        console.error('Erro de login:', error);
      }
    });
  }
}
