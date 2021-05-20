jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAutreAction, AutreAction } from '../autre-action.model';
import { AutreActionService } from '../service/autre-action.service';

import { AutreActionRoutingResolveService } from './autre-action-routing-resolve.service';

describe('Service Tests', () => {
  describe('AutreAction routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AutreActionRoutingResolveService;
    let service: AutreActionService;
    let resultAutreAction: IAutreAction | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AutreActionRoutingResolveService);
      service = TestBed.inject(AutreActionService);
      resultAutreAction = undefined;
    });

    describe('resolve', () => {
      it('should return IAutreAction returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAutreAction = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAutreAction).toEqual({ id: 123 });
      });

      it('should return new IAutreAction if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAutreAction = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAutreAction).toEqual(new AutreAction());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAutreAction = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAutreAction).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
