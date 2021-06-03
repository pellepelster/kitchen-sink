import {ModuleWithProviders, NgModule} from '@angular/core';
import {AuthenticationService} from './authentication.service';
import {CommonModule} from '@angular/common';

@NgModule({
  imports: [CommonModule]
})
export class AuthenticationModule {
  public static forRoot(): ModuleWithProviders<AuthenticationModule> {
    return {
      ngModule: AuthenticationModule,
      providers: [AuthenticationService]
    };
  }
}
