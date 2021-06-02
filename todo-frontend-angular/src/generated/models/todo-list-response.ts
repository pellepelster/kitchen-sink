/* eslint-disable */
/* eslint-disable */
import { TodoListItemResponse } from './todo-list-item-response';
export interface TodoListResponse {
  id: string;
  items?: Array<TodoListItemResponse>;
  name: string;
}
