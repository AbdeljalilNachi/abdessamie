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
import { IAnalyseSWOT, getAnalyseSWOTIdentifier } from '../analyse-swot.model';

export type EntityResponseType = HttpResponse<IAnalyseSWOT>;
export type EntityArrayResponseType = HttpResponse<IAnalyseSWOT[]>;

@Injectable({ providedIn: 'root' })
export class AnalyseSWOTService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/analyse-swots');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/analyse-swots');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(analyseSWOT: IAnalyseSWOT): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(analyseSWOT);
    return this.http
      .post<IAnalyseSWOT>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(analyseSWOT: IAnalyseSWOT): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(analyseSWOT);
    return this.http
      .put<IAnalyseSWOT>(`${this.resourceUrl}/${getAnalyseSWOTIdentifier(analyseSWOT) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(analyseSWOT: IAnalyseSWOT): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(analyseSWOT);
    return this.http
      .patch<IAnalyseSWOT>(`${this.resourceUrl}/${getAnalyseSWOTIdentifier(analyseSWOT) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAnalyseSWOT>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnalyseSWOT[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnalyseSWOT[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAnalyseSWOTToCollectionIfMissing(
    analyseSWOTCollection: IAnalyseSWOT[],
    ...analyseSWOTSToCheck: (IAnalyseSWOT | null | undefined)[]
  ): IAnalyseSWOT[] {
    const analyseSWOTS: IAnalyseSWOT[] = analyseSWOTSToCheck.filter(isPresent);
    if (analyseSWOTS.length > 0) {
      const analyseSWOTCollectionIdentifiers = analyseSWOTCollection.map(analyseSWOTItem => getAnalyseSWOTIdentifier(analyseSWOTItem)!);
      const analyseSWOTSToAdd = analyseSWOTS.filter(analyseSWOTItem => {
        const analyseSWOTIdentifier = getAnalyseSWOTIdentifier(analyseSWOTItem);
        if (analyseSWOTIdentifier == null || analyseSWOTCollectionIdentifiers.includes(analyseSWOTIdentifier)) {
          return false;
        }
        analyseSWOTCollectionIdentifiers.push(analyseSWOTIdentifier);
        return true;
      });
      return [...analyseSWOTSToAdd, ...analyseSWOTCollection];
    }
    return analyseSWOTCollection;
  }

  protected convertDateFromClient(analyseSWOT: IAnalyseSWOT): IAnalyseSWOT {
    return Object.assign({}, analyseSWOT, {
      dateIdentification: analyseSWOT.dateIdentification?.isValid() ? analyseSWOT.dateIdentification.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateIdentification = res.body.dateIdentification ? dayjs(res.body.dateIdentification) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((analyseSWOT: IAnalyseSWOT) => {
        analyseSWOT.dateIdentification = analyseSWOT.dateIdentification ? dayjs(analyseSWOT.dateIdentification) : undefined;
      });
    }
    return res;
  }
}
