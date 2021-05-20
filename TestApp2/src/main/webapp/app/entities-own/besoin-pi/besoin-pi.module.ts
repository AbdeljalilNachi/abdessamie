import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BesoinPIComponent } from './list/besoin-pi.component';
import { BesoinPIDetailComponent } from './detail/besoin-pi-detail.component';
import { BesoinPIUpdateComponent } from './update/besoin-pi-update.component';
import { BesoinPIDeleteDialogComponent } from './delete/besoin-pi-delete-dialog.component';
import { BesoinPIRoutingModule } from './route/besoin-pi-routing.module';

@NgModule({
  imports: [SharedModule, BesoinPIRoutingModule],
  declarations: [BesoinPIComponent, BesoinPIDetailComponent, BesoinPIUpdateComponent, BesoinPIDeleteDialogComponent],
  entryComponents: [BesoinPIDeleteDialogComponent],
})
export class BesoinPIModule {}
