import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AnalyseSWOTComponent } from '../list/analyse-swot.component';
import { AnalyseSWOTDetailComponent } from '../detail/analyse-swot-detail.component';
import { AnalyseSWOTUpdateComponent } from '../update/analyse-swot-update.component';
import { AnalyseSWOTRoutingResolveService } from './analyse-swot-routing-resolve.service';

const analyseSWOTRoute: Routes = [
  {
    path: '',
    component: AnalyseSWOTComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnalyseSWOTDetailComponent,
    resolve: {
      analyseSWOT: AnalyseSWOTRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnalyseSWOTUpdateComponent,
    resolve: {
      analyseSWOT: AnalyseSWOTRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnalyseSWOTUpdateComponent,
    resolve: {
      analyseSWOT: AnalyseSWOTRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(analyseSWOTRoute)],
  exports: [RouterModule],
})
export class AnalyseSWOTRoutingModule {}
