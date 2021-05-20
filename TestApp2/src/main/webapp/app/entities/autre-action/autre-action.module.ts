import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AutreActionComponent } from './list/autre-action.component';
import { AutreActionDetailComponent } from './detail/autre-action-detail.component';
import { AutreActionUpdateComponent } from './update/autre-action-update.component';
import { AutreActionDeleteDialogComponent } from './delete/autre-action-delete-dialog.component';
import { AutreActionRoutingModule } from './route/autre-action-routing.module';

@NgModule({
  imports: [SharedModule, AutreActionRoutingModule],
  declarations: [AutreActionComponent, AutreActionDetailComponent, AutreActionUpdateComponent, AutreActionDeleteDialogComponent],
  entryComponents: [AutreActionDeleteDialogComponent],
})
export class AutreActionModule {}
