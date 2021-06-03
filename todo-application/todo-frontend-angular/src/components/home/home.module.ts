import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HomeComponent} from './home.component';
import {CommonModule} from '@angular/common';

@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    FormsModule,
    CommonModule
  ],
  providers: []
})
export class HomeModule {
}
