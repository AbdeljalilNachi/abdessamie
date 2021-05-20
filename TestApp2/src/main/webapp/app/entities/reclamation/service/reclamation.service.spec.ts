import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IReclamation, Reclamation } from '../reclamation.model';

import { ReclamationService } from './reclamation.service';

describe('Service Tests', () => {
  describe('Reclamation Service', () => {
    let service: ReclamationService;
    let httpMock: HttpTestingController;
    let elemDefault: IReclamation;
    let expectedResult: IReclamation | IReclamation[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ReclamationService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        date: currentDate,
        description: 'AAAAAAA',
        justifiee: false,
        client: 'AAAAAAA',
        piecejointeContentType: 'image/png',
        piecejointe: 'AAAAAAA',
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

      it('should create a Reclamation', () => {
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

        service.create(new Reclamation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Reclamation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            description: 'BBBBBB',
            justifiee: true,
            client: 'BBBBBB',
            piecejointe: 'BBBBBB',
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

      it('should partial update a Reclamation', () => {
        const patchObject = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
          },
          new Reclamation()
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

      it('should return a list of Reclamation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            description: 'BBBBBB',
            justifiee: true,
            client: 'BBBBBB',
            piecejointe: 'BBBBBB',
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

      it('should delete a Reclamation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addReclamationToCollectionIfMissing', () => {
        it('should add a Reclamation to an empty array', () => {
          const reclamation: IReclamation = { id: 123 };
          expectedResult = service.addReclamationToCollectionIfMissing([], reclamation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(reclamation);
        });

        it('should not add a Reclamation to an array that contains it', () => {
          const reclamation: IReclamation = { id: 123 };
          const reclamationCollection: IReclamation[] = [
            {
              ...reclamation,
            },
            { id: 456 },
          ];
          expectedResult = service.addReclamationToCollectionIfMissing(reclamationCollection, reclamation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Reclamation to an array that doesn't contain it", () => {
          const reclamation: IReclamation = { id: 123 };
          const reclamationCollection: IReclamation[] = [{ id: 456 }];
          expectedResult = service.addReclamationToCollectionIfMissing(reclamationCollection, reclamation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(reclamation);
        });

        it('should add only unique Reclamation to an array', () => {
          const reclamationArray: IReclamation[] = [{ id: 123 }, { id: 456 }, { id: 898 }];
          const reclamationCollection: IReclamation[] = [{ id: 123 }];
          expectedResult = service.addReclamationToCollectionIfMissing(reclamationCollection, ...reclamationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const reclamation: IReclamation = { id: 123 };
          const reclamation2: IReclamation = { id: 456 };
          expectedResult = service.addReclamationToCollectionIfMissing([], reclamation, reclamation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(reclamation);
          expect(expectedResult).toContain(reclamation2);
        });

        it('should accept null and undefined values', () => {
          const reclamation: IReclamation = { id: 123 };
          expectedResult = service.addReclamationToCollectionIfMissing([], null, reclamation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(reclamation);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
