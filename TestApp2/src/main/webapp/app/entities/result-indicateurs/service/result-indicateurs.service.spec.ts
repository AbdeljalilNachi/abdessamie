import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IResultIndicateurs, ResultIndicateurs } from '../result-indicateurs.model';

import { ResultIndicateursService } from './result-indicateurs.service';

describe('Service Tests', () => {
  describe('ResultIndicateurs Service', () => {
    let service: ResultIndicateursService;
    let httpMock: HttpTestingController;
    let elemDefault: IResultIndicateurs;
    let expectedResult: IResultIndicateurs | IResultIndicateurs[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ResultIndicateursService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        annee: 0,
        observation: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ResultIndicateurs', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ResultIndicateurs()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ResultIndicateurs', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            annee: 1,
            observation: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ResultIndicateurs', () => {
        const patchObject = Object.assign({}, new ResultIndicateurs());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ResultIndicateurs', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            annee: 1,
            observation: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ResultIndicateurs', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addResultIndicateursToCollectionIfMissing', () => {
        it('should add a ResultIndicateurs to an empty array', () => {
          const resultIndicateurs: IResultIndicateurs = { id: 123 };
          expectedResult = service.addResultIndicateursToCollectionIfMissing([], resultIndicateurs);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(resultIndicateurs);
        });

        it('should not add a ResultIndicateurs to an array that contains it', () => {
          const resultIndicateurs: IResultIndicateurs = { id: 123 };
          const resultIndicateursCollection: IResultIndicateurs[] = [
            {
              ...resultIndicateurs,
            },
            { id: 456 },
          ];
          expectedResult = service.addResultIndicateursToCollectionIfMissing(resultIndicateursCollection, resultIndicateurs);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ResultIndicateurs to an array that doesn't contain it", () => {
          const resultIndicateurs: IResultIndicateurs = { id: 123 };
          const resultIndicateursCollection: IResultIndicateurs[] = [{ id: 456 }];
          expectedResult = service.addResultIndicateursToCollectionIfMissing(resultIndicateursCollection, resultIndicateurs);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(resultIndicateurs);
        });

        it('should add only unique ResultIndicateurs to an array', () => {
          const resultIndicateursArray: IResultIndicateurs[] = [{ id: 123 }, { id: 456 }, { id: 31517 }];
          const resultIndicateursCollection: IResultIndicateurs[] = [{ id: 123 }];
          expectedResult = service.addResultIndicateursToCollectionIfMissing(resultIndicateursCollection, ...resultIndicateursArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const resultIndicateurs: IResultIndicateurs = { id: 123 };
          const resultIndicateurs2: IResultIndicateurs = { id: 456 };
          expectedResult = service.addResultIndicateursToCollectionIfMissing([], resultIndicateurs, resultIndicateurs2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(resultIndicateurs);
          expect(expectedResult).toContain(resultIndicateurs2);
        });

        it('should accept null and undefined values', () => {
          const resultIndicateurs: IResultIndicateurs = { id: 123 };
          expectedResult = service.addResultIndicateursToCollectionIfMissing([], null, resultIndicateurs, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(resultIndicateurs);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
