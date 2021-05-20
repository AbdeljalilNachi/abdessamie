import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ResultIndicateursComponent } from './list/result-indicateurs.component';
import { ResultIndicateursDetailComponent } from './detail/result-indicateurs-detail.component';
import { ResultIndicateursUpdateComponent } from './update/result-indicateurs-update.component';
import { ResultIndicateursDeleteDialogComponent } from './delete/result-indicateurs-delete-dialog.component';
import { ResultIndicateursRoutingModule } from './route/result-indicateurs-routing.module';

@NgModule({
  imports: [SharedModule, ResultIndicateursRoutingModule],
  declarations: [
    ResultIndicateursComponent,
    ResultIndicateursDetailComponent,
    ResultIndicateursUpdateComponent,
    ResultIndicateursDeleteDialogComponent,
  ],
  entryComponents: [ResultIndicateursDeleteDialogComponent],
})
export class ResultIndicateursModule {}
