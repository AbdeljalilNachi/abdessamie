import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IResultIndicateurs } from '../result-indicateurs.model';
import { ResultIndicateursService } from '../service/result-indicateurs.service';

@Component({
  templateUrl: './result-indicateurs-delete-dialog.component.html',
})
export class ResultIndicateursDeleteDialogComponent {
  resultIndicateurs?: IResultIndicateurs;

  constructor(protected resultIndicateursService: ResultIndicateursService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.resultIndicateursService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
