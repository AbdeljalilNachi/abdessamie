import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReclamationComponent } from '../list/reclamation.component';
import { ReclamationDetailComponent } from '../detail/reclamation-detail.component';
import { ReclamationUpdateComponent } from '../update/reclamation-update.component';
import { ReclamationRoutingResolveService } from './reclamation-routing-resolve.service';

const reclamationRoute: Routes = [
  {
    path: '',
    component: ReclamationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReclamationDetailComponent,
    resolve: {
      reclamation: ReclamationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReclamationUpdateComponent,
    resolve: {
      reclamation: ReclamationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReclamationUpdateComponent,
    resolve: {
      reclamation: ReclamationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reclamationRoute)],
  exports: [RouterModule],
})
export class ReclamationRoutingModule {}
