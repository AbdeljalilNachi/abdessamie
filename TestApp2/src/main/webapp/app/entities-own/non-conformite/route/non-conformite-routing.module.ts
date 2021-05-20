import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NonConformiteComponent } from '../list/non-conformite.component';
import { NonConformiteDetailComponent } from '../detail/non-conformite-detail.component';
import { NonConformiteUpdateComponent } from '../update/non-conformite-update.component';
import { NonConformiteRoutingResolveService } from './non-conformite-routing-resolve.service';

const nonConformiteRoute: Routes = [
  {
    path: '',
    component: NonConformiteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NonConformiteDetailComponent,
    resolve: {
      nonConformite: NonConformiteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NonConformiteUpdateComponent,
    resolve: {
      nonConformite: NonConformiteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NonConformiteUpdateComponent,
    resolve: {
      nonConformite: NonConformiteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nonConformiteRoute)],
  exports: [RouterModule],
})
export class NonConformiteRoutingModule {}
