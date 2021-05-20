jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPolitiqueQSE, PolitiqueQSE } from '../politique-qse.model';
import { PolitiqueQSEService } from '../service/politique-qse.service';

import { PolitiqueQSERoutingResolveService } from './politique-qse-routing-resolve.service';

describe('Service Tests', () => {
  describe('PolitiqueQSE routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PolitiqueQSERoutingResolveService;
    let service: PolitiqueQSEService;
    let resultPolitiqueQSE: IPolitiqueQSE | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PolitiqueQSERoutingResolveService);
      service = TestBed.inject(PolitiqueQSEService);
      resultPolitiqueQSE = undefined;
    });

    describe('resolve', () => {
      it('should return IPolitiqueQSE returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPolitiqueQSE = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPolitiqueQSE).toEqual({ id: 123 });
      });

      it('should return new IPolitiqueQSE if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPolitiqueQSE = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPolitiqueQSE).toEqual(new PolitiqueQSE());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPolitiqueQSE = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPolitiqueQSE).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
