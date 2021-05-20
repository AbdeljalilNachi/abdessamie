import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { IndicateurSMIComponent } from './list/indicateur-smi.component';
import { IndicateurSMIDetailComponent } from './detail/indicateur-smi-detail.component';
import { IndicateurSMIUpdateComponent } from './update/indicateur-smi-update.component';
import { IndicateurSMIDeleteDialogComponent } from './delete/indicateur-smi-delete-dialog.component';
import { IndicateurSMIRoutingModule } from './route/indicateur-smi-routing.module';

@NgModule({
  imports: [SharedModule, IndicateurSMIRoutingModule],
  declarations: [IndicateurSMIComponent, IndicateurSMIDetailComponent, IndicateurSMIUpdateComponent, IndicateurSMIDeleteDialogComponent],
  entryComponents: [IndicateurSMIDeleteDialogComponent],
})
export class IndicateurSMIModule {}
