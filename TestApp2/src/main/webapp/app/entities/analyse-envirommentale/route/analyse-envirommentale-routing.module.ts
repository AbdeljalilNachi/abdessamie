import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AnalyseEnvirommentaleComponent } from '../list/analyse-envirommentale.component';
import { AnalyseEnvirommentaleDetailComponent } from '../detail/analyse-envirommentale-detail.component';
import { AnalyseEnvirommentaleUpdateComponent } from '../update/analyse-envirommentale-update.component';
import { AnalyseEnvirommentaleRoutingResolveService } from './analyse-envirommentale-routing-resolve.service';

const analyseEnvirommentaleRoute: Routes = [
  {
    path: '',
    component: AnalyseEnvirommentaleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnalyseEnvirommentaleDetailComponent,
    resolve: {
      analyseEnvirommentale: AnalyseEnvirommentaleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnalyseEnvirommentaleUpdateComponent,
    resolve: {
      analyseEnvirommentale: AnalyseEnvirommentaleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnalyseEnvirommentaleUpdateComponent,
    resolve: {
      analyseEnvirommentale: AnalyseEnvirommentaleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(analyseEnvirommentaleRoute)],
  exports: [RouterModule],
})
export class AnalyseEnvirommentaleRoutingModule {}
