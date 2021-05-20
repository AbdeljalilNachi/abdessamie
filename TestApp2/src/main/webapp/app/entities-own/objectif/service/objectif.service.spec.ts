import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IObjectif, Objectif } from '../objectif.model';

import { ObjectifService } from './objectif.service';

describe('Service Tests', () => {
  describe('Objectif Service', () => {
    let service: ObjectifService;
    let httpMock: HttpTestingController;
    let elemDefault: IObjectif;
    let expectedResult: IObjectif | IObjectif[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ObjectifService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        axedelapolitiqueqse: 'AAAAAAA',
        objectifqse: 'AAAAAAA',
        origine: 'AAAAAAA',
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

      it('should create a Objectif', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Objectif()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Objectif', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            axedelapolitiqueqse: 'BBBBBB',
            objectifqse: 'BBBBBB',
            origine: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Objectif', () => {
        const patchObject = Object.assign(
          {
            axedelapolitiqueqse: 'BBBBBB',
          },
          new Objectif()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Objectif', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            axedelapolitiqueqse: 'BBBBBB',
            objectifqse: 'BBBBBB',
            origine: 'BBBBBB',
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

      it('should delete a Objectif', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addObjectifToCollectionIfMissing', () => {
        it('should add a Objectif to an empty array', () => {
          const objectif: IObjectif = { id: 123 };
          expectedResult = service.addObjectifToCollectionIfMissing([], objectif);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(objectif);
        });

        it('should not add a Objectif to an array that contains it', () => {
          const objectif: IObjectif = { id: 123 };
          const objectifCollection: IObjectif[] = [
            {
              ...objectif,
            },
            { id: 456 },
          ];
          expectedResult = service.addObjectifToCollectionIfMissing(objectifCollection, objectif);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Objectif to an array that doesn't contain it", () => {
          const objectif: IObjectif = { id: 123 };
          const objectifCollection: IObjectif[] = [{ id: 456 }];
          expectedResult = service.addObjectifToCollectionIfMissing(objectifCollection, objectif);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(objectif);
        });

        it('should add only unique Objectif to an array', () => {
          const objectifArray: IObjectif[] = [{ id: 123 }, { id: 456 }, { id: 27439 }];
          const objectifCollection: IObjectif[] = [{ id: 123 }];
          expectedResult = service.addObjectifToCollectionIfMissing(objectifCollection, ...objectifArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const objectif: IObjectif = { id: 123 };
          const objectif2: IObjectif = { id: 456 };
          expectedResult = service.addObjectifToCollectionIfMissing([], objectif, objectif2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(objectif);
          expect(expectedResult).toContain(objectif2);
        });

        it('should accept null and undefined values', () => {
          const objectif: IObjectif = { id: 123 };
          expectedResult = service.addObjectifToCollectionIfMissing([], null, objectif, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(objectif);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
