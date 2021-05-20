import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProcessusSMI, ProcessusSMI } from '../processus-smi.model';
import { ProcessusSMIService } from '../service/processus-smi.service';

@Injectable({ providedIn: 'root' })
export class ProcessusSMIRoutingResolveService implements Resolve<IProcessusSMI> {
  constructor(protected service: ProcessusSMIService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProcessusSMI> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((processusSMI: HttpResponse<ProcessusSMI>) => {
          if (processusSMI.body) {
            return of(processusSMI.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProcessusSMI());
  }
}
