import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { TypeRisque } from 'app/entities/enumerations/type-risque.model';
import { EnumFive } from 'app/entities/enumerations/enum-five.model';
import { Traitement } from 'app/entities/enumerations/traitement.model';
import { IRisque, Risque } from '../risque.model';

import { RisqueService } from './risque.service';

describe('Service Tests', () => {
  describe('Risque Service', () => {
    let service: RisqueService;
    let httpMock: HttpTestingController;
    let elemDefault: IRisque;
    let expectedResult: IRisque | IRisque[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RisqueService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dateIdentification: currentDate,
        description: 'AAAAAAA',
        causePotentielle: 'AAAAAAA',
        effetPotentiel: 'AAAAAAA',
        type: TypeRisque.MENACE,
        gravite: EnumFive.ONE,
        probabilite: EnumFive.ONE,
        criticite: 0,
        traitement: Traitement.ACCEPTE,
        commentaire: 'AAAAAAA',
        origine: 'AAAAAAA',
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

      it('should create a Risque', () => {
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

        service.create(new Risque()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Risque', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateIdentification: currentDate.format(DATE_FORMAT),
            description: 'BBBBBB',
            causePotentielle: 'BBBBBB',
            effetPotentiel: 'BBBBBB',
            type: 'BBBBBB',
            gravite: 'BBBBBB',
            probabilite: 'BBBBBB',
            criticite: 1,
            traitement: 'BBBBBB',
            commentaire: 'BBBBBB',
            origine: 'BBBBBB',
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

      it('should partial update a Risque', () => {
        const patchObject = Object.assign(
          {
            description: 'BBBBBB',
            causePotentielle: 'BBBBBB',
            gravite: 'BBBBBB',
            criticite: 1,
            commentaire: 'BBBBBB',
            origine: 'BBBBBB',
          },
          new Risque()
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

      it('should return a list of Risque', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateIdentification: currentDate.format(DATE_FORMAT),
            description: 'BBBBBB',
            causePotentielle: 'BBBBBB',
            effetPotentiel: 'BBBBBB',
            type: 'BBBBBB',
            gravite: 'BBBBBB',
            probabilite: 'BBBBBB',
            criticite: 1,
            traitement: 'BBBBBB',
            commentaire: 'BBBBBB',
            origine: 'BBBBBB',
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

      it('should delete a Risque', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRisqueToCollectionIfMissing', () => {
        it('should add a Risque to an empty array', () => {
          const risque: IRisque = { id: 123 };
          expectedResult = service.addRisqueToCollectionIfMissing([], risque);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(risque);
        });

        it('should not add a Risque to an array that contains it', () => {
          const risque: IRisque = { id: 123 };
          const risqueCollection: IRisque[] = [
            {
              ...risque,
            },
            { id: 456 },
          ];
          expectedResult = service.addRisqueToCollectionIfMissing(risqueCollection, risque);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Risque to an array that doesn't contain it", () => {
          const risque: IRisque = { id: 123 };
          const risqueCollection: IRisque[] = [{ id: 456 }];
          expectedResult = service.addRisqueToCollectionIfMissing(risqueCollection, risque);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(risque);
        });

        it('should add only unique Risque to an array', () => {
          const risqueArray: IRisque[] = [{ id: 123 }, { id: 456 }, { id: 22749 }];
          const risqueCollection: IRisque[] = [{ id: 123 }];
          expectedResult = service.addRisqueToCollectionIfMissing(risqueCollection, ...risqueArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const risque: IRisque = { id: 123 };
          const risque2: IRisque = { id: 456 };
          expectedResult = service.addRisqueToCollectionIfMissing([], risque, risque2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(risque);
          expect(expectedResult).toContain(risque2);
        });

        it('should accept null and undefined values', () => {
          const risque: IRisque = { id: 123 };
          expectedResult = service.addRisqueToCollectionIfMissing([], null, risque, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(risque);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
