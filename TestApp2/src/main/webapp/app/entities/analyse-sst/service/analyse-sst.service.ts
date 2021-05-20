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
import { IAnalyseSST, getAnalyseSSTIdentifier } from '../analyse-sst.model';

export type EntityResponseType = HttpResponse<IAnalyseSST>;
export type EntityArrayResponseType = HttpResponse<IAnalyseSST[]>;

@Injectable({ providedIn: 'root' })
export class AnalyseSSTService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/analyse-ssts');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/analyse-ssts');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(analyseSST: IAnalyseSST): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(analyseSST);
    return this.http
      .post<IAnalyseSST>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(analyseSST: IAnalyseSST): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(analyseSST);
    return this.http
      .put<IAnalyseSST>(`${this.resourceUrl}/${getAnalyseSSTIdentifier(analyseSST) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(analyseSST: IAnalyseSST): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(analyseSST);
    return this.http
      .patch<IAnalyseSST>(`${this.resourceUrl}/${getAnalyseSSTIdentifier(analyseSST) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAnalyseSST>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnalyseSST[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnalyseSST[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAnalyseSSTToCollectionIfMissing(
    analyseSSTCollection: IAnalyseSST[],
    ...analyseSSTSToCheck: (IAnalyseSST | null | undefined)[]
  ): IAnalyseSST[] {
    const analyseSSTS: IAnalyseSST[] = analyseSSTSToCheck.filter(isPresent);
    if (analyseSSTS.length > 0) {
      const analyseSSTCollectionIdentifiers = analyseSSTCollection.map(analyseSSTItem => getAnalyseSSTIdentifier(analyseSSTItem)!);
      const analyseSSTSToAdd = analyseSSTS.filter(analyseSSTItem => {
        const analyseSSTIdentifier = getAnalyseSSTIdentifier(analyseSSTItem);
        if (analyseSSTIdentifier == null || analyseSSTCollectionIdentifiers.includes(analyseSSTIdentifier)) {
          return false;
        }
        analyseSSTCollectionIdentifiers.push(analyseSSTIdentifier);
        return true;
      });
      return [...analyseSSTSToAdd, ...analyseSSTCollection];
    }
    return analyseSSTCollection;
  }

  protected convertDateFromClient(analyseSST: IAnalyseSST): IAnalyseSST {
    return Object.assign({}, analyseSST, {
      date: analyseSST.date?.isValid() ? analyseSST.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((analyseSST: IAnalyseSST) => {
        analyseSST.date = analyseSST.date ? dayjs(analyseSST.date) : undefined;
      });
    }
    return res;
  }
}
