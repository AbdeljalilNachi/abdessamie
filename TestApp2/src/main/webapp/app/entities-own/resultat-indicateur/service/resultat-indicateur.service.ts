import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IResultatIndicateur, getResultatIndicateurIdentifier } from '../resultat-indicateur.model';

export type EntityResponseType = HttpResponse<IResultatIndicateur>;
export type EntityArrayResponseType = HttpResponse<IResultatIndicateur[]>;

@Injectable({ providedIn: 'root' })
export class ResultatIndicateurService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/resultat-indicateurs');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/resultat-indicateurs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(resultatIndicateur: IResultatIndicateur): Observable<EntityResponseType> {
    return this.http.post<IResultatIndicateur>(this.resourceUrl, resultatIndicateur, { observe: 'response' });
  }

  update(resultatIndicateur: IResultatIndicateur): Observable<EntityResponseType> {
    return this.http.put<IResultatIndicateur>(
      `${this.resourceUrl}/${getResultatIndicateurIdentifier(resultatIndicateur) as number}`,
      resultatIndicateur,
      { observe: 'response' }
    );
  }

  partialUpdate(resultatIndicateur: IResultatIndicateur): Observable<EntityResponseType> {
    return this.http.patch<IResultatIndicateur>(
      `${this.resourceUrl}/${getResultatIndicateurIdentifier(resultatIndicateur) as number}`,
      resultatIndicateur,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResultatIndicateur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResultatIndicateur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResultatIndicateur[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addResultatIndicateurToCollectionIfMissing(
    resultatIndicateurCollection: IResultatIndicateur[],
    ...resultatIndicateursToCheck: (IResultatIndicateur | null | undefined)[]
  ): IResultatIndicateur[] {
    const resultatIndicateurs: IResultatIndicateur[] = resultatIndicateursToCheck.filter(isPresent);
    if (resultatIndicateurs.length > 0) {
      const resultatIndicateurCollectionIdentifiers = resultatIndicateurCollection.map(
        resultatIndicateurItem => getResultatIndicateurIdentifier(resultatIndicateurItem)!
      );
      const resultatIndicateursToAdd = resultatIndicateurs.filter(resultatIndicateurItem => {
        const resultatIndicateurIdentifier = getResultatIndicateurIdentifier(resultatIndicateurItem);
        if (resultatIndicateurIdentifier == null || resultatIndicateurCollectionIdentifiers.includes(resultatIndicateurIdentifier)) {
          return false;
        }
        resultatIndicateurCollectionIdentifiers.push(resultatIndicateurIdentifier);
        return true;
      });
      return [...resultatIndicateursToAdd, ...resultatIndicateurCollection];
    }
    return resultatIndicateurCollection;
  }
}
