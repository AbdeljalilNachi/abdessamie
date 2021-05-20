import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAutreAction } from '../autre-action.model';
import { AutreActionService } from '../service/autre-action.service';

@Component({
  templateUrl: './autre-action-delete-dialog.component.html',
})
export class AutreActionDeleteDialogComponent {
  autreAction?: IAutreAction;

  constructor(protected autreActionService: AutreActionService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.autreActionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
