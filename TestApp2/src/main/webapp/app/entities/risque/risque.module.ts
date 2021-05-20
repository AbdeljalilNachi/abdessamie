import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { RisqueComponent } from './list/risque.component';
import { RisqueDetailComponent } from './detail/risque-detail.component';
import { RisqueUpdateComponent } from './update/risque-update.component';
import { RisqueDeleteDialogComponent } from './delete/risque-delete-dialog.component';
import { RisqueRoutingModule } from './route/risque-routing.module';

@NgModule({
  imports: [SharedModule, RisqueRoutingModule],
  declarations: [RisqueComponent, RisqueDetailComponent, RisqueUpdateComponent, RisqueDeleteDialogComponent],
  entryComponents: [RisqueDeleteDialogComponent],
})
export class RisqueModule {}
