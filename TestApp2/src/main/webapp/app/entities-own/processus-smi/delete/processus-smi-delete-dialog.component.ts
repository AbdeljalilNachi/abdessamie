import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProcessusSMI } from '../processus-smi.model';
import { ProcessusSMIService } from '../service/processus-smi.service';

@Component({
  templateUrl: './processus-smi-delete-dialog.component.html',
})
export class ProcessusSMIDeleteDialogComponent {
  processusSMI?: IProcessusSMI;

  constructor(protected processusSMIService: ProcessusSMIService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.processusSMIService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
