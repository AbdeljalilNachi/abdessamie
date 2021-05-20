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
import { IRisque, getRisqueIdentifier } from '../risque.model';

export type EntityResponseType = HttpResponse<IRisque>;
export type EntityArrayResponseType = HttpResponse<IRisque[]>;

@Injectable({ providedIn: 'root' })
export class RisqueService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/risques');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/risques');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(risque: IRisque): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(risque);
    return this.http
      .post<IRisque>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(risque: IRisque): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(risque);
    return this.http
      .put<IRisque>(`${this.resourceUrl}/${getRisqueIdentifier(risque) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(risque: IRisque): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(risque);
    return this.http
      .patch<IRisque>(`${this.resourceUrl}/${getRisqueIdentifier(risque) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRisque>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRisque[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRisque[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addRisqueToCollectionIfMissing(risqueCollection: IRisque[], ...risquesToCheck: (IRisque | null | undefined)[]): IRisque[] {
    const risques: IRisque[] = risquesToCheck.filter(isPresent);
    if (risques.length > 0) {
      const risqueCollectionIdentifiers = risqueCollection.map(risqueItem => getRisqueIdentifier(risqueItem)!);
      const risquesToAdd = risques.filter(risqueItem => {
        const risqueIdentifier = getRisqueIdentifier(risqueItem);
        if (risqueIdentifier == null || risqueCollectionIdentifiers.includes(risqueIdentifier)) {
          return false;
        }
        risqueCollectionIdentifiers.push(risqueIdentifier);
        return true;
      });
      return [...risquesToAdd, ...risqueCollection];
    }
    return risqueCollection;
  }

  protected convertDateFromClient(risque: IRisque): IRisque {
    return Object.assign({}, risque, {
      dateIdentification: risque.dateIdentification?.isValid() ? risque.dateIdentification.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((risque: IRisque) => {
        risque.dateIdentification = risque.dateIdentification ? dayjs(risque.dateIdentification) : undefined;
      });
    }
    return res;
  }
}
