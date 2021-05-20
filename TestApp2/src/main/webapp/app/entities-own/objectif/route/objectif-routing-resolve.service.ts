import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IObjectif, Objectif } from '../objectif.model';
import { ObjectifService } from '../service/objectif.service';

@Injectable({ providedIn: 'root' })
export class ObjectifRoutingResolveService implements Resolve<IObjectif> {
  constructor(protected service: ObjectifService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IObjectif> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((objectif: HttpResponse<Objectif>) => {
          if (objectif.body) {
            return of(objectif.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Objectif());
  }
}
