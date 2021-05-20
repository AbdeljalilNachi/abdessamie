import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Rubrique } from 'app/entities-own/enumerations/rubrique.model';
import { IObligationConformite, ObligationConformite } from '../obligation-conformite.model';

import { ObligationConformiteService } from './obligation-conformite.service';

describe('Service Tests', () => {
  describe('ObligationConformite Service', () => {
    let service: ObligationConformiteService;
    let httpMock: HttpTestingController;
    let elemDefault: IObligationConformite;
    let expectedResult: IObligationConformite | IObligationConformite[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ObligationConformiteService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        date: currentDate,
        rubrique: Rubrique.RUBRIQUE,
        reference: 'AAAAAAA',
        num: 0,
        exigence: 'AAAAAAA',
        applicable: false,
        conforme: false,
        statut: 0,
        observation: 'AAAAAAA',
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

      it('should create a ObligationConformite', () => {
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

        service.create(new ObligationConformite()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ObligationConformite', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            rubrique: 'BBBBBB',
            reference: 'BBBBBB',
            num: 1,
            exigence: 'BBBBBB',
            applicable: true,
            conforme: true,
            statut: 1,
            observation: 'BBBBBB',
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

      it('should partial update a ObligationConformite', () => {
        const patchObject = Object.assign(
          {
            reference: 'BBBBBB',
            exigence: 'BBBBBB',
            applicable: true,
            observation: 'BBBBBB',
            origine: 'BBBBBB',
          },
          new ObligationConformite()
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

      it('should return a list of ObligationConformite', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            rubrique: 'BBBBBB',
            reference: 'BBBBBB',
            num: 1,
            exigence: 'BBBBBB',
            applicable: true,
            conforme: true,
            statut: 1,
            observation: 'BBBBBB',
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

      it('should delete a ObligationConformite', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addObligationConformiteToCollectionIfMissing', () => {
        it('should add a ObligationConformite to an empty array', () => {
          const obligationConformite: IObligationConformite = { id: 123 };
          expectedResult = service.addObligationConformiteToCollectionIfMissing([], obligationConformite);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(obligationConformite);
        });

        it('should not add a ObligationConformite to an array that contains it', () => {
          const obligationConformite: IObligationConformite = { id: 123 };
          const obligationConformiteCollection: IObligationConformite[] = [
            {
              ...obligationConformite,
            },
            { id: 456 },
          ];
          expectedResult = service.addObligationConformiteToCollectionIfMissing(obligationConformiteCollection, obligationConformite);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ObligationConformite to an array that doesn't contain it", () => {
          const obligationConformite: IObligationConformite = { id: 123 };
          const obligationConformiteCollection: IObligationConformite[] = [{ id: 456 }];
          expectedResult = service.addObligationConformiteToCollectionIfMissing(obligationConformiteCollection, obligationConformite);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(obligationConformite);
        });

        it('should add only unique ObligationConformite to an array', () => {
          const obligationConformiteArray: IObligationConformite[] = [{ id: 123 }, { id: 456 }, { id: 21268 }];
          const obligationConformiteCollection: IObligationConformite[] = [{ id: 123 }];
          expectedResult = service.addObligationConformiteToCollectionIfMissing(
            obligationConformiteCollection,
            ...obligationConformiteArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const obligationConformite: IObligationConformite = { id: 123 };
          const obligationConformite2: IObligationConformite = { id: 456 };
          expectedResult = service.addObligationConformiteToCollectionIfMissing([], obligationConformite, obligationConformite2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(obligationConformite);
          expect(expectedResult).toContain(obligationConformite2);
        });

        it('should accept null and undefined values', () => {
          const obligationConformite: IObligationConformite = { id: 123 };
          expectedResult = service.addObligationConformiteToCollectionIfMissing([], null, obligationConformite, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(obligationConformite);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
