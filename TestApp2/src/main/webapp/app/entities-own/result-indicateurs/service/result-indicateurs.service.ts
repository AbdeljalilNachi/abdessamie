import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IResultIndicateurs, getResultIndicateursIdentifier } from '../result-indicateurs.model';

export type EntityResponseType = HttpResponse<IResultIndicateurs>;
export type EntityArrayResponseType = HttpResponse<IResultIndicateurs[]>;

@Injectable({ providedIn: 'root' })
export class ResultIndicateursService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/result-indicateurs');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/result-indicateurs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(resultIndicateurs: IResultIndicateurs): Observable<EntityResponseType> {
    return this.http.post<IResultIndicateurs>(this.resourceUrl, resultIndicateurs, { observe: 'response' });
  }

  update(resultIndicateurs: IResultIndicateurs): Observable<EntityResponseType> {
    return this.http.put<IResultIndicateurs>(
      `${this.resourceUrl}/${getResultIndicateursIdentifier(resultIndicateurs) as number}`,
      resultIndicateurs,
      { observe: 'response' }
    );
  }

  partialUpdate(resultIndicateurs: IResultIndicateurs): Observable<EntityResponseType> {
    return this.http.patch<IResultIndicateurs>(
      `${this.resourceUrl}/${getResultIndicateursIdentifier(resultIndicateurs) as number}`,
      resultIndicateurs,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResultIndicateurs>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResultIndicateurs[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResultIndicateurs[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addResultIndicateursToCollectionIfMissing(
    resultIndicateursCollection: IResultIndicateurs[],
    ...resultIndicateursToCheck: (IResultIndicateurs | null | undefined)[]
  ): IResultIndicateurs[] {
    const resultIndicateurs: IResultIndicateurs[] = resultIndicateursToCheck.filter(isPresent);
    if (resultIndicateurs.length > 0) {
      const resultIndicateursCollectionIdentifiers = resultIndicateursCollection.map(
        resultIndicateursItem => getResultIndicateursIdentifier(resultIndicateursItem)!
      );
      const resultIndicateursToAdd = resultIndicateurs.filter(resultIndicateursItem => {
        const resultIndicateursIdentifier = getResultIndicateursIdentifier(resultIndicateursItem);
        if (resultIndicateursIdentifier == null || resultIndicateursCollectionIdentifiers.includes(resultIndicateursIdentifier)) {
          return false;
        }
        resultIndicateursCollectionIdentifiers.push(resultIndicateursIdentifier);
        return true;
      });
      return [...resultIndicateursToAdd, ...resultIndicateursCollection];
    }
    return resultIndicateursCollection;
  }
}
