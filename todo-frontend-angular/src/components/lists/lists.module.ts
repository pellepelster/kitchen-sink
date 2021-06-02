import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ListsComponent} from './lists.component';
import {ListComponent} from './list.component';
import {CommonModule} from '@angular/common';
import {ListItemComponent} from './list-item.component';
import {CommonModule as CommonModule1} from '../common/common.module';
import {TranslocoModule} from '@ngneat/transloco';
import {MatButtonModule} from '@angular/material/button';
import {ListsAddDialogComponent} from './lists-add-dialog.component';
import {MatDialogModule} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {MatDividerModule} from '@angular/material/divider';
import {MatCardModule} from '@angular/material/card';

@NgModule({
  declarations: [
    ListsComponent,
    ListComponent,
    ListItemComponent,
    ListsAddDialogComponent
  ],
  imports: [
    FormsModule,
    CommonModule1,
    TranslocoModule,
    CommonModule,
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatMenuModule,
    MatDividerModule,
    MatCardModule
  ],
  providers: []
})
export class ListsModule {
}
