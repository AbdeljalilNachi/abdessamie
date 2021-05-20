import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ConstatAuditComponent } from './list/constat-audit.component';
import { ConstatAuditDetailComponent } from './detail/constat-audit-detail.component';
import { ConstatAuditUpdateComponent } from './update/constat-audit-update.component';
import { ConstatAuditDeleteDialogComponent } from './delete/constat-audit-delete-dialog.component';
import { ConstatAuditRoutingModule } from './route/constat-audit-routing.module';

@NgModule({
  imports: [SharedModule, ConstatAuditRoutingModule],
  declarations: [ConstatAuditComponent, ConstatAuditDetailComponent, ConstatAuditUpdateComponent, ConstatAuditDeleteDialogComponent],
  entryComponents: [ConstatAuditDeleteDialogComponent],
})
export class ConstatAuditModule {}
