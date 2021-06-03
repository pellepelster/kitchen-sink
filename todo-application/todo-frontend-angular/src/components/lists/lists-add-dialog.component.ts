import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

export interface DialogData {
  name: string;
}

@Component({
  selector: 'todo-list-add-dialog',
  templateUrl: 'lists-add-dialog.component.html',
})
export class ListsAddDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ListsAddDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
