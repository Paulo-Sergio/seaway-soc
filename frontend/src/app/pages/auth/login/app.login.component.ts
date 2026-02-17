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

  public isLoginFalhou: boolean = false
  public form: FormGroup

  public userInfoVitrine = { token: null, username: null, fullname: null, roles: null }

  public msgs: Message[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private messageService: MessageService,
    private router: Router,
    private authService: AuthService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      username: [null, Validators.required],
      password: [null, Validators.required],
    })
  }

  public onSubmit(): void {
    if (this.form.valid) {
      this.isLoginFalhou = false
      this.authService.login(this.form.get('username')?.value, this.form.get('password')?.value)
        .subscribe({
          next: (res: any) => {
            console.log(res)
            this.userInfoVitrine.token = res.token
            localStorage.setItem('userInfoVitrine', JSON.stringify(this.userInfoVitrine))
            this.userService.getUserByUsername(this.form.get('username').value)
              .subscribe({
                next: (user: User) => {
                  console.log(user)
                  this.userInfoVitrine.username = user.username
                  this.userInfoVitrine.fullname = user.fullname
                  this.userInfoVitrine.roles = user.roles
                  localStorage.setItem('userInfoVitrine', JSON.stringify(this.userInfoVitrine))

                  this.router.navigate(['/produtos'])
                },
                error: (err) => {
                  console.log(err)
                }
              });
          },
          error: (err) => {
            console.log(err)
            if (err.status == '401') {
              this.isLoginFalhou = true
            }
            //this.form.get('username').setValue(null)
            this.form.get('password').setValue(null)
          }
        })
    }
  }
}
