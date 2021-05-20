import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ObligationConformiteComponent } from '../list/obligation-conformite.component';
import { ObligationConformiteDetailComponent } from '../detail/obligation-conformite-detail.component';
import { ObligationConformiteUpdateComponent } from '../update/obligation-conformite-update.component';
import { ObligationConformiteRoutingResolveService } from './obligation-conformite-routing-resolve.service';

const obligationConformiteRoute: Routes = [
  {
    path: '',
    component: ObligationConformiteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ObligationConformiteDetailComponent,
    resolve: {
      obligationConformite: ObligationConformiteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ObligationConformiteUpdateComponent,
    resolve: {
      obligationConformite: ObligationConformiteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ObligationConformiteUpdateComponent,
    resolve: {
      obligationConformite: ObligationConformiteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(obligationConformiteRoute)],
  exports: [RouterModule],
})
export class ObligationConformiteRoutingModule {}
