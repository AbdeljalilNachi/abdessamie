import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ObligationConformiteComponent } from './list/obligation-conformite.component';
import { ObligationConformiteDetailComponent } from './detail/obligation-conformite-detail.component';
import { ObligationConformiteUpdateComponent } from './update/obligation-conformite-update.component';
import { ObligationConformiteDeleteDialogComponent } from './delete/obligation-conformite-delete-dialog.component';
import { ObligationConformiteRoutingModule } from './route/obligation-conformite-routing.module';

@NgModule({
  imports: [SharedModule, ObligationConformiteRoutingModule],
  declarations: [
    ObligationConformiteComponent,
    ObligationConformiteDetailComponent,
    ObligationConformiteUpdateComponent,
    ObligationConformiteDeleteDialogComponent,
  ],
  entryComponents: [ObligationConformiteDeleteDialogComponent],
})
export class ObligationConformiteModule {}
