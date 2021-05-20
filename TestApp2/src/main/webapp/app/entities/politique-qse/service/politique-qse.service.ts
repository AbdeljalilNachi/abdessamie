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
import { IPolitiqueQSE, getPolitiqueQSEIdentifier } from '../politique-qse.model';

export type EntityResponseType = HttpResponse<IPolitiqueQSE>;
export type EntityArrayResponseType = HttpResponse<IPolitiqueQSE[]>;

@Injectable({ providedIn: 'root' })
export class PolitiqueQSEService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/politique-qses');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/politique-qses');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(politiqueQSE: IPolitiqueQSE): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(politiqueQSE);
    return this.http
      .post<IPolitiqueQSE>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(politiqueQSE: IPolitiqueQSE): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(politiqueQSE);
    return this.http
      .put<IPolitiqueQSE>(`${this.resourceUrl}/${getPolitiqueQSEIdentifier(politiqueQSE) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(politiqueQSE: IPolitiqueQSE): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(politiqueQSE);
    return this.http
      .patch<IPolitiqueQSE>(`${this.resourceUrl}/${getPolitiqueQSEIdentifier(politiqueQSE) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPolitiqueQSE>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPolitiqueQSE[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPolitiqueQSE[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPolitiqueQSEToCollectionIfMissing(
    politiqueQSECollection: IPolitiqueQSE[],
    ...politiqueQSESToCheck: (IPolitiqueQSE | null | undefined)[]
  ): IPolitiqueQSE[] {
    const politiqueQSES: IPolitiqueQSE[] = politiqueQSESToCheck.filter(isPresent);
    if (politiqueQSES.length > 0) {
      const politiqueQSECollectionIdentifiers = politiqueQSECollection.map(
        politiqueQSEItem => getPolitiqueQSEIdentifier(politiqueQSEItem)!
      );
      const politiqueQSESToAdd = politiqueQSES.filter(politiqueQSEItem => {
        const politiqueQSEIdentifier = getPolitiqueQSEIdentifier(politiqueQSEItem);
        if (politiqueQSEIdentifier == null || politiqueQSECollectionIdentifiers.includes(politiqueQSEIdentifier)) {
          return false;
        }
        politiqueQSECollectionIdentifiers.push(politiqueQSEIdentifier);
        return true;
      });
      return [...politiqueQSESToAdd, ...politiqueQSECollection];
    }
    return politiqueQSECollection;
  }

  protected convertDateFromClient(politiqueQSE: IPolitiqueQSE): IPolitiqueQSE {
    return Object.assign({}, politiqueQSE, {
      date: politiqueQSE.date?.isValid() ? politiqueQSE.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((politiqueQSE: IPolitiqueQSE) => {
        politiqueQSE.date = politiqueQSE.date ? dayjs(politiqueQSE.date) : undefined;
      });
    }
    return res;
  }
}
