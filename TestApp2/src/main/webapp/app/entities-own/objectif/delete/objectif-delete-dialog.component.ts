import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IObjectif } from '../objectif.model';
import { ObjectifService } from '../service/objectif.service';

@Component({
  templateUrl: './objectif-delete-dialog.component.html',
})
export class ObjectifDeleteDialogComponent {
  objectif?: IObjectif;

  constructor(protected objectifService: ObjectifService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.objectifService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
