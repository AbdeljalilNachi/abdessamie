import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { INonConformite, NonConformite } from '../non-conformite.model';

import { NonConformiteService } from './non-conformite.service';

describe('Service Tests', () => {
  describe('NonConformite Service', () => {
    let service: NonConformiteService;
    let httpMock: HttpTestingController;
    let elemDefault: INonConformite;
    let expectedResult: INonConformite | INonConformite[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(NonConformiteService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        date: currentDate,
        description: 'AAAAAAA',
        causesPotentielles: 'AAAAAAA',
        origine: 'AAAAAAA',
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

      it('should create a NonConformite', () => {
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

        service.create(new NonConformite()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a NonConformite', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            description: 'BBBBBB',
            causesPotentielles: 'BBBBBB',
            origine: 'BBBBBB',
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

      it('should partial update a NonConformite', () => {
        const patchObject = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
            description: 'BBBBBB',
            causesPotentielles: 'BBBBBB',
          },
          new NonConformite()
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

      it('should return a list of NonConformite', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            description: 'BBBBBB',
            causesPotentielles: 'BBBBBB',
            origine: 'BBBBBB',
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

      it('should delete a NonConformite', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addNonConformiteToCollectionIfMissing', () => {
        it('should add a NonConformite to an empty array', () => {
          const nonConformite: INonConformite = { id: 123 };
          expectedResult = service.addNonConformiteToCollectionIfMissing([], nonConformite);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(nonConformite);
        });

        it('should not add a NonConformite to an array that contains it', () => {
          const nonConformite: INonConformite = { id: 123 };
          const nonConformiteCollection: INonConformite[] = [
            {
              ...nonConformite,
            },
            { id: 456 },
          ];
          expectedResult = service.addNonConformiteToCollectionIfMissing(nonConformiteCollection, nonConformite);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a NonConformite to an array that doesn't contain it", () => {
          const nonConformite: INonConformite = { id: 123 };
          const nonConformiteCollection: INonConformite[] = [{ id: 456 }];
          expectedResult = service.addNonConformiteToCollectionIfMissing(nonConformiteCollection, nonConformite);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(nonConformite);
        });

        it('should add only unique NonConformite to an array', () => {
          const nonConformiteArray: INonConformite[] = [{ id: 123 }, { id: 456 }, { id: 89637 }];
          const nonConformiteCollection: INonConformite[] = [{ id: 123 }];
          expectedResult = service.addNonConformiteToCollectionIfMissing(nonConformiteCollection, ...nonConformiteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const nonConformite: INonConformite = { id: 123 };
          const nonConformite2: INonConformite = { id: 456 };
          expectedResult = service.addNonConformiteToCollectionIfMissing([], nonConformite, nonConformite2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(nonConformite);
          expect(expectedResult).toContain(nonConformite2);
        });

        it('should accept null and undefined values', () => {
          const nonConformite: INonConformite = { id: 123 };
          expectedResult = service.addNonConformiteToCollectionIfMissing([], null, nonConformite, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(nonConformite);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
