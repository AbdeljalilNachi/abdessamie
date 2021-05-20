import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRisque } from '../risque.model';
import { RisqueService } from '../service/risque.service';

@Component({
  templateUrl: './risque-delete-dialog.component.html',
})
export class RisqueDeleteDialogComponent {
  risque?: IRisque;

  constructor(protected risqueService: RisqueService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.risqueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
