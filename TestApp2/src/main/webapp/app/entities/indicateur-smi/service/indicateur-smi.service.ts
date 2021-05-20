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
import { IIndicateurSMI, getIndicateurSMIIdentifier } from '../indicateur-smi.model';

export type EntityResponseType = HttpResponse<IIndicateurSMI>;
export type EntityArrayResponseType = HttpResponse<IIndicateurSMI[]>;

@Injectable({ providedIn: 'root' })
export class IndicateurSMIService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/indicateur-smis');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/indicateur-smis');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(indicateurSMI: IIndicateurSMI): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(indicateurSMI);
    return this.http
      .post<IIndicateurSMI>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(indicateurSMI: IIndicateurSMI): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(indicateurSMI);
    return this.http
      .put<IIndicateurSMI>(`${this.resourceUrl}/${getIndicateurSMIIdentifier(indicateurSMI) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(indicateurSMI: IIndicateurSMI): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(indicateurSMI);
    return this.http
      .patch<IIndicateurSMI>(`${this.resourceUrl}/${getIndicateurSMIIdentifier(indicateurSMI) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIndicateurSMI>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIndicateurSMI[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIndicateurSMI[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addIndicateurSMIToCollectionIfMissing(
    indicateurSMICollection: IIndicateurSMI[],
    ...indicateurSMISToCheck: (IIndicateurSMI | null | undefined)[]
  ): IIndicateurSMI[] {
    const indicateurSMIS: IIndicateurSMI[] = indicateurSMISToCheck.filter(isPresent);
    if (indicateurSMIS.length > 0) {
      const indicateurSMICollectionIdentifiers = indicateurSMICollection.map(
        indicateurSMIItem => getIndicateurSMIIdentifier(indicateurSMIItem)!
      );
      const indicateurSMISToAdd = indicateurSMIS.filter(indicateurSMIItem => {
        const indicateurSMIIdentifier = getIndicateurSMIIdentifier(indicateurSMIItem);
        if (indicateurSMIIdentifier == null || indicateurSMICollectionIdentifiers.includes(indicateurSMIIdentifier)) {
          return false;
        }
        indicateurSMICollectionIdentifiers.push(indicateurSMIIdentifier);
        return true;
      });
      return [...indicateurSMISToAdd, ...indicateurSMICollection];
    }
    return indicateurSMICollection;
  }

  protected convertDateFromClient(indicateurSMI: IIndicateurSMI): IIndicateurSMI {
    return Object.assign({}, indicateurSMI, {
      dateIdentification: indicateurSMI.dateIdentification?.isValid() ? indicateurSMI.dateIdentification.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((indicateurSMI: IIndicateurSMI) => {
        indicateurSMI.dateIdentification = indicateurSMI.dateIdentification ? dayjs(indicateurSMI.dateIdentification) : undefined;
      });
    }
    return res;
  }
}
