import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResultatIndicateur, ResultatIndicateur } from '../resultat-indicateur.model';
import { ResultatIndicateurService } from '../service/resultat-indicateur.service';

@Injectable({ providedIn: 'root' })
export class ResultatIndicateurRoutingResolveService implements Resolve<IResultatIndicateur> {
  constructor(protected service: ResultatIndicateurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResultatIndicateur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((resultatIndicateur: HttpResponse<ResultatIndicateur>) => {
          if (resultatIndicateur.body) {
            return of(resultatIndicateur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ResultatIndicateur());
  }
}
