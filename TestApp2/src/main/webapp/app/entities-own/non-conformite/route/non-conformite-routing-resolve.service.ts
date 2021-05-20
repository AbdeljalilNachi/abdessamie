import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INonConformite, NonConformite } from '../non-conformite.model';
import { NonConformiteService } from '../service/non-conformite.service';

@Injectable({ providedIn: 'root' })
export class NonConformiteRoutingResolveService implements Resolve<INonConformite> {
  constructor(protected service: NonConformiteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INonConformite> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((nonConformite: HttpResponse<NonConformite>) => {
          if (nonConformite.body) {
            return of(nonConformite.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NonConformite());
  }
}
