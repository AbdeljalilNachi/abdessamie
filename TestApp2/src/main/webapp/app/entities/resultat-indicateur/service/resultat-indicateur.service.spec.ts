import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Mois } from 'app/entities/enumerations/mois.model';
import { IResultatIndicateur, ResultatIndicateur } from '../resultat-indicateur.model';

import { ResultatIndicateurService } from './resultat-indicateur.service';

describe('Service Tests', () => {
  describe('ResultatIndicateur Service', () => {
    let service: ResultatIndicateurService;
    let httpMock: HttpTestingController;
    let elemDefault: IResultatIndicateur;
    let expectedResult: IResultatIndicateur | IResultatIndicateur[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ResultatIndicateurService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        mois: Mois.JAN,
        cible: 0,
        resultat: 0,
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

      it('should create a ResultatIndicateur', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ResultatIndicateur()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ResultatIndicateur', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            mois: 'BBBBBB',
            cible: 1,
            resultat: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ResultatIndicateur', () => {
        const patchObject = Object.assign(
          {
            mois: 'BBBBBB',
            resultat: 1,
          },
          new ResultatIndicateur()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ResultatIndicateur', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            mois: 'BBBBBB',
            cible: 1,
            resultat: 1,
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

      it('should delete a ResultatIndicateur', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addResultatIndicateurToCollectionIfMissing', () => {
        it('should add a ResultatIndicateur to an empty array', () => {
          const resultatIndicateur: IResultatIndicateur = { id: 123 };
          expectedResult = service.addResultatIndicateurToCollectionIfMissing([], resultatIndicateur);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(resultatIndicateur);
        });

        it('should not add a ResultatIndicateur to an array that contains it', () => {
          const resultatIndicateur: IResultatIndicateur = { id: 123 };
          const resultatIndicateurCollection: IResultatIndicateur[] = [
            {
              ...resultatIndicateur,
            },
            { id: 456 },
          ];
          expectedResult = service.addResultatIndicateurToCollectionIfMissing(resultatIndicateurCollection, resultatIndicateur);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ResultatIndicateur to an array that doesn't contain it", () => {
          const resultatIndicateur: IResultatIndicateur = { id: 123 };
          const resultatIndicateurCollection: IResultatIndicateur[] = [{ id: 456 }];
          expectedResult = service.addResultatIndicateurToCollectionIfMissing(resultatIndicateurCollection, resultatIndicateur);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(resultatIndicateur);
        });

        it('should add only unique ResultatIndicateur to an array', () => {
          const resultatIndicateurArray: IResultatIndicateur[] = [{ id: 123 }, { id: 456 }, { id: 6743 }];
          const resultatIndicateurCollection: IResultatIndicateur[] = [{ id: 123 }];
          expectedResult = service.addResultatIndicateurToCollectionIfMissing(resultatIndicateurCollection, ...resultatIndicateurArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const resultatIndicateur: IResultatIndicateur = { id: 123 };
          const resultatIndicateur2: IResultatIndicateur = { id: 456 };
          expectedResult = service.addResultatIndicateurToCollectionIfMissing([], resultatIndicateur, resultatIndicateur2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(resultatIndicateur);
          expect(expectedResult).toContain(resultatIndicateur2);
        });

        it('should accept null and undefined values', () => {
          const resultatIndicateur: IResultatIndicateur = { id: 123 };
          expectedResult = service.addResultatIndicateurToCollectionIfMissing([], null, resultatIndicateur, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(resultatIndicateur);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
