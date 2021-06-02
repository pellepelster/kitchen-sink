import {Component, OnInit} from '@angular/core';
import {ListsService} from '../../generated/services/lists.service';
import {TodoListResponse} from '../../generated/models';
import {BaseComponent} from '../users/base.component';
import {MatDialog} from '@angular/material/dialog';
import {ListsAddDialogComponent} from './lists-add-dialog.component';
import {MatSnackBar} from '@angular/material/snack-bar';
import {TranslocoService} from '@ngneat/transloco';
import {ActivatedRoute, ActivationEnd, Router} from '@angular/router';

@Component({
  selector: 'todo-lists',
  templateUrl: './lists.component.html',
  styleUrls: ['./lists.component.css'],
})
export class ListsComponent extends BaseComponent implements OnInit {

  constructor(private route: ActivatedRoute, private router: Router, private snackBar: MatSnackBar, private translocoService: TranslocoService, private dialog: MatDialog, private listsService: ListsService) {
    super();
    router.events.subscribe((val) => {
      if (val instanceof ActivationEnd) {
        const action = (val as ActivationEnd).snapshot.paramMap.get('action');
        if (action == 'add-new-list') {
          this.openAddDialog();
        }
      }
    });
  }

  name: string;

  lists: Array<TodoListResponse> = [];

  ngOnInit(): void {
    this.updateLists();
  }

  private updateLists() {
    this.listsService.list().subscribe(lists => {
      this.lists = lists;
    });
  }

  createList(newListName: string) {
    this.listsService.create({
      body: {
        name: newListName
      }
    }).subscribe(_ => {
        this.updateLists();
      },
      error => {
        this.handleError(error);
      });
  }

  async deleteList(id: string) {
    await this.listsService.delete({
      id
    }).toPromise();
    this.updateLists();
  }

  listId(index: number, el: any): number {
    return el.id;
  }

  openAddDialog(): void {
    const dialogRef = this.dialog.open(ListsAddDialogComponent, {
      width: '24em',
      data: {name: this.name}
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.createList(result);
      }

      this.name = '';
      this.router.navigate(['/lists']);
    });
  }
}
