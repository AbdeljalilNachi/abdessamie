import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IObligationConformite } from '../obligation-conformite.model';
import { ObligationConformiteService } from '../service/obligation-conformite.service';

@Component({
  templateUrl: './obligation-conformite-delete-dialog.component.html',
})
export class ObligationConformiteDeleteDialogComponent {
  obligationConformite?: IObligationConformite;

  constructor(protected obligationConformiteService: ObligationConformiteService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.obligationConformiteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
