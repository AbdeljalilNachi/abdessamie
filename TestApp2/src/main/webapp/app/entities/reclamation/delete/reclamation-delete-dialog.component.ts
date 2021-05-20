import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReclamation } from '../reclamation.model';
import { ReclamationService } from '../service/reclamation.service';

@Component({
  templateUrl: './reclamation-delete-dialog.component.html',
})
export class ReclamationDeleteDialogComponent {
  reclamation?: IReclamation;

  constructor(protected reclamationService: ReclamationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reclamationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
