jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPlanificationRDD, PlanificationRDD } from '../planification-rdd.model';
import { PlanificationRDDService } from '../service/planification-rdd.service';

import { PlanificationRDDRoutingResolveService } from './planification-rdd-routing-resolve.service';

describe('Service Tests', () => {
  describe('PlanificationRDD routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PlanificationRDDRoutingResolveService;
    let service: PlanificationRDDService;
    let resultPlanificationRDD: IPlanificationRDD | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PlanificationRDDRoutingResolveService);
      service = TestBed.inject(PlanificationRDDService);
      resultPlanificationRDD = undefined;
    });

    describe('resolve', () => {
      it('should return IPlanificationRDD returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPlanificationRDD = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPlanificationRDD).toEqual({ id: 123 });
      });

      it('should return new IPlanificationRDD if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPlanificationRDD = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPlanificationRDD).toEqual(new PlanificationRDD());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPlanificationRDD = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPlanificationRDD).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
