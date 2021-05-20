import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PolitiqueQSEComponent } from '../list/politique-qse.component';
import { PolitiqueQSEDetailComponent } from '../detail/politique-qse-detail.component';
import { PolitiqueQSEUpdateComponent } from '../update/politique-qse-update.component';
import { PolitiqueQSERoutingResolveService } from './politique-qse-routing-resolve.service';

const politiqueQSERoute: Routes = [
  {
    path: '',
    component: PolitiqueQSEComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PolitiqueQSEDetailComponent,
    resolve: {
      politiqueQSE: PolitiqueQSERoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PolitiqueQSEUpdateComponent,
    resolve: {
      politiqueQSE: PolitiqueQSERoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PolitiqueQSEUpdateComponent,
    resolve: {
      politiqueQSE: PolitiqueQSERoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(politiqueQSERoute)],
  exports: [RouterModule],
})
export class PolitiqueQSERoutingModule {}
