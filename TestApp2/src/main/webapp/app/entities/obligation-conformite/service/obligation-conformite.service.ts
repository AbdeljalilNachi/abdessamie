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
import { IObligationConformite, getObligationConformiteIdentifier } from '../obligation-conformite.model';

export type EntityResponseType = HttpResponse<IObligationConformite>;
export type EntityArrayResponseType = HttpResponse<IObligationConformite[]>;

@Injectable({ providedIn: 'root' })
export class ObligationConformiteService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/obligation-conformites');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/obligation-conformites');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(obligationConformite: IObligationConformite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(obligationConformite);
    return this.http
      .post<IObligationConformite>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(obligationConformite: IObligationConformite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(obligationConformite);
    return this.http
      .put<IObligationConformite>(`${this.resourceUrl}/${getObligationConformiteIdentifier(obligationConformite) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(obligationConformite: IObligationConformite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(obligationConformite);
    return this.http
      .patch<IObligationConformite>(`${this.resourceUrl}/${getObligationConformiteIdentifier(obligationConformite) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IObligationConformite>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IObligationConformite[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IObligationConformite[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addObligationConformiteToCollectionIfMissing(
    obligationConformiteCollection: IObligationConformite[],
    ...obligationConformitesToCheck: (IObligationConformite | null | undefined)[]
  ): IObligationConformite[] {
    const obligationConformites: IObligationConformite[] = obligationConformitesToCheck.filter(isPresent);
    if (obligationConformites.length > 0) {
      const obligationConformiteCollectionIdentifiers = obligationConformiteCollection.map(
        obligationConformiteItem => getObligationConformiteIdentifier(obligationConformiteItem)!
      );
      const obligationConformitesToAdd = obligationConformites.filter(obligationConformiteItem => {
        const obligationConformiteIdentifier = getObligationConformiteIdentifier(obligationConformiteItem);
        if (obligationConformiteIdentifier == null || obligationConformiteCollectionIdentifiers.includes(obligationConformiteIdentifier)) {
          return false;
        }
        obligationConformiteCollectionIdentifiers.push(obligationConformiteIdentifier);
        return true;
      });
      return [...obligationConformitesToAdd, ...obligationConformiteCollection];
    }
    return obligationConformiteCollection;
  }

  protected convertDateFromClient(obligationConformite: IObligationConformite): IObligationConformite {
    return Object.assign({}, obligationConformite, {
      date: obligationConformite.date?.isValid() ? obligationConformite.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((obligationConformite: IObligationConformite) => {
        obligationConformite.date = obligationConformite.date ? dayjs(obligationConformite.date) : undefined;
      });
    }
    return res;
  }
}
