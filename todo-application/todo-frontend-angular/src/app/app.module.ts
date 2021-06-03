import {forwardRef, LOCALE_ID, NgModule, Provider} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {ApiModule} from '../generated/api.module';
import {ApiInterceptor} from './api.interceptor';
import {TranslocoRootModule} from './transloco/transloco-root.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import { environment } from '../environments/environment';

export const API_INTERCEPTOR_PROVIDER: Provider = {
  provide: HTTP_INTERCEPTORS,
  useExisting: forwardRef(() => ApiInterceptor),
  multi: true
};

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ApiModule.forRoot({
      rootUrl: environment.rootUrl
    }),
    TranslocoRootModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatIconModule
  ],
  providers: [
    ApiInterceptor,
    API_INTERCEPTOR_PROVIDER,
    {provide: LOCALE_ID, useValue: 'en'}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
