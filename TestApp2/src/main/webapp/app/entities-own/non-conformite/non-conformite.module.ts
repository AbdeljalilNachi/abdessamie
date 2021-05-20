import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { NonConformiteComponent } from './list/non-conformite.component';
import { NonConformiteDetailComponent } from './detail/non-conformite-detail.component';
import { NonConformiteUpdateComponent } from './update/non-conformite-update.component';
import { NonConformiteDeleteDialogComponent } from './delete/non-conformite-delete-dialog.component';
import { NonConformiteRoutingModule } from './route/non-conformite-routing.module';

@NgModule({
  imports: [SharedModule, NonConformiteRoutingModule],
  declarations: [NonConformiteComponent, NonConformiteDetailComponent, NonConformiteUpdateComponent, NonConformiteDeleteDialogComponent],
  entryComponents: [NonConformiteDeleteDialogComponent],
})
export class NonConformiteModule {}
