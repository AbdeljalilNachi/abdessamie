import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AnalyseEnvirommentaleComponent } from './list/analyse-envirommentale.component';
import { AnalyseEnvirommentaleDetailComponent } from './detail/analyse-envirommentale-detail.component';
import { AnalyseEnvirommentaleUpdateComponent } from './update/analyse-envirommentale-update.component';
import { AnalyseEnvirommentaleDeleteDialogComponent } from './delete/analyse-envirommentale-delete-dialog.component';
import { AnalyseEnvirommentaleRoutingModule } from './route/analyse-envirommentale-routing.module';

@NgModule({
  imports: [SharedModule, AnalyseEnvirommentaleRoutingModule],
  declarations: [
    AnalyseEnvirommentaleComponent,
    AnalyseEnvirommentaleDetailComponent,
    AnalyseEnvirommentaleUpdateComponent,
    AnalyseEnvirommentaleDeleteDialogComponent,
  ],
  entryComponents: [AnalyseEnvirommentaleDeleteDialogComponent],
})
export class AnalyseEnvirommentaleModule {}
