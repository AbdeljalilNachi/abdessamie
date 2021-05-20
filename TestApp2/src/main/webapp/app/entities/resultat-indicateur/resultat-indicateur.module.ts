import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ResultatIndicateurComponent } from './list/resultat-indicateur.component';
import { ResultatIndicateurDetailComponent } from './detail/resultat-indicateur-detail.component';
import { ResultatIndicateurUpdateComponent } from './update/resultat-indicateur-update.component';
import { ResultatIndicateurDeleteDialogComponent } from './delete/resultat-indicateur-delete-dialog.component';
import { ResultatIndicateurRoutingModule } from './route/resultat-indicateur-routing.module';

@NgModule({
  imports: [SharedModule, ResultatIndicateurRoutingModule],
  declarations: [
    ResultatIndicateurComponent,
    ResultatIndicateurDetailComponent,
    ResultatIndicateurUpdateComponent,
    ResultatIndicateurDeleteDialogComponent,
  ],
  entryComponents: [ResultatIndicateurDeleteDialogComponent],
})
export class ResultatIndicateurModule {}
