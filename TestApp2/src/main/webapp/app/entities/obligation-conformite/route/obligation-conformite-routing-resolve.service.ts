import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IObligationConformite, ObligationConformite } from '../obligation-conformite.model';
import { ObligationConformiteService } from '../service/obligation-conformite.service';

@Injectable({ providedIn: 'root' })
export class ObligationConformiteRoutingResolveService implements Resolve<IObligationConformite> {
  constructor(protected service: ObligationConformiteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IObligationConformite> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((obligationConformite: HttpResponse<ObligationConformite>) => {
          if (obligationConformite.body) {
            return of(obligationConformite.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ObligationConformite());
  }
}
