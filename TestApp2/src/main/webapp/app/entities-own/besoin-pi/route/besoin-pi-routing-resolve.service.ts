import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBesoinPI, BesoinPI } from '../besoin-pi.model';
import { BesoinPIService } from '../service/besoin-pi.service';

@Injectable({ providedIn: 'root' })
export class BesoinPIRoutingResolveService implements Resolve<IBesoinPI> {
  constructor(protected service: BesoinPIService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBesoinPI> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((besoinPI: HttpResponse<BesoinPI>) => {
          if (besoinPI.body) {
            return of(besoinPI.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BesoinPI());
  }
}
