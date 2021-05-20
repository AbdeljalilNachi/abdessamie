import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AnalyseSSTComponent } from '../list/analyse-sst.component';
import { AnalyseSSTDetailComponent } from '../detail/analyse-sst-detail.component';
import { AnalyseSSTUpdateComponent } from '../update/analyse-sst-update.component';
import { AnalyseSSTRoutingResolveService } from './analyse-sst-routing-resolve.service';

const analyseSSTRoute: Routes = [
  {
    path: '',
    component: AnalyseSSTComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnalyseSSTDetailComponent,
    resolve: {
      analyseSST: AnalyseSSTRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnalyseSSTUpdateComponent,
    resolve: {
      analyseSST: AnalyseSSTRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnalyseSSTUpdateComponent,
    resolve: {
      analyseSST: AnalyseSSTRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(analyseSSTRoute)],
  exports: [RouterModule],
})
export class AnalyseSSTRoutingModule {}
