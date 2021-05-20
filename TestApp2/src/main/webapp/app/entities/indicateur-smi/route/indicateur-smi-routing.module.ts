import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IndicateurSMIComponent } from '../list/indicateur-smi.component';
import { IndicateurSMIDetailComponent } from '../detail/indicateur-smi-detail.component';
import { IndicateurSMIUpdateComponent } from '../update/indicateur-smi-update.component';
import { IndicateurSMIRoutingResolveService } from './indicateur-smi-routing-resolve.service';

const indicateurSMIRoute: Routes = [
  {
    path: '',
    component: IndicateurSMIComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IndicateurSMIDetailComponent,
    resolve: {
      indicateurSMI: IndicateurSMIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IndicateurSMIUpdateComponent,
    resolve: {
      indicateurSMI: IndicateurSMIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IndicateurSMIUpdateComponent,
    resolve: {
      indicateurSMI: IndicateurSMIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(indicateurSMIRoute)],
  exports: [RouterModule],
})
export class IndicateurSMIRoutingModule {}
