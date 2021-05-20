import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlanificationRDD } from '../planification-rdd.model';
import { PlanificationRDDService } from '../service/planification-rdd.service';

@Component({
  templateUrl: './planification-rdd-delete-dialog.component.html',
})
export class PlanificationRDDDeleteDialogComponent {
  planificationRDD?: IPlanificationRDD;

  constructor(protected planificationRDDService: PlanificationRDDService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.planificationRDDService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
