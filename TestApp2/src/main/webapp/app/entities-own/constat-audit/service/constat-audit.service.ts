import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IConstatAudit, getConstatAuditIdentifier } from '../constat-audit.model';

export type EntityResponseType = HttpResponse<IConstatAudit>;
export type EntityArrayResponseType = HttpResponse<IConstatAudit[]>;

@Injectable({ providedIn: 'root' })
export class ConstatAuditService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/constat-audits');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/constat-audits');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(constatAudit: IConstatAudit): Observable<EntityResponseType> {
    return this.http.post<IConstatAudit>(this.resourceUrl, constatAudit, { observe: 'response' });
  }

  update(constatAudit: IConstatAudit): Observable<EntityResponseType> {
    return this.http.put<IConstatAudit>(`${this.resourceUrl}/${getConstatAuditIdentifier(constatAudit) as number}`, constatAudit, {
      observe: 'response',
    });
  }

  partialUpdate(constatAudit: IConstatAudit): Observable<EntityResponseType> {
    return this.http.patch<IConstatAudit>(`${this.resourceUrl}/${getConstatAuditIdentifier(constatAudit) as number}`, constatAudit, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConstatAudit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConstatAudit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConstatAudit[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addConstatAuditToCollectionIfMissing(
    constatAuditCollection: IConstatAudit[],
    ...constatAuditsToCheck: (IConstatAudit | null | undefined)[]
  ): IConstatAudit[] {
    const constatAudits: IConstatAudit[] = constatAuditsToCheck.filter(isPresent);
    if (constatAudits.length > 0) {
      const constatAuditCollectionIdentifiers = constatAuditCollection.map(
        constatAuditItem => getConstatAuditIdentifier(constatAuditItem)!
      );
      const constatAuditsToAdd = constatAudits.filter(constatAuditItem => {
        const constatAuditIdentifier = getConstatAuditIdentifier(constatAuditItem);
        if (constatAuditIdentifier == null || constatAuditCollectionIdentifiers.includes(constatAuditIdentifier)) {
          return false;
        }
        constatAuditCollectionIdentifiers.push(constatAuditIdentifier);
        return true;
      });
      return [...constatAuditsToAdd, ...constatAuditCollection];
    }
    return constatAuditCollection;
  }
}
