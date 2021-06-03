import {Component, OnInit} from '@angular/core';
import {UsersService} from '../../generated/services/users.service';
import {Router} from '@angular/router';

@Component({
  selector: 'todo-home',
  template: '<div></div>',
})
export class HomeComponent implements OnInit {

  constructor(private usersService: UsersService, private router: Router) {
  }

  ngOnInit(): void {
    this.usersService.whoami().subscribe(
      _ => this.router.navigate(['/lists']),
      _ => this.router.navigate(['/login']));
  }
}
