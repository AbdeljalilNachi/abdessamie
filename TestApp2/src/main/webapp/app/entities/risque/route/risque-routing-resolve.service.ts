import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRisque, Risque } from '../risque.model';
import { RisqueService } from '../service/risque.service';

@Injectable({ providedIn: 'root' })
export class RisqueRoutingResolveService implements Resolve<IRisque> {
  constructor(protected service: RisqueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRisque> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((risque: HttpResponse<Risque>) => {
          if (risque.body) {
            return of(risque.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Risque());
  }
}
