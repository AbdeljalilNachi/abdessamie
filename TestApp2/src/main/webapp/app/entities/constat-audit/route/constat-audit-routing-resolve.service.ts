import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConstatAudit, ConstatAudit } from '../constat-audit.model';
import { ConstatAuditService } from '../service/constat-audit.service';

@Injectable({ providedIn: 'root' })
export class ConstatAuditRoutingResolveService implements Resolve<IConstatAudit> {
  constructor(protected service: ConstatAuditService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConstatAudit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((constatAudit: HttpResponse<ConstatAudit>) => {
          if (constatAudit.body) {
            return of(constatAudit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ConstatAudit());
  }
}
