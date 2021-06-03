import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {Router} from '@angular/router';
import {AuthenticationService} from '../components/authentication/authentication.service';

@Injectable()
export class ApiInterceptor implements HttpInterceptor {

  constructor(private router: Router, private authenticationService: AuthenticationService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    req = req.clone({
      setHeaders: {
        Authorization: '' + this.authenticationService.getAuthentication(),
        'Content-Type': 'application/json'
      }
    });

    return next.handle(req).pipe(
      tap(x => x, err => {
        if (err.status > 499) {
          this.authenticationService.clearAuthentication();
          this.router.navigate(['/login']);
        }
      })
    );
  }
}
