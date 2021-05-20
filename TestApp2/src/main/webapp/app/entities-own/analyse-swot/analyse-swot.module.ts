import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AnalyseSWOTComponent } from './list/analyse-swot.component';
import { AnalyseSWOTDetailComponent } from './detail/analyse-swot-detail.component';
import { AnalyseSWOTUpdateComponent } from './update/analyse-swot-update.component';
import { AnalyseSWOTDeleteDialogComponent } from './delete/analyse-swot-delete-dialog.component';
import { AnalyseSWOTRoutingModule } from './route/analyse-swot-routing.module';

@NgModule({
  imports: [SharedModule, AnalyseSWOTRoutingModule],
  declarations: [AnalyseSWOTComponent, AnalyseSWOTDetailComponent, AnalyseSWOTUpdateComponent, AnalyseSWOTDeleteDialogComponent],
  entryComponents: [AnalyseSWOTDeleteDialogComponent],
})
export class AnalyseSWOTModule {}
