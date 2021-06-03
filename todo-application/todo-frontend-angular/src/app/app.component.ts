import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationService} from '../components/authentication/authentication.service';

@Component({
  selector: 'todo-root',
  templateUrl: './app.component.html',
})
export class AppComponent {

  constructor(private router: Router, private authenticationService: AuthenticationService) {
  }

  public logout() {
    this.authenticationService.clearAuthentication();
    this.router.navigate(['/']);
  }

  public showLogout() {
    return this.authenticationService.hasAuthentication();
  }

}
