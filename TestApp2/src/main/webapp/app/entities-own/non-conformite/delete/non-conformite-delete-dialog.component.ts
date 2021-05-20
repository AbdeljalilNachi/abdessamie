import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INonConformite } from '../non-conformite.model';
import { NonConformiteService } from '../service/non-conformite.service';

@Component({
  templateUrl: './non-conformite-delete-dialog.component.html',
})
export class NonConformiteDeleteDialogComponent {
  nonConformite?: INonConformite;

  constructor(protected nonConformiteService: NonConformiteService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nonConformiteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
