import {Injectable} from '@angular/core';

@Injectable()
export class AuthenticationService {

  public setAuthentication(authentication: string) {
    localStorage.setItem('authorization', authentication);
  }

  public clearAuthentication() {
    localStorage.clear();
  }

  public getAuthentication(): string {
    return localStorage.getItem('authorization') + '';
  }

  public hasAuthentication() {
    return localStorage.getItem('authorization') != null;
  }

}
