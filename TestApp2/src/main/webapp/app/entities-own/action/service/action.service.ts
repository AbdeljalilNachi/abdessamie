import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAction, getActionIdentifier } from '../action.model';

export type EntityResponseType = HttpResponse<IAction>;
export type EntityArrayResponseType = HttpResponse<IAction[]>;

@Injectable({ providedIn: 'root' })
export class ActionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/actions');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/actions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(action: IAction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(action);
    return this.http
      .post<IAction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(action: IAction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(action);
    return this.http
      .put<IAction>(`${this.resourceUrl}/${getActionIdentifier(action) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(action: IAction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(action);
    return this.http
      .patch<IAction>(`${this.resourceUrl}/${getActionIdentifier(action) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAction[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addActionToCollectionIfMissing(actionCollection: IAction[], ...actionsToCheck: (IAction | null | undefined)[]): IAction[] {
    const actions: IAction[] = actionsToCheck.filter(isPresent);
    if (actions.length > 0) {
      const actionCollectionIdentifiers = actionCollection.map(actionItem => getActionIdentifier(actionItem)!);
      const actionsToAdd = actions.filter(actionItem => {
        const actionIdentifier = getActionIdentifier(actionItem);
        if (actionIdentifier == null || actionCollectionIdentifiers.includes(actionIdentifier)) {
          return false;
        }
        actionCollectionIdentifiers.push(actionIdentifier);
        return true;
      });
      return [...actionsToAdd, ...actionCollection];
    }
    return actionCollection;
  }

  protected convertDateFromClient(action: IAction): IAction {
    return Object.assign({}, action, {
      datePlanification: action.datePlanification?.isValid() ? action.datePlanification.format(DATE_FORMAT) : undefined,
      delai: action.delai?.isValid() ? action.delai.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.datePlanification = res.body.datePlanification ? dayjs(res.body.datePlanification) : undefined;
      res.body.delai = res.body.delai ? dayjs(res.body.delai) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((action: IAction) => {
        action.datePlanification = action.datePlanification ? dayjs(action.datePlanification) : undefined;
        action.delai = action.delai ? dayjs(action.delai) : undefined;
      });
    }
    return res;
  }
}
