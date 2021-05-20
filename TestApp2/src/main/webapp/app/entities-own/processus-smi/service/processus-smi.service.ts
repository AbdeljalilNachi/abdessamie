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
import { IProcessusSMI, getProcessusSMIIdentifier } from '../processus-smi.model';

export type EntityResponseType = HttpResponse<IProcessusSMI>;
export type EntityArrayResponseType = HttpResponse<IProcessusSMI[]>;

@Injectable({ providedIn: 'root' })
export class ProcessusSMIService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/processus-smis');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/processus-smis');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(processusSMI: IProcessusSMI): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(processusSMI);
    return this.http
      .post<IProcessusSMI>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(processusSMI: IProcessusSMI): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(processusSMI);
    return this.http
      .put<IProcessusSMI>(`${this.resourceUrl}/${getProcessusSMIIdentifier(processusSMI) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(processusSMI: IProcessusSMI): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(processusSMI);
    return this.http
      .patch<IProcessusSMI>(`${this.resourceUrl}/${getProcessusSMIIdentifier(processusSMI) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProcessusSMI>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProcessusSMI[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProcessusSMI[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addProcessusSMIToCollectionIfMissing(
    processusSMICollection: IProcessusSMI[],
    ...processusSMISToCheck: (IProcessusSMI | null | undefined)[]
  ): IProcessusSMI[] {
    const processusSMIS: IProcessusSMI[] = processusSMISToCheck.filter(isPresent);
    if (processusSMIS.length > 0) {
      const processusSMICollectionIdentifiers = processusSMICollection.map(
        processusSMIItem => getProcessusSMIIdentifier(processusSMIItem)!
      );
      const processusSMISToAdd = processusSMIS.filter(processusSMIItem => {
        const processusSMIIdentifier = getProcessusSMIIdentifier(processusSMIItem);
        if (processusSMIIdentifier == null || processusSMICollectionIdentifiers.includes(processusSMIIdentifier)) {
          return false;
        }
        processusSMICollectionIdentifiers.push(processusSMIIdentifier);
        return true;
      });
      return [...processusSMISToAdd, ...processusSMICollection];
    }
    return processusSMICollection;
  }

  protected convertDateFromClient(processusSMI: IProcessusSMI): IProcessusSMI {
    return Object.assign({}, processusSMI, {
      date: processusSMI.date?.isValid() ? processusSMI.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((processusSMI: IProcessusSMI) => {
        processusSMI.date = processusSMI.date ? dayjs(processusSMI.date) : undefined;
      });
    }
    return res;
  }
}
