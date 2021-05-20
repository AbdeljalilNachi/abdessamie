import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlanificationRDD, PlanificationRDD } from '../planification-rdd.model';
import { PlanificationRDDService } from '../service/planification-rdd.service';

@Injectable({ providedIn: 'root' })
export class PlanificationRDDRoutingResolveService implements Resolve<IPlanificationRDD> {
  constructor(protected service: PlanificationRDDService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlanificationRDD> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((planificationRDD: HttpResponse<PlanificationRDD>) => {
          if (planificationRDD.body) {
            return of(planificationRDD.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PlanificationRDD());
  }
}
