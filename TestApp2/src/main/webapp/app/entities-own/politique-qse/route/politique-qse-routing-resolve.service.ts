import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPolitiqueQSE, PolitiqueQSE } from '../politique-qse.model';
import { PolitiqueQSEService } from '../service/politique-qse.service';

@Injectable({ providedIn: 'root' })
export class PolitiqueQSERoutingResolveService implements Resolve<IPolitiqueQSE> {
  constructor(protected service: PolitiqueQSEService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPolitiqueQSE> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((politiqueQSE: HttpResponse<PolitiqueQSE>) => {
          if (politiqueQSE.body) {
            return of(politiqueQSE.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PolitiqueQSE());
  }
}
