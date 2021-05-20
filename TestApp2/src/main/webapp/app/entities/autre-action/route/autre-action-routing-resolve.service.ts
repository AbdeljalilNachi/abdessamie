import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAutreAction, AutreAction } from '../autre-action.model';
import { AutreActionService } from '../service/autre-action.service';

@Injectable({ providedIn: 'root' })
export class AutreActionRoutingResolveService implements Resolve<IAutreAction> {
  constructor(protected service: AutreActionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAutreAction> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((autreAction: HttpResponse<AutreAction>) => {
          if (autreAction.body) {
            return of(autreAction.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AutreAction());
  }
}
