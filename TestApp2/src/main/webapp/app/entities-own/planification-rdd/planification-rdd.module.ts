import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PlanificationRDDComponent } from './list/planification-rdd.component';
import { PlanificationRDDDetailComponent } from './detail/planification-rdd-detail.component';
import { PlanificationRDDUpdateComponent } from './update/planification-rdd-update.component';
import { PlanificationRDDDeleteDialogComponent } from './delete/planification-rdd-delete-dialog.component';
import { PlanificationRDDRoutingModule } from './route/planification-rdd-routing.module';

@NgModule({
  imports: [SharedModule, PlanificationRDDRoutingModule],
  declarations: [
    PlanificationRDDComponent,
    PlanificationRDDDetailComponent,
    PlanificationRDDUpdateComponent,
    PlanificationRDDDeleteDialogComponent,
  ],
  entryComponents: [PlanificationRDDDeleteDialogComponent],
})
export class PlanificationRDDModule {}
