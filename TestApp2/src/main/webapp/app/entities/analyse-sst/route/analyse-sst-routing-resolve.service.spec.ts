jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAnalyseSST, AnalyseSST } from '../analyse-sst.model';
import { AnalyseSSTService } from '../service/analyse-sst.service';

import { AnalyseSSTRoutingResolveService } from './analyse-sst-routing-resolve.service';

describe('Service Tests', () => {
  describe('AnalyseSST routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AnalyseSSTRoutingResolveService;
    let service: AnalyseSSTService;
    let resultAnalyseSST: IAnalyseSST | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AnalyseSSTRoutingResolveService);
      service = TestBed.inject(AnalyseSSTService);
      resultAnalyseSST = undefined;
    });

    describe('resolve', () => {
      it('should return IAnalyseSST returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAnalyseSST = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAnalyseSST).toEqual({ id: 123 });
      });

      it('should return new IAnalyseSST if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAnalyseSST = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAnalyseSST).toEqual(new AnalyseSST());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAnalyseSST = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAnalyseSST).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
