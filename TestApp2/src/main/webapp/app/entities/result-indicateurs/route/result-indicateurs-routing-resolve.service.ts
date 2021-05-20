import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResultIndicateurs, ResultIndicateurs } from '../result-indicateurs.model';
import { ResultIndicateursService } from '../service/result-indicateurs.service';

@Injectable({ providedIn: 'root' })
export class ResultIndicateursRoutingResolveService implements Resolve<IResultIndicateurs> {
  constructor(protected service: ResultIndicateursService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResultIndicateurs> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((resultIndicateurs: HttpResponse<ResultIndicateurs>) => {
          if (resultIndicateurs.body) {
            return of(resultIndicateurs.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ResultIndicateurs());
  }
}
