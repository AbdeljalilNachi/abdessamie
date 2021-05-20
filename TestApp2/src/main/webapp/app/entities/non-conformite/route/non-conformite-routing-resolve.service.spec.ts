jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INonConformite, NonConformite } from '../non-conformite.model';
import { NonConformiteService } from '../service/non-conformite.service';

import { NonConformiteRoutingResolveService } from './non-conformite-routing-resolve.service';

describe('Service Tests', () => {
  describe('NonConformite routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: NonConformiteRoutingResolveService;
    let service: NonConformiteService;
    let resultNonConformite: INonConformite | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(NonConformiteRoutingResolveService);
      service = TestBed.inject(NonConformiteService);
      resultNonConformite = undefined;
    });

    describe('resolve', () => {
      it('should return INonConformite returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNonConformite = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNonConformite).toEqual({ id: 123 });
      });

      it('should return new INonConformite if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNonConformite = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultNonConformite).toEqual(new NonConformite());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNonConformite = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNonConformite).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
