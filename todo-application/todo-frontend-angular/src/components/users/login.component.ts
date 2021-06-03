import {Component} from '@angular/core';
import {UsersService} from '../../generated/services/users.service';
import {Router} from '@angular/router';
import {AuthenticationService} from '../authentication/authentication.service';
import {BaseComponent, RemoteErrorStateMatcher} from './base.component';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'todo-login',
  templateUrl: './login.component.html',
})
export class LoginComponent extends BaseComponent {

  constructor(private snackBar: MatSnackBar, private usersService: UsersService, private router: Router, private authenticationService: AuthenticationService) {
    super();
  }

  email = '';
  emailErrorMatcher = new RemoteErrorStateMatcher(this, 'email');

  password = '';
  passwordErrorMatcher = new RemoteErrorStateMatcher(this, 'password');

  public async doLogin() {

    this.usersService.login({
      body: {
        email: this.email,
        password: this.password
      }
    }).subscribe(response => {
        this.authenticationService.setAuthentication(response.authorization);
        this.router.navigate(['/lists']);
      },
      error => {

        if (error.status == 400) {
          if (error.error?.messages) {
            this.messages = error.error.messages;
          }
        }

        if (error.status == 401) {
          this.messages = [];
          this.snackBar.open('Login failed', 'Ok', {
            duration: 3000
          });
        }
      });
  }
}
