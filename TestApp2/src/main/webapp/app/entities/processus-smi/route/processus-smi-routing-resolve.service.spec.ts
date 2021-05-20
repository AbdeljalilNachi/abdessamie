jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IProcessusSMI, ProcessusSMI } from '../processus-smi.model';
import { ProcessusSMIService } from '../service/processus-smi.service';

import { ProcessusSMIRoutingResolveService } from './processus-smi-routing-resolve.service';

describe('Service Tests', () => {
  describe('ProcessusSMI routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ProcessusSMIRoutingResolveService;
    let service: ProcessusSMIService;
    let resultProcessusSMI: IProcessusSMI | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ProcessusSMIRoutingResolveService);
      service = TestBed.inject(ProcessusSMIService);
      resultProcessusSMI = undefined;
    });

    describe('resolve', () => {
      it('should return IProcessusSMI returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProcessusSMI = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultProcessusSMI).toEqual({ id: 123 });
      });

      it('should return new IProcessusSMI if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProcessusSMI = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultProcessusSMI).toEqual(new ProcessusSMI());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProcessusSMI = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultProcessusSMI).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
