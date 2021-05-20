import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlanificationRDDComponent } from '../list/planification-rdd.component';
import { PlanificationRDDDetailComponent } from '../detail/planification-rdd-detail.component';
import { PlanificationRDDUpdateComponent } from '../update/planification-rdd-update.component';
import { PlanificationRDDRoutingResolveService } from './planification-rdd-routing-resolve.service';

const planificationRDDRoute: Routes = [
  {
    path: '',
    component: PlanificationRDDComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlanificationRDDDetailComponent,
    resolve: {
      planificationRDD: PlanificationRDDRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlanificationRDDUpdateComponent,
    resolve: {
      planificationRDD: PlanificationRDDRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlanificationRDDUpdateComponent,
    resolve: {
      planificationRDD: PlanificationRDDRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(planificationRDDRoute)],
  exports: [RouterModule],
})
export class PlanificationRDDRoutingModule {}
