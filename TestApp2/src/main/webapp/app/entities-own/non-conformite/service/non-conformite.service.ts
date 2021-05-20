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
import { INonConformite, getNonConformiteIdentifier } from '../non-conformite.model';

export type EntityResponseType = HttpResponse<INonConformite>;
export type EntityArrayResponseType = HttpResponse<INonConformite[]>;

@Injectable({ providedIn: 'root' })
export class NonConformiteService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/non-conformites');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/non-conformites');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(nonConformite: INonConformite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nonConformite);
    return this.http
      .post<INonConformite>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(nonConformite: INonConformite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nonConformite);
    return this.http
      .put<INonConformite>(`${this.resourceUrl}/${getNonConformiteIdentifier(nonConformite) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(nonConformite: INonConformite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nonConformite);
    return this.http
      .patch<INonConformite>(`${this.resourceUrl}/${getNonConformiteIdentifier(nonConformite) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<INonConformite>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INonConformite[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INonConformite[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addNonConformiteToCollectionIfMissing(
    nonConformiteCollection: INonConformite[],
    ...nonConformitesToCheck: (INonConformite | null | undefined)[]
  ): INonConformite[] {
    const nonConformites: INonConformite[] = nonConformitesToCheck.filter(isPresent);
    if (nonConformites.length > 0) {
      const nonConformiteCollectionIdentifiers = nonConformiteCollection.map(
        nonConformiteItem => getNonConformiteIdentifier(nonConformiteItem)!
      );
      const nonConformitesToAdd = nonConformites.filter(nonConformiteItem => {
        const nonConformiteIdentifier = getNonConformiteIdentifier(nonConformiteItem);
        if (nonConformiteIdentifier == null || nonConformiteCollectionIdentifiers.includes(nonConformiteIdentifier)) {
          return false;
        }
        nonConformiteCollectionIdentifiers.push(nonConformiteIdentifier);
        return true;
      });
      return [...nonConformitesToAdd, ...nonConformiteCollection];
    }
    return nonConformiteCollection;
  }

  protected convertDateFromClient(nonConformite: INonConformite): INonConformite {
    return Object.assign({}, nonConformite, {
      date: nonConformite.date?.isValid() ? nonConformite.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((nonConformite: INonConformite) => {
        nonConformite.date = nonConformite.date ? dayjs(nonConformite.date) : undefined;
      });
    }
    return res;
  }
}
