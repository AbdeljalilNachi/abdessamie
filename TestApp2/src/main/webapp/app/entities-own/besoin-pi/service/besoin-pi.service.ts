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
import { IBesoinPI, getBesoinPIIdentifier } from '../besoin-pi.model';

export type EntityResponseType = HttpResponse<IBesoinPI>;
export type EntityArrayResponseType = HttpResponse<IBesoinPI[]>;

@Injectable({ providedIn: 'root' })
export class BesoinPIService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/besoin-pis');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/besoin-pis');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(besoinPI: IBesoinPI): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(besoinPI);
    return this.http
      .post<IBesoinPI>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(besoinPI: IBesoinPI): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(besoinPI);
    return this.http
      .put<IBesoinPI>(`${this.resourceUrl}/${getBesoinPIIdentifier(besoinPI) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(besoinPI: IBesoinPI): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(besoinPI);
    return this.http
      .patch<IBesoinPI>(`${this.resourceUrl}/${getBesoinPIIdentifier(besoinPI) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBesoinPI>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBesoinPI[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBesoinPI[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addBesoinPIToCollectionIfMissing(besoinPICollection: IBesoinPI[], ...besoinPISToCheck: (IBesoinPI | null | undefined)[]): IBesoinPI[] {
    const besoinPIS: IBesoinPI[] = besoinPISToCheck.filter(isPresent);
    if (besoinPIS.length > 0) {
      const besoinPICollectionIdentifiers = besoinPICollection.map(besoinPIItem => getBesoinPIIdentifier(besoinPIItem)!);
      const besoinPISToAdd = besoinPIS.filter(besoinPIItem => {
        const besoinPIIdentifier = getBesoinPIIdentifier(besoinPIItem);
        if (besoinPIIdentifier == null || besoinPICollectionIdentifiers.includes(besoinPIIdentifier)) {
          return false;
        }
        besoinPICollectionIdentifiers.push(besoinPIIdentifier);
        return true;
      });
      return [...besoinPISToAdd, ...besoinPICollection];
    }
    return besoinPICollection;
  }

  protected convertDateFromClient(besoinPI: IBesoinPI): IBesoinPI {
    return Object.assign({}, besoinPI, {
      dateIdentification: besoinPI.dateIdentification?.isValid() ? besoinPI.dateIdentification.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((besoinPI: IBesoinPI) => {
        besoinPI.dateIdentification = besoinPI.dateIdentification ? dayjs(besoinPI.dateIdentification) : undefined;
      });
    }
    return res;
  }
}
