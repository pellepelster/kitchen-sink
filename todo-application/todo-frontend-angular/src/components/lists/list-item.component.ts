import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TodoListItemResponse} from '../../generated/models';

@Component({
  selector: 'todo-list-item',
  templateUrl: './list-item.component.html',
  styleUrls: ['./list-item.component.css'],
})
export class ListItemComponent {

  @Input() item: TodoListItemResponse;
  @Input() first: Boolean;
  @Input() last: Boolean;

  @Output() delete = new EventEmitter<string>();
  @Output() markAsDone = new EventEmitter<string>();
  @Output() markAsPending = new EventEmitter<string>();

}
