import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BesoinPIComponent } from '../list/besoin-pi.component';
import { BesoinPIDetailComponent } from '../detail/besoin-pi-detail.component';
import { BesoinPIUpdateComponent } from '../update/besoin-pi-update.component';
import { BesoinPIRoutingResolveService } from './besoin-pi-routing-resolve.service';

const besoinPIRoute: Routes = [
  {
    path: '',
    component: BesoinPIComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BesoinPIDetailComponent,
    resolve: {
      besoinPI: BesoinPIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BesoinPIUpdateComponent,
    resolve: {
      besoinPI: BesoinPIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BesoinPIUpdateComponent,
    resolve: {
      besoinPI: BesoinPIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(besoinPIRoute)],
  exports: [RouterModule],
})
export class BesoinPIRoutingModule {}
