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
import { IPlanificationRDD, getPlanificationRDDIdentifier } from '../planification-rdd.model';

export type EntityResponseType = HttpResponse<IPlanificationRDD>;
export type EntityArrayResponseType = HttpResponse<IPlanificationRDD[]>;

@Injectable({ providedIn: 'root' })
export class PlanificationRDDService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/planification-rdds');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/planification-rdds');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(planificationRDD: IPlanificationRDD): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planificationRDD);
    return this.http
      .post<IPlanificationRDD>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(planificationRDD: IPlanificationRDD): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planificationRDD);
    return this.http
      .put<IPlanificationRDD>(`${this.resourceUrl}/${getPlanificationRDDIdentifier(planificationRDD) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(planificationRDD: IPlanificationRDD): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planificationRDD);
    return this.http
      .patch<IPlanificationRDD>(`${this.resourceUrl}/${getPlanificationRDDIdentifier(planificationRDD) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPlanificationRDD>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPlanificationRDD[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPlanificationRDD[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPlanificationRDDToCollectionIfMissing(
    planificationRDDCollection: IPlanificationRDD[],
    ...planificationRDDSToCheck: (IPlanificationRDD | null | undefined)[]
  ): IPlanificationRDD[] {
    const planificationRDDS: IPlanificationRDD[] = planificationRDDSToCheck.filter(isPresent);
    if (planificationRDDS.length > 0) {
      const planificationRDDCollectionIdentifiers = planificationRDDCollection.map(
        planificationRDDItem => getPlanificationRDDIdentifier(planificationRDDItem)!
      );
      const planificationRDDSToAdd = planificationRDDS.filter(planificationRDDItem => {
        const planificationRDDIdentifier = getPlanificationRDDIdentifier(planificationRDDItem);
        if (planificationRDDIdentifier == null || planificationRDDCollectionIdentifiers.includes(planificationRDDIdentifier)) {
          return false;
        }
        planificationRDDCollectionIdentifiers.push(planificationRDDIdentifier);
        return true;
      });
      return [...planificationRDDSToAdd, ...planificationRDDCollection];
    }
    return planificationRDDCollection;
  }

  protected convertDateFromClient(planificationRDD: IPlanificationRDD): IPlanificationRDD {
    return Object.assign({}, planificationRDD, {
      date: planificationRDD.date?.isValid() ? planificationRDD.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((planificationRDD: IPlanificationRDD) => {
        planificationRDD.date = planificationRDD.date ? dayjs(planificationRDD.date) : undefined;
      });
    }
    return res;
  }
}
