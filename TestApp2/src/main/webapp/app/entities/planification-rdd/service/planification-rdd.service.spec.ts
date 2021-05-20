import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Standard } from 'app/entities/enumerations/standard.model';
import { IPlanificationRDD, PlanificationRDD } from '../planification-rdd.model';

import { PlanificationRDDService } from './planification-rdd.service';

describe('Service Tests', () => {
  describe('PlanificationRDD Service', () => {
    let service: PlanificationRDDService;
    let httpMock: HttpTestingController;
    let elemDefault: IPlanificationRDD;
    let expectedResult: IPlanificationRDD | IPlanificationRDD[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PlanificationRDDService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nRdd: 0,
        date: currentDate,
        realisee: false,
        presentationContentType: 'image/png',
        presentation: 'AAAAAAA',
        standard: Standard.ISO9001,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PlanificationRDD', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new PlanificationRDD()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PlanificationRDD', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nRdd: 1,
            date: currentDate.format(DATE_FORMAT),
            realisee: true,
            presentation: 'BBBBBB',
            standard: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PlanificationRDD', () => {
        const patchObject = Object.assign(
          {
            presentation: 'BBBBBB',
          },
          new PlanificationRDD()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PlanificationRDD', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nRdd: 1,
            date: currentDate.format(DATE_FORMAT),
            realisee: true,
            presentation: 'BBBBBB',
            standard: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PlanificationRDD', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPlanificationRDDToCollectionIfMissing', () => {
        it('should add a PlanificationRDD to an empty array', () => {
          const planificationRDD: IPlanificationRDD = { id: 123 };
          expectedResult = service.addPlanificationRDDToCollectionIfMissing([], planificationRDD);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(planificationRDD);
        });

        it('should not add a PlanificationRDD to an array that contains it', () => {
          const planificationRDD: IPlanificationRDD = { id: 123 };
          const planificationRDDCollection: IPlanificationRDD[] = [
            {
              ...planificationRDD,
            },
            { id: 456 },
          ];
          expectedResult = service.addPlanificationRDDToCollectionIfMissing(planificationRDDCollection, planificationRDD);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PlanificationRDD to an array that doesn't contain it", () => {
          const planificationRDD: IPlanificationRDD = { id: 123 };
          const planificationRDDCollection: IPlanificationRDD[] = [{ id: 456 }];
          expectedResult = service.addPlanificationRDDToCollectionIfMissing(planificationRDDCollection, planificationRDD);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(planificationRDD);
        });

        it('should add only unique PlanificationRDD to an array', () => {
          const planificationRDDArray: IPlanificationRDD[] = [{ id: 123 }, { id: 456 }, { id: 62227 }];
          const planificationRDDCollection: IPlanificationRDD[] = [{ id: 123 }];
          expectedResult = service.addPlanificationRDDToCollectionIfMissing(planificationRDDCollection, ...planificationRDDArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const planificationRDD: IPlanificationRDD = { id: 123 };
          const planificationRDD2: IPlanificationRDD = { id: 456 };
          expectedResult = service.addPlanificationRDDToCollectionIfMissing([], planificationRDD, planificationRDD2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(planificationRDD);
          expect(expectedResult).toContain(planificationRDD2);
        });

        it('should accept null and undefined values', () => {
          const planificationRDD: IPlanificationRDD = { id: 123 };
          expectedResult = service.addPlanificationRDDToCollectionIfMissing([], null, planificationRDD, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(planificationRDD);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
