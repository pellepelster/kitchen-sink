import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TodoListResponse} from '../../generated/models';
import {ListsService} from '../../generated/services/lists.service';

@Component({
  selector: 'todo-list',
  templateUrl: './list.component.html',
  styleUrls: ['./lists.css']
})
export class ListComponent implements OnInit {

  @Input() list: TodoListResponse;

  @Output() delete = new EventEmitter<string>();

  name = '';

  constructor(private listsService: ListsService) {
  }

  updateList() {
    this.listsService.get({id: this.list.id}).subscribe(list => {
      this.list = list;
    });
  }

  ngOnInit(): void {
    this.updateList();
  }

  async createItem() {
    await this.listsService.createItem({
      id: this.list.id,
      body: {
        name: this.name
      }
    }).toPromise();
    this.name = '';
    this.updateList();
  }

  async deleteItem(id: string) {
    await this.listsService.deleteItem({
      listId: this.list.id,
      itemId: id
    }).toPromise();
    this.updateList();
  }

  async markAsDone(id: string) {
    await this.listsService.updateItem({
      listId: this.list.id,
      itemId: id,
      body: {
        done: true
      }
    }).toPromise();
    this.updateList();
  }

  async markAsPending(id: string) {
    await this.listsService.updateItem({
      listId: this.list.id,
      itemId: id,
      body: {
        done: false
      }
    }).toPromise();
    this.updateList();
  }

  itemId(index: number, el: any): number {
    return el.id;
  }
}
