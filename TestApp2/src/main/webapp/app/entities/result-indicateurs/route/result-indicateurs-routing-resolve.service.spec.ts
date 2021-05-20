jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IResultIndicateurs, ResultIndicateurs } from '../result-indicateurs.model';
import { ResultIndicateursService } from '../service/result-indicateurs.service';

import { ResultIndicateursRoutingResolveService } from './result-indicateurs-routing-resolve.service';

describe('Service Tests', () => {
  describe('ResultIndicateurs routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ResultIndicateursRoutingResolveService;
    let service: ResultIndicateursService;
    let resultResultIndicateurs: IResultIndicateurs | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ResultIndicateursRoutingResolveService);
      service = TestBed.inject(ResultIndicateursService);
      resultResultIndicateurs = undefined;
    });

    describe('resolve', () => {
      it('should return IResultIndicateurs returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultResultIndicateurs = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultResultIndicateurs).toEqual({ id: 123 });
      });

      it('should return new IResultIndicateurs if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultResultIndicateurs = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultResultIndicateurs).toEqual(new ResultIndicateurs());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultResultIndicateurs = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultResultIndicateurs).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
