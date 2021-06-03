import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UsersModule} from '../components/users/users.module';
import {LoginComponent} from '../components/users/login.component';
import {RegisterComponent} from '../components/users/register.component';
import {ListsComponent} from '../components/lists/lists.component';
import {ListsModule} from '../components/lists/lists.module';
import {HomeComponent} from '../components/home/home.component';
import {HomeModule} from '../components/home/home.module';
import {AuthenticationModule} from '../components/authentication/authentication.module';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {
    path: 'lists',
    component: ListsComponent,
    children: [
      {path: 'actions/:action', component: ListsComponent}
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes), AuthenticationModule.forRoot(), HomeModule, UsersModule, ListsModule],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
