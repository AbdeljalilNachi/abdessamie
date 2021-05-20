import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ObjectifComponent } from './list/objectif.component';
import { ObjectifDetailComponent } from './detail/objectif-detail.component';
import { ObjectifUpdateComponent } from './update/objectif-update.component';
import { ObjectifDeleteDialogComponent } from './delete/objectif-delete-dialog.component';
import { ObjectifRoutingModule } from './route/objectif-routing.module';

@NgModule({
  imports: [SharedModule, ObjectifRoutingModule],
  declarations: [ObjectifComponent, ObjectifDetailComponent, ObjectifUpdateComponent, ObjectifDeleteDialogComponent],
  entryComponents: [ObjectifDeleteDialogComponent],
})
export class ObjectifModule {}
