import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ProcessusSMIComponent } from './list/processus-smi.component';
import { ProcessusSMIDetailComponent } from './detail/processus-smi-detail.component';
import { ProcessusSMIUpdateComponent } from './update/processus-smi-update.component';
import { ProcessusSMIDeleteDialogComponent } from './delete/processus-smi-delete-dialog.component';
import { ProcessusSMIRoutingModule } from './route/processus-smi-routing.module';

@NgModule({
  imports: [SharedModule, ProcessusSMIRoutingModule],
  declarations: [ProcessusSMIComponent, ProcessusSMIDetailComponent, ProcessusSMIUpdateComponent, ProcessusSMIDeleteDialogComponent],
  entryComponents: [ProcessusSMIDeleteDialogComponent],
})
export class ProcessusSMIModule {}
