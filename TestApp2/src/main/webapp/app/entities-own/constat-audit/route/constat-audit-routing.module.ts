import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConstatAuditComponent } from '../list/constat-audit.component';
import { ConstatAuditDetailComponent } from '../detail/constat-audit-detail.component';
import { ConstatAuditUpdateComponent } from '../update/constat-audit-update.component';
import { ConstatAuditRoutingResolveService } from './constat-audit-routing-resolve.service';

const constatAuditRoute: Routes = [
  {
    path: '',
    component: ConstatAuditComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConstatAuditDetailComponent,
    resolve: {
      constatAudit: ConstatAuditRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConstatAuditUpdateComponent,
    resolve: {
      constatAudit: ConstatAuditRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConstatAuditUpdateComponent,
    resolve: {
      constatAudit: ConstatAuditRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(constatAuditRoute)],
  exports: [RouterModule],
})
export class ConstatAuditRoutingModule {}
