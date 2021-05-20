import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPolitiqueQSE } from '../politique-qse.model';
import { PolitiqueQSEService } from '../service/politique-qse.service';

@Component({
  templateUrl: './politique-qse-delete-dialog.component.html',
})
export class PolitiqueQSEDeleteDialogComponent {
  politiqueQSE?: IPolitiqueQSE;

  constructor(protected politiqueQSEService: PolitiqueQSEService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.politiqueQSEService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
