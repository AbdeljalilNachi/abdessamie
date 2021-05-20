import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PolitiqueQSEComponent } from './list/politique-qse.component';
import { PolitiqueQSEDetailComponent } from './detail/politique-qse-detail.component';
import { PolitiqueQSEUpdateComponent } from './update/politique-qse-update.component';
import { PolitiqueQSEDeleteDialogComponent } from './delete/politique-qse-delete-dialog.component';
import { PolitiqueQSERoutingModule } from './route/politique-qse-routing.module';

@NgModule({
  imports: [SharedModule, PolitiqueQSERoutingModule],
  declarations: [PolitiqueQSEComponent, PolitiqueQSEDetailComponent, PolitiqueQSEUpdateComponent, PolitiqueQSEDeleteDialogComponent],
  entryComponents: [PolitiqueQSEDeleteDialogComponent],
})
export class PolitiqueQSEModule {}
