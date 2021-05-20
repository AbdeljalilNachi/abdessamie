import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IResultatIndicateur } from '../resultat-indicateur.model';
import { ResultatIndicateurService } from '../service/resultat-indicateur.service';

@Component({
  templateUrl: './resultat-indicateur-delete-dialog.component.html',
})
export class ResultatIndicateurDeleteDialogComponent {
  resultatIndicateur?: IResultatIndicateur;

  constructor(protected resultatIndicateurService: ResultatIndicateurService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.resultatIndicateurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
