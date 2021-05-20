import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAudit } from '../audit.model';
import { AuditService } from '../service/audit.service';

@Component({
  templateUrl: './audit-delete-dialog.component.html',
})
export class AuditDeleteDialogComponent {
  audit?: IAudit;

  constructor(protected auditService: AuditService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.auditService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
