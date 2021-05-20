import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAnalyseSST } from '../analyse-sst.model';
import { AnalyseSSTService } from '../service/analyse-sst.service';

@Component({
  templateUrl: './analyse-sst-delete-dialog.component.html',
})
export class AnalyseSSTDeleteDialogComponent {
  analyseSST?: IAnalyseSST;

  constructor(protected analyseSSTService: AnalyseSSTService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.analyseSSTService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
