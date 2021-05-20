import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ResultIndicateursComponent } from '../list/result-indicateurs.component';
import { ResultIndicateursDetailComponent } from '../detail/result-indicateurs-detail.component';
import { ResultIndicateursUpdateComponent } from '../update/result-indicateurs-update.component';
import { ResultIndicateursRoutingResolveService } from './result-indicateurs-routing-resolve.service';

const resultIndicateursRoute: Routes = [
  {
    path: '',
    component: ResultIndicateursComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResultIndicateursDetailComponent,
    resolve: {
      resultIndicateurs: ResultIndicateursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResultIndicateursUpdateComponent,
    resolve: {
      resultIndicateurs: ResultIndicateursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResultIndicateursUpdateComponent,
    resolve: {
      resultIndicateurs: ResultIndicateursRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(resultIndicateursRoute)],
  exports: [RouterModule],
})
export class ResultIndicateursRoutingModule {}
