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
import { IAnalyseEnvirommentale, getAnalyseEnvirommentaleIdentifier } from '../analyse-envirommentale.model';

export type EntityResponseType = HttpResponse<IAnalyseEnvirommentale>;
export type EntityArrayResponseType = HttpResponse<IAnalyseEnvirommentale[]>;

@Injectable({ providedIn: 'root' })
export class AnalyseEnvirommentaleService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/analyse-envirommentales');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/analyse-envirommentales');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(analyseEnvirommentale: IAnalyseEnvirommentale): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(analyseEnvirommentale);
    return this.http
      .post<IAnalyseEnvirommentale>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(analyseEnvirommentale: IAnalyseEnvirommentale): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(analyseEnvirommentale);
    return this.http
      .put<IAnalyseEnvirommentale>(`${this.resourceUrl}/${getAnalyseEnvirommentaleIdentifier(analyseEnvirommentale) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(analyseEnvirommentale: IAnalyseEnvirommentale): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(analyseEnvirommentale);
    return this.http
      .patch<IAnalyseEnvirommentale>(`${this.resourceUrl}/${getAnalyseEnvirommentaleIdentifier(analyseEnvirommentale) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAnalyseEnvirommentale>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnalyseEnvirommentale[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnalyseEnvirommentale[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAnalyseEnvirommentaleToCollectionIfMissing(
    analyseEnvirommentaleCollection: IAnalyseEnvirommentale[],
    ...analyseEnvirommentalesToCheck: (IAnalyseEnvirommentale | null | undefined)[]
  ): IAnalyseEnvirommentale[] {
    const analyseEnvirommentales: IAnalyseEnvirommentale[] = analyseEnvirommentalesToCheck.filter(isPresent);
    if (analyseEnvirommentales.length > 0) {
      const analyseEnvirommentaleCollectionIdentifiers = analyseEnvirommentaleCollection.map(
        analyseEnvirommentaleItem => getAnalyseEnvirommentaleIdentifier(analyseEnvirommentaleItem)!
      );
      const analyseEnvirommentalesToAdd = analyseEnvirommentales.filter(analyseEnvirommentaleItem => {
        const analyseEnvirommentaleIdentifier = getAnalyseEnvirommentaleIdentifier(analyseEnvirommentaleItem);
        if (
          analyseEnvirommentaleIdentifier == null ||
          analyseEnvirommentaleCollectionIdentifiers.includes(analyseEnvirommentaleIdentifier)
        ) {
          return false;
        }
        analyseEnvirommentaleCollectionIdentifiers.push(analyseEnvirommentaleIdentifier);
        return true;
      });
      return [...analyseEnvirommentalesToAdd, ...analyseEnvirommentaleCollection];
    }
    return analyseEnvirommentaleCollection;
  }

  protected convertDateFromClient(analyseEnvirommentale: IAnalyseEnvirommentale): IAnalyseEnvirommentale {
    return Object.assign({}, analyseEnvirommentale, {
      date: analyseEnvirommentale.date?.isValid() ? analyseEnvirommentale.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((analyseEnvirommentale: IAnalyseEnvirommentale) => {
        analyseEnvirommentale.date = analyseEnvirommentale.date ? dayjs(analyseEnvirommentale.date) : undefined;
      });
    }
    return res;
  }
}
