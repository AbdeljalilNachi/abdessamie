import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAnalyseSWOT } from '../analyse-swot.model';
import { AnalyseSWOTService } from '../service/analyse-swot.service';

@Component({
  templateUrl: './analyse-swot-delete-dialog.component.html',
})
export class AnalyseSWOTDeleteDialogComponent {
  analyseSWOT?: IAnalyseSWOT;

  constructor(protected analyseSWOTService: AnalyseSWOTService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.analyseSWOTService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
