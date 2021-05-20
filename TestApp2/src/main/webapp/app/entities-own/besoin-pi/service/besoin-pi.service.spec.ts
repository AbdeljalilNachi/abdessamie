import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBesoinPI, BesoinPI } from '../besoin-pi.model';

import { BesoinPIService } from './besoin-pi.service';

describe('Service Tests', () => {
  describe('BesoinPI Service', () => {
    let service: BesoinPIService;
    let httpMock: HttpTestingController;
    let elemDefault: IBesoinPI;
    let expectedResult: IBesoinPI | IBesoinPI[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BesoinPIService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dateIdentification: currentDate,
        piPertinentes: 'AAAAAAA',
        pertinente: false,
        priseEnCharge: false,
        afficher: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateIdentification: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a BesoinPI', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateIdentification: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateIdentification: currentDate,
          },
          returnedFromService
        );

        service.create(new BesoinPI()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BesoinPI', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateIdentification: currentDate.format(DATE_FORMAT),
            piPertinentes: 'BBBBBB',
            pertinente: true,
            priseEnCharge: true,
            afficher: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateIdentification: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a BesoinPI', () => {
        const patchObject = Object.assign(
          {
            piPertinentes: 'BBBBBB',
            pertinente: true,
            afficher: true,
          },
          new BesoinPI()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateIdentification: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of BesoinPI', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateIdentification: currentDate.format(DATE_FORMAT),
            piPertinentes: 'BBBBBB',
            pertinente: true,
            priseEnCharge: true,
            afficher: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateIdentification: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a BesoinPI', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBesoinPIToCollectionIfMissing', () => {
        it('should add a BesoinPI to an empty array', () => {
          const besoinPI: IBesoinPI = { id: 123 };
          expectedResult = service.addBesoinPIToCollectionIfMissing([], besoinPI);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(besoinPI);
        });

        it('should not add a BesoinPI to an array that contains it', () => {
          const besoinPI: IBesoinPI = { id: 123 };
          const besoinPICollection: IBesoinPI[] = [
            {
              ...besoinPI,
            },
            { id: 456 },
          ];
          expectedResult = service.addBesoinPIToCollectionIfMissing(besoinPICollection, besoinPI);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a BesoinPI to an array that doesn't contain it", () => {
          const besoinPI: IBesoinPI = { id: 123 };
          const besoinPICollection: IBesoinPI[] = [{ id: 456 }];
          expectedResult = service.addBesoinPIToCollectionIfMissing(besoinPICollection, besoinPI);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(besoinPI);
        });

        it('should add only unique BesoinPI to an array', () => {
          const besoinPIArray: IBesoinPI[] = [{ id: 123 }, { id: 456 }, { id: 75861 }];
          const besoinPICollection: IBesoinPI[] = [{ id: 123 }];
          expectedResult = service.addBesoinPIToCollectionIfMissing(besoinPICollection, ...besoinPIArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const besoinPI: IBesoinPI = { id: 123 };
          const besoinPI2: IBesoinPI = { id: 456 };
          expectedResult = service.addBesoinPIToCollectionIfMissing([], besoinPI, besoinPI2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(besoinPI);
          expect(expectedResult).toContain(besoinPI2);
        });

        it('should accept null and undefined values', () => {
          const besoinPI: IBesoinPI = { id: 123 };
          expectedResult = service.addBesoinPIToCollectionIfMissing([], null, besoinPI, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(besoinPI);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
