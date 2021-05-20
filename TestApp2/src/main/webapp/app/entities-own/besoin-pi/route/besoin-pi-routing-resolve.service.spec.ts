jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBesoinPI, BesoinPI } from '../besoin-pi.model';
import { BesoinPIService } from '../service/besoin-pi.service';

import { BesoinPIRoutingResolveService } from './besoin-pi-routing-resolve.service';

describe('Service Tests', () => {
  describe('BesoinPI routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BesoinPIRoutingResolveService;
    let service: BesoinPIService;
    let resultBesoinPI: IBesoinPI | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BesoinPIRoutingResolveService);
      service = TestBed.inject(BesoinPIService);
      resultBesoinPI = undefined;
    });

    describe('resolve', () => {
      it('should return IBesoinPI returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBesoinPI = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBesoinPI).toEqual({ id: 123 });
      });

      it('should return new IBesoinPI if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBesoinPI = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBesoinPI).toEqual(new BesoinPI());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBesoinPI = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBesoinPI).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
