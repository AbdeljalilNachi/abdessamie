import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAutreAction, getAutreActionIdentifier } from '../autre-action.model';

export type EntityResponseType = HttpResponse<IAutreAction>;
export type EntityArrayResponseType = HttpResponse<IAutreAction[]>;

@Injectable({ providedIn: 'root' })
export class AutreActionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/autre-actions');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/autre-actions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(autreAction: IAutreAction): Observable<EntityResponseType> {
    return this.http.post<IAutreAction>(this.resourceUrl, autreAction, { observe: 'response' });
  }

  update(autreAction: IAutreAction): Observable<EntityResponseType> {
    return this.http.put<IAutreAction>(`${this.resourceUrl}/${getAutreActionIdentifier(autreAction) as number}`, autreAction, {
      observe: 'response',
    });
  }

  partialUpdate(autreAction: IAutreAction): Observable<EntityResponseType> {
    return this.http.patch<IAutreAction>(`${this.resourceUrl}/${getAutreActionIdentifier(autreAction) as number}`, autreAction, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAutreAction>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAutreAction[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAutreAction[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addAutreActionToCollectionIfMissing(
    autreActionCollection: IAutreAction[],
    ...autreActionsToCheck: (IAutreAction | null | undefined)[]
  ): IAutreAction[] {
    const autreActions: IAutreAction[] = autreActionsToCheck.filter(isPresent);
    if (autreActions.length > 0) {
      const autreActionCollectionIdentifiers = autreActionCollection.map(autreActionItem => getAutreActionIdentifier(autreActionItem)!);
      const autreActionsToAdd = autreActions.filter(autreActionItem => {
        const autreActionIdentifier = getAutreActionIdentifier(autreActionItem);
        if (autreActionIdentifier == null || autreActionCollectionIdentifiers.includes(autreActionIdentifier)) {
          return false;
        }
        autreActionCollectionIdentifiers.push(autreActionIdentifier);
        return true;
      });
      return [...autreActionsToAdd, ...autreActionCollection];
    }
    return autreActionCollection;
  }
}
