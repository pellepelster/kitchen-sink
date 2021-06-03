import {Component} from '@angular/core';
import {UsersService} from '../../generated/services/users.service';
import {Router} from '@angular/router';
import {BaseComponent, RemoteErrorStateMatcher} from './base.component';

@Component({
  selector: 'todo-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent extends BaseComponent {

  constructor(private usersService: UsersService, private router: Router) {
    super();
  }

  email = '';
  emailErrorMatcher = new RemoteErrorStateMatcher(this, 'email');

  password = '';
  passwordErrorMatcher = new RemoteErrorStateMatcher(this, 'password');

  public doRegister() {

    this.usersService.register({
      body: {
        email: this.email,
        password: this.password
      }
    }).subscribe(
      _ => this.router.navigate(['/login']),
      error => {
        if (error.status == 400) {
          if (error.error?.messages) {
            this.messages = error.error.messages;
          }
        }      },
      () => console.log('HTTP request completed.')
    );
  }
}
