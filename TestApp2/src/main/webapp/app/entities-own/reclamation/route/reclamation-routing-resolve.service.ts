import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReclamation, Reclamation } from '../reclamation.model';
import { ReclamationService } from '../service/reclamation.service';

@Injectable({ providedIn: 'root' })
export class ReclamationRoutingResolveService implements Resolve<IReclamation> {
  constructor(protected service: ReclamationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReclamation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((reclamation: HttpResponse<Reclamation>) => {
          if (reclamation.body) {
            return of(reclamation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Reclamation());
  }
}
