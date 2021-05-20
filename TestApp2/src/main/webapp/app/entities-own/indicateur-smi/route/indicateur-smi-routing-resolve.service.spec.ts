jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IIndicateurSMI, IndicateurSMI } from '../indicateur-smi.model';
import { IndicateurSMIService } from '../service/indicateur-smi.service';

import { IndicateurSMIRoutingResolveService } from './indicateur-smi-routing-resolve.service';

describe('Service Tests', () => {
  describe('IndicateurSMI routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: IndicateurSMIRoutingResolveService;
    let service: IndicateurSMIService;
    let resultIndicateurSMI: IIndicateurSMI | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(IndicateurSMIRoutingResolveService);
      service = TestBed.inject(IndicateurSMIService);
      resultIndicateurSMI = undefined;
    });

    describe('resolve', () => {
      it('should return IIndicateurSMI returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIndicateurSMI = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIndicateurSMI).toEqual({ id: 123 });
      });

      it('should return new IIndicateurSMI if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIndicateurSMI = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultIndicateurSMI).toEqual(new IndicateurSMI());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIndicateurSMI = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIndicateurSMI).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
