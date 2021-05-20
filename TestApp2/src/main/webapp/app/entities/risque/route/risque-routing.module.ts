import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RisqueComponent } from '../list/risque.component';
import { RisqueDetailComponent } from '../detail/risque-detail.component';
import { RisqueUpdateComponent } from '../update/risque-update.component';
import { RisqueRoutingResolveService } from './risque-routing-resolve.service';

const risqueRoute: Routes = [
  {
    path: '',
    component: RisqueComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RisqueDetailComponent,
    resolve: {
      risque: RisqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RisqueUpdateComponent,
    resolve: {
      risque: RisqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RisqueUpdateComponent,
    resolve: {
      risque: RisqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(risqueRoute)],
  exports: [RouterModule],
})
export class RisqueRoutingModule {}
