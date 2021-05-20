jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRisque, Risque } from '../risque.model';
import { RisqueService } from '../service/risque.service';

import { RisqueRoutingResolveService } from './risque-routing-resolve.service';

describe('Service Tests', () => {
  describe('Risque routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RisqueRoutingResolveService;
    let service: RisqueService;
    let resultRisque: IRisque | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RisqueRoutingResolveService);
      service = TestBed.inject(RisqueService);
      resultRisque = undefined;
    });

    describe('resolve', () => {
      it('should return IRisque returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRisque = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRisque).toEqual({ id: 123 });
      });

      it('should return new IRisque if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRisque = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRisque).toEqual(new Risque());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRisque = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRisque).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
