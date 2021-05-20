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
import { IAudit, getAuditIdentifier } from '../audit.model';

export type EntityResponseType = HttpResponse<IAudit>;
export type EntityArrayResponseType = HttpResponse<IAudit[]>;

@Injectable({ providedIn: 'root' })
export class AuditService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/audits');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/audits');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(audit: IAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(audit);
    return this.http
      .post<IAudit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(audit: IAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(audit);
    return this.http
      .put<IAudit>(`${this.resourceUrl}/${getAuditIdentifier(audit) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(audit: IAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(audit);
    return this.http
      .patch<IAudit>(`${this.resourceUrl}/${getAuditIdentifier(audit) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAudit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAudit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAudit[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAuditToCollectionIfMissing(auditCollection: IAudit[], ...auditsToCheck: (IAudit | null | undefined)[]): IAudit[] {
    const audits: IAudit[] = auditsToCheck.filter(isPresent);
    if (audits.length > 0) {
      const auditCollectionIdentifiers = auditCollection.map(auditItem => getAuditIdentifier(auditItem)!);
      const auditsToAdd = audits.filter(auditItem => {
        const auditIdentifier = getAuditIdentifier(auditItem);
        if (auditIdentifier == null || auditCollectionIdentifiers.includes(auditIdentifier)) {
          return false;
        }
        auditCollectionIdentifiers.push(auditIdentifier);
        return true;
      });
      return [...auditsToAdd, ...auditCollection];
    }
    return auditCollection;
  }

  protected convertDateFromClient(audit: IAudit): IAudit {
    return Object.assign({}, audit, {
      dateAudit: audit.dateAudit?.isValid() ? audit.dateAudit.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateAudit = res.body.dateAudit ? dayjs(res.body.dateAudit) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((audit: IAudit) => {
        audit.dateAudit = audit.dateAudit ? dayjs(audit.dateAudit) : undefined;
      });
    }
    return res;
  }
}
