import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ReclamationComponent } from './list/reclamation.component';
import { ReclamationDetailComponent } from './detail/reclamation-detail.component';
import { ReclamationUpdateComponent } from './update/reclamation-update.component';
import { ReclamationDeleteDialogComponent } from './delete/reclamation-delete-dialog.component';
import { ReclamationRoutingModule } from './route/reclamation-routing.module';

@NgModule({
  imports: [SharedModule, ReclamationRoutingModule],
  declarations: [ReclamationComponent, ReclamationDetailComponent, ReclamationUpdateComponent, ReclamationDeleteDialogComponent],
  entryComponents: [ReclamationDeleteDialogComponent],
})
export class ReclamationModule {}
