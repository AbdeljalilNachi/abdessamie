jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IConstatAudit, ConstatAudit } from '../constat-audit.model';
import { ConstatAuditService } from '../service/constat-audit.service';

import { ConstatAuditRoutingResolveService } from './constat-audit-routing-resolve.service';

describe('Service Tests', () => {
  describe('ConstatAudit routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ConstatAuditRoutingResolveService;
    let service: ConstatAuditService;
    let resultConstatAudit: IConstatAudit | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ConstatAuditRoutingResolveService);
      service = TestBed.inject(ConstatAuditService);
      resultConstatAudit = undefined;
    });

    describe('resolve', () => {
      it('should return IConstatAudit returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConstatAudit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultConstatAudit).toEqual({ id: 123 });
      });

      it('should return new IConstatAudit if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConstatAudit = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultConstatAudit).toEqual(new ConstatAudit());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConstatAudit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultConstatAudit).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
