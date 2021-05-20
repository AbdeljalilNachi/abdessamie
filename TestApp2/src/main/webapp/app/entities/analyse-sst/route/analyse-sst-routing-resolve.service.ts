import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAnalyseSST, AnalyseSST } from '../analyse-sst.model';
import { AnalyseSSTService } from '../service/analyse-sst.service';

@Injectable({ providedIn: 'root' })
export class AnalyseSSTRoutingResolveService implements Resolve<IAnalyseSST> {
  constructor(protected service: AnalyseSSTService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAnalyseSST> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((analyseSST: HttpResponse<AnalyseSST>) => {
          if (analyseSST.body) {
            return of(analyseSST.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AnalyseSST());
  }
}
