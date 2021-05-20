import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBesoinPI } from '../besoin-pi.model';
import { BesoinPIService } from '../service/besoin-pi.service';

@Component({
  templateUrl: './besoin-pi-delete-dialog.component.html',
})
export class BesoinPIDeleteDialogComponent {
  besoinPI?: IBesoinPI;

  constructor(protected besoinPIService: BesoinPIService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.besoinPIService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
