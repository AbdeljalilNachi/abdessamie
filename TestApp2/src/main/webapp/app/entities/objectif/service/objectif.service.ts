import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IObjectif, getObjectifIdentifier } from '../objectif.model';

export type EntityResponseType = HttpResponse<IObjectif>;
export type EntityArrayResponseType = HttpResponse<IObjectif[]>;

@Injectable({ providedIn: 'root' })
export class ObjectifService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/objectifs');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/objectifs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(objectif: IObjectif): Observable<EntityResponseType> {
    return this.http.post<IObjectif>(this.resourceUrl, objectif, { observe: 'response' });
  }

  update(objectif: IObjectif): Observable<EntityResponseType> {
    return this.http.put<IObjectif>(`${this.resourceUrl}/${getObjectifIdentifier(objectif) as number}`, objectif, { observe: 'response' });
  }

  partialUpdate(objectif: IObjectif): Observable<EntityResponseType> {
    return this.http.patch<IObjectif>(`${this.resourceUrl}/${getObjectifIdentifier(objectif) as number}`, objectif, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IObjectif>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IObjectif[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IObjectif[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addObjectifToCollectionIfMissing(objectifCollection: IObjectif[], ...objectifsToCheck: (IObjectif | null | undefined)[]): IObjectif[] {
    const objectifs: IObjectif[] = objectifsToCheck.filter(isPresent);
    if (objectifs.length > 0) {
      const objectifCollectionIdentifiers = objectifCollection.map(objectifItem => getObjectifIdentifier(objectifItem)!);
      const objectifsToAdd = objectifs.filter(objectifItem => {
        const objectifIdentifier = getObjectifIdentifier(objectifItem);
        if (objectifIdentifier == null || objectifCollectionIdentifiers.includes(objectifIdentifier)) {
          return false;
        }
        objectifCollectionIdentifiers.push(objectifIdentifier);
        return true;
      });
      return [...objectifsToAdd, ...objectifCollection];
    }
    return objectifCollection;
  }
}
