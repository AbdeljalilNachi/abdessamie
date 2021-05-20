import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AutreActionComponent } from '../list/autre-action.component';
import { AutreActionDetailComponent } from '../detail/autre-action-detail.component';
import { AutreActionUpdateComponent } from '../update/autre-action-update.component';
import { AutreActionRoutingResolveService } from './autre-action-routing-resolve.service';

const autreActionRoute: Routes = [
  {
    path: '',
    component: AutreActionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AutreActionDetailComponent,
    resolve: {
      autreAction: AutreActionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AutreActionUpdateComponent,
    resolve: {
      autreAction: AutreActionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AutreActionUpdateComponent,
    resolve: {
      autreAction: AutreActionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(autreActionRoute)],
  exports: [RouterModule],
})
export class AutreActionRoutingModule {}
