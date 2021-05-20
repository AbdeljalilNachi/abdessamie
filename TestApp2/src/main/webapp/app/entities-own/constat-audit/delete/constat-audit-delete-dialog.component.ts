import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConstatAudit } from '../constat-audit.model';
import { ConstatAuditService } from '../service/constat-audit.service';

@Component({
  templateUrl: './constat-audit-delete-dialog.component.html',
})
export class ConstatAuditDeleteDialogComponent {
  constatAudit?: IConstatAudit;

  constructor(protected constatAuditService: ConstatAuditService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.constatAuditService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
