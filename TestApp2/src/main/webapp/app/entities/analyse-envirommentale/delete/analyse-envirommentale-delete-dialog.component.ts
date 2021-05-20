import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAnalyseEnvirommentale } from '../analyse-envirommentale.model';
import { AnalyseEnvirommentaleService } from '../service/analyse-envirommentale.service';

@Component({
  templateUrl: './analyse-envirommentale-delete-dialog.component.html',
})
export class AnalyseEnvirommentaleDeleteDialogComponent {
  analyseEnvirommentale?: IAnalyseEnvirommentale;

  constructor(protected analyseEnvirommentaleService: AnalyseEnvirommentaleService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.analyseEnvirommentaleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
