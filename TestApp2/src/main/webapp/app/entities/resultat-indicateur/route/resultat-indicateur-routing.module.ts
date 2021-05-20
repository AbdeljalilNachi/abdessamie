import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ResultatIndicateurComponent } from '../list/resultat-indicateur.component';
import { ResultatIndicateurDetailComponent } from '../detail/resultat-indicateur-detail.component';
import { ResultatIndicateurUpdateComponent } from '../update/resultat-indicateur-update.component';
import { ResultatIndicateurRoutingResolveService } from './resultat-indicateur-routing-resolve.service';

const resultatIndicateurRoute: Routes = [
  {
    path: '',
    component: ResultatIndicateurComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResultatIndicateurDetailComponent,
    resolve: {
      resultatIndicateur: ResultatIndicateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResultatIndicateurUpdateComponent,
    resolve: {
      resultatIndicateur: ResultatIndicateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResultatIndicateurUpdateComponent,
    resolve: {
      resultatIndicateur: ResultatIndicateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(resultatIndicateurRoute)],
  exports: [RouterModule],
})
export class ResultatIndicateurRoutingModule {}
