/* eslint-disable */
/* eslint-disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';
import { RequestBuilder } from '../request-builder';
import { Observable } from 'rxjs';
import { map, filter } from 'rxjs/operators';

import { UserLoginRequest } from '../models/user-login-request';
import { UserLoginResponse } from '../models/user-login-response';
import { UserRegistrationRequest } from '../models/user-registration-request';
import { WhoAmiResponse } from '../models/who-ami-response';


/**
 * User API
 */
@Injectable({
  providedIn: 'root',
})
export class UsersService extends BaseService {
  constructor(
    config: ApiConfiguration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * Path part for operation register
   */
  static readonly RegisterPath = '/api/v1/users/public/register';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `register()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  register$Response(params: {
    body: UserRegistrationRequest
  }): Observable<StrictHttpResponse<UserLoginResponse>> {

    const rb = new RequestBuilder(this.rootUrl, UsersService.RegisterPath, 'post');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<UserLoginResponse>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `register$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  register(params: {
    body: UserRegistrationRequest
  }): Observable<UserLoginResponse> {

    return this.register$Response(params).pipe(
      map((r: StrictHttpResponse<UserLoginResponse>) => r.body as UserLoginResponse)
    );
  }

  /**
   * Path part for operation login
   */
  static readonly LoginPath = '/api/v1/users/public/login';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `login()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  login$Response(params: {
    body: UserLoginRequest
  }): Observable<StrictHttpResponse<UserLoginResponse>> {

    const rb = new RequestBuilder(this.rootUrl, UsersService.LoginPath, 'post');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<UserLoginResponse>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `login$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  login(params: {
    body: UserLoginRequest
  }): Observable<UserLoginResponse> {

    return this.login$Response(params).pipe(
      map((r: StrictHttpResponse<UserLoginResponse>) => r.body as UserLoginResponse)
    );
  }

  /**
   * Path part for operation whoami
   */
  static readonly WhoamiPath = '/api/v1/users/whoami';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `whoami()` instead.
   *
   * This method doesn't expect any request body.
   */
  whoami$Response(params?: {
  }): Observable<StrictHttpResponse<WhoAmiResponse>> {

    const rb = new RequestBuilder(this.rootUrl, UsersService.WhoamiPath, 'get');
    if (params) {
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<WhoAmiResponse>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `whoami$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  whoami(params?: {
  }): Observable<WhoAmiResponse> {

    return this.whoami$Response(params).pipe(
      map((r: StrictHttpResponse<WhoAmiResponse>) => r.body as WhoAmiResponse)
    );
  }

}
