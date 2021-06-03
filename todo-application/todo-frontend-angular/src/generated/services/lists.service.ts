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

import { TodoListCreateRequest } from '../models/todo-list-create-request';
import { TodoListItemCreateRequest } from '../models/todo-list-item-create-request';
import { TodoListItemUpdateRequest } from '../models/todo-list-item-update-request';
import { TodoListResponse } from '../models/todo-list-response';


/**
 * Todo Lists API
 */
@Injectable({
  providedIn: 'root',
})
export class ListsService extends BaseService {
  constructor(
    config: ApiConfiguration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * Path part for operation get
   */
  static readonly GetPath = '/api/v1/lists/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `get()` instead.
   *
   * This method doesn't expect any request body.
   */
  get$Response(params: {
    id: string;
  }): Observable<StrictHttpResponse<TodoListResponse>> {

    const rb = new RequestBuilder(this.rootUrl, ListsService.GetPath, 'get');
    if (params) {
      rb.path('id', params.id, {});
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<TodoListResponse>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `get$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  get(params: {
    id: string;
  }): Observable<TodoListResponse> {

    return this.get$Response(params).pipe(
      map((r: StrictHttpResponse<TodoListResponse>) => r.body as TodoListResponse)
    );
  }

  /**
   * Path part for operation createItem
   */
  static readonly CreateItemPath = '/api/v1/lists/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createItem()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createItem$Response(params: {
    id: string;
    body: TodoListItemCreateRequest
  }): Observable<StrictHttpResponse<TodoListResponse>> {

    const rb = new RequestBuilder(this.rootUrl, ListsService.CreateItemPath, 'post');
    if (params) {
      rb.path('id', params.id, {});
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<TodoListResponse>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `createItem$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createItem(params: {
    id: string;
    body: TodoListItemCreateRequest
  }): Observable<TodoListResponse> {

    return this.createItem$Response(params).pipe(
      map((r: StrictHttpResponse<TodoListResponse>) => r.body as TodoListResponse)
    );
  }

  /**
   * Path part for operation delete
   */
  static readonly DeletePath = '/api/v1/lists/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete$Response(params: {
    id: string;
  }): Observable<StrictHttpResponse<void>> {

    const rb = new RequestBuilder(this.rootUrl, ListsService.DeletePath, 'delete');
    if (params) {
      rb.path('id', params.id, {});
    }

    return this.http.request(rb.build({
      responseType: 'text',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return (r as HttpResponse<any>).clone({ body: undefined }) as StrictHttpResponse<void>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `delete$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete(params: {
    id: string;
  }): Observable<void> {

    return this.delete$Response(params).pipe(
      map((r: StrictHttpResponse<void>) => r.body as void)
    );
  }

  /**
   * Path part for operation list
   */
  static readonly ListPath = '/api/v1/lists';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `list()` instead.
   *
   * This method doesn't expect any request body.
   */
  list$Response(params?: {
  }): Observable<StrictHttpResponse<Array<TodoListResponse>>> {

    const rb = new RequestBuilder(this.rootUrl, ListsService.ListPath, 'get');
    if (params) {
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<Array<TodoListResponse>>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `list$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  list(params?: {
  }): Observable<Array<TodoListResponse>> {

    return this.list$Response(params).pipe(
      map((r: StrictHttpResponse<Array<TodoListResponse>>) => r.body as Array<TodoListResponse>)
    );
  }

  /**
   * Path part for operation create
   */
  static readonly CreatePath = '/api/v1/lists';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `create()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create$Response(params: {
    body: TodoListCreateRequest
  }): Observable<StrictHttpResponse<TodoListResponse>> {

    const rb = new RequestBuilder(this.rootUrl, ListsService.CreatePath, 'post');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<TodoListResponse>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `create$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create(params: {
    body: TodoListCreateRequest
  }): Observable<TodoListResponse> {

    return this.create$Response(params).pipe(
      map((r: StrictHttpResponse<TodoListResponse>) => r.body as TodoListResponse)
    );
  }

  /**
   * Path part for operation deleteItem
   */
  static readonly DeleteItemPath = '/api/v1/lists/{listId}/items/{itemId}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteItem()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteItem$Response(params: {
    listId: string;
    itemId: string;
  }): Observable<StrictHttpResponse<TodoListResponse>> {

    const rb = new RequestBuilder(this.rootUrl, ListsService.DeleteItemPath, 'delete');
    if (params) {
      rb.path('listId', params.listId, {});
      rb.path('itemId', params.itemId, {});
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<TodoListResponse>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `deleteItem$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteItem(params: {
    listId: string;
    itemId: string;
  }): Observable<TodoListResponse> {

    return this.deleteItem$Response(params).pipe(
      map((r: StrictHttpResponse<TodoListResponse>) => r.body as TodoListResponse)
    );
  }

  /**
   * Path part for operation updateItem
   */
  static readonly UpdateItemPath = '/api/v1/lists/{listId}/items/{itemId}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateItem()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateItem$Response(params: {
    listId: string;
    itemId: string;
    body: TodoListItemUpdateRequest
  }): Observable<StrictHttpResponse<TodoListResponse>> {

    const rb = new RequestBuilder(this.rootUrl, ListsService.UpdateItemPath, 'patch');
    if (params) {
      rb.path('listId', params.listId, {});
      rb.path('itemId', params.itemId, {});
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<TodoListResponse>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `updateItem$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateItem(params: {
    listId: string;
    itemId: string;
    body: TodoListItemUpdateRequest
  }): Observable<TodoListResponse> {

    return this.updateItem$Response(params).pipe(
      map((r: StrictHttpResponse<TodoListResponse>) => r.body as TodoListResponse)
    );
  }

}
