import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProcessusSMIComponent } from '../list/processus-smi.component';
import { ProcessusSMIDetailComponent } from '../detail/processus-smi-detail.component';
import { ProcessusSMIUpdateComponent } from '../update/processus-smi-update.component';
import { ProcessusSMIRoutingResolveService } from './processus-smi-routing-resolve.service';

const processusSMIRoute: Routes = [
  {
    path: '',
    component: ProcessusSMIComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProcessusSMIDetailComponent,
    resolve: {
      processusSMI: ProcessusSMIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProcessusSMIUpdateComponent,
    resolve: {
      processusSMI: ProcessusSMIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProcessusSMIUpdateComponent,
    resolve: {
      processusSMI: ProcessusSMIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(processusSMIRoute)],
  exports: [RouterModule],
})
export class ProcessusSMIRoutingModule {}
