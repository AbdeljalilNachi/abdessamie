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
import { IReclamation, getReclamationIdentifier } from '../reclamation.model';

export type EntityResponseType = HttpResponse<IReclamation>;
export type EntityArrayResponseType = HttpResponse<IReclamation[]>;

@Injectable({ providedIn: 'root' })
export class ReclamationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/reclamations');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/reclamations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(reclamation: IReclamation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reclamation);
    return this.http
      .post<IReclamation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(reclamation: IReclamation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reclamation);
    return this.http
      .put<IReclamation>(`${this.resourceUrl}/${getReclamationIdentifier(reclamation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(reclamation: IReclamation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reclamation);
    return this.http
      .patch<IReclamation>(`${this.resourceUrl}/${getReclamationIdentifier(reclamation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IReclamation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReclamation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReclamation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addReclamationToCollectionIfMissing(
    reclamationCollection: IReclamation[],
    ...reclamationsToCheck: (IReclamation | null | undefined)[]
  ): IReclamation[] {
    const reclamations: IReclamation[] = reclamationsToCheck.filter(isPresent);
    if (reclamations.length > 0) {
      const reclamationCollectionIdentifiers = reclamationCollection.map(reclamationItem => getReclamationIdentifier(reclamationItem)!);
      const reclamationsToAdd = reclamations.filter(reclamationItem => {
        const reclamationIdentifier = getReclamationIdentifier(reclamationItem);
        if (reclamationIdentifier == null || reclamationCollectionIdentifiers.includes(reclamationIdentifier)) {
          return false;
        }
        reclamationCollectionIdentifiers.push(reclamationIdentifier);
        return true;
      });
      return [...reclamationsToAdd, ...reclamationCollection];
    }
    return reclamationCollection;
  }

  protected convertDateFromClient(reclamation: IReclamation): IReclamation {
    return Object.assign({}, reclamation, {
      date: reclamation.date?.isValid() ? reclamation.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((reclamation: IReclamation) => {
        reclamation.date = reclamation.date ? dayjs(reclamation.date) : undefined;
      });
    }
    return res;
  }
}
