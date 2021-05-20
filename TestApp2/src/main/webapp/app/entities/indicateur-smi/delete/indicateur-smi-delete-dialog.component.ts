import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIndicateurSMI } from '../indicateur-smi.model';
import { IndicateurSMIService } from '../service/indicateur-smi.service';

@Component({
  templateUrl: './indicateur-smi-delete-dialog.component.html',
})
export class IndicateurSMIDeleteDialogComponent {
  indicateurSMI?: IIndicateurSMI;

  constructor(protected indicateurSMIService: IndicateurSMIService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.indicateurSMIService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
