import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPolitiqueQSE, PolitiqueQSE } from '../politique-qse.model';

import { PolitiqueQSEService } from './politique-qse.service';

describe('Service Tests', () => {
  describe('PolitiqueQSE Service', () => {
    let service: PolitiqueQSEService;
    let httpMock: HttpTestingController;
    let elemDefault: IPolitiqueQSE;
    let expectedResult: IPolitiqueQSE | IPolitiqueQSE[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PolitiqueQSEService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        date: currentDate,
        axePolitiqueQSE: 'AAAAAAA',
        objectifQSE: 'AAAAAAA',
        vigueur: false,
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

      it('should create a PolitiqueQSE', () => {
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

        service.create(new PolitiqueQSE()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PolitiqueQSE', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            axePolitiqueQSE: 'BBBBBB',
            objectifQSE: 'BBBBBB',
            vigueur: true,
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

      it('should partial update a PolitiqueQSE', () => {
        const patchObject = Object.assign(
          {
            axePolitiqueQSE: 'BBBBBB',
            vigueur: true,
          },
          new PolitiqueQSE()
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

      it('should return a list of PolitiqueQSE', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            axePolitiqueQSE: 'BBBBBB',
            objectifQSE: 'BBBBBB',
            vigueur: true,
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

      it('should delete a PolitiqueQSE', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPolitiqueQSEToCollectionIfMissing', () => {
        it('should add a PolitiqueQSE to an empty array', () => {
          const politiqueQSE: IPolitiqueQSE = { id: 123 };
          expectedResult = service.addPolitiqueQSEToCollectionIfMissing([], politiqueQSE);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(politiqueQSE);
        });

        it('should not add a PolitiqueQSE to an array that contains it', () => {
          const politiqueQSE: IPolitiqueQSE = { id: 123 };
          const politiqueQSECollection: IPolitiqueQSE[] = [
            {
              ...politiqueQSE,
            },
            { id: 456 },
          ];
          expectedResult = service.addPolitiqueQSEToCollectionIfMissing(politiqueQSECollection, politiqueQSE);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PolitiqueQSE to an array that doesn't contain it", () => {
          const politiqueQSE: IPolitiqueQSE = { id: 123 };
          const politiqueQSECollection: IPolitiqueQSE[] = [{ id: 456 }];
          expectedResult = service.addPolitiqueQSEToCollectionIfMissing(politiqueQSECollection, politiqueQSE);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(politiqueQSE);
        });

        it('should add only unique PolitiqueQSE to an array', () => {
          const politiqueQSEArray: IPolitiqueQSE[] = [{ id: 123 }, { id: 456 }, { id: 53236 }];
          const politiqueQSECollection: IPolitiqueQSE[] = [{ id: 123 }];
          expectedResult = service.addPolitiqueQSEToCollectionIfMissing(politiqueQSECollection, ...politiqueQSEArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const politiqueQSE: IPolitiqueQSE = { id: 123 };
          const politiqueQSE2: IPolitiqueQSE = { id: 456 };
          expectedResult = service.addPolitiqueQSEToCollectionIfMissing([], politiqueQSE, politiqueQSE2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(politiqueQSE);
          expect(expectedResult).toContain(politiqueQSE2);
        });

        it('should accept null and undefined values', () => {
          const politiqueQSE: IPolitiqueQSE = { id: 123 };
          expectedResult = service.addPolitiqueQSEToCollectionIfMissing([], null, politiqueQSE, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(politiqueQSE);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
