import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAnalyseEnvirommentale, AnalyseEnvirommentale } from '../analyse-envirommentale.model';
import { AnalyseEnvirommentaleService } from '../service/analyse-envirommentale.service';

@Injectable({ providedIn: 'root' })
export class AnalyseEnvirommentaleRoutingResolveService implements Resolve<IAnalyseEnvirommentale> {
  constructor(protected service: AnalyseEnvirommentaleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAnalyseEnvirommentale> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((analyseEnvirommentale: HttpResponse<AnalyseEnvirommentale>) => {
          if (analyseEnvirommentale.body) {
            return of(analyseEnvirommentale.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AnalyseEnvirommentale());
  }
}
