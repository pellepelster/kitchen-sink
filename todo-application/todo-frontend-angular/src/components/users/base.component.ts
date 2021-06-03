import {translate} from '@ngneat/transloco';
import {ErrorMessage} from '../../generated/models/error-message';
import {ErrorStateMatcher} from '@angular/material/core';
import {FormControl, FormGroupDirective, NgForm} from '@angular/forms';

export class RemoteErrorStateMatcher implements ErrorStateMatcher {

  constructor(private baseComponent: BaseComponent, private attribute: String) {
  }

  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    return this.baseComponent.hasError(this.attribute);
  }
}

export class BaseComponent {

  messages: Array<ErrorMessage>;

  public hasError(attribute: String) {
    return this.getMessagesForAttribute(attribute).length > 0;
  }

  public getErrorText(attribute: String) {
    const messages = this.getMessagesForAttribute(attribute);

    if (messages.length == 0) {
      return null;
    }

    return messages.map(item => translate(item.code)).join(' ');
  }

  public getMessagesForAttribute(attribute: String) {

    if (this.messages) {
      return this.messages.filter(item => item.attribute === attribute);
    }

    return [];
  }

  public handleError(error: any) {
    if (error.error?.messages) {
      this.messages = error.error?.messages;
    }
  }

}
