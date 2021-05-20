import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AnalyseSSTComponent } from './list/analyse-sst.component';
import { AnalyseSSTDetailComponent } from './detail/analyse-sst-detail.component';
import { AnalyseSSTUpdateComponent } from './update/analyse-sst-update.component';
import { AnalyseSSTDeleteDialogComponent } from './delete/analyse-sst-delete-dialog.component';
import { AnalyseSSTRoutingModule } from './route/analyse-sst-routing.module';

@NgModule({
  imports: [SharedModule, AnalyseSSTRoutingModule],
  declarations: [AnalyseSSTComponent, AnalyseSSTDetailComponent, AnalyseSSTUpdateComponent, AnalyseSSTDeleteDialogComponent],
  entryComponents: [AnalyseSSTDeleteDialogComponent],
})
export class AnalyseSSTModule {}
