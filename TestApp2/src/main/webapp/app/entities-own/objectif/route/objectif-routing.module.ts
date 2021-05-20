import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ObjectifComponent } from '../list/objectif.component';
import { ObjectifDetailComponent } from '../detail/objectif-detail.component';
import { ObjectifUpdateComponent } from '../update/objectif-update.component';
import { ObjectifRoutingResolveService } from './objectif-routing-resolve.service';

const objectifRoute: Routes = [
  {
    path: '',
    component: ObjectifComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ObjectifDetailComponent,
    resolve: {
      objectif: ObjectifRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ObjectifUpdateComponent,
    resolve: {
      objectif: ObjectifRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ObjectifUpdateComponent,
    resolve: {
      objectif: ObjectifRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(objectifRoute)],
  exports: [RouterModule],
})
export class ObjectifRoutingModule {}
