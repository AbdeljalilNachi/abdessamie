import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIndicateurSMI, IndicateurSMI } from '../indicateur-smi.model';
import { IndicateurSMIService } from '../service/indicateur-smi.service';

@Injectable({ providedIn: 'root' })
export class IndicateurSMIRoutingResolveService implements Resolve<IIndicateurSMI> {
  constructor(protected service: IndicateurSMIService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIndicateurSMI> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((indicateurSMI: HttpResponse<IndicateurSMI>) => {
          if (indicateurSMI.body) {
            return of(indicateurSMI.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IndicateurSMI());
  }
}
