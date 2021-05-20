import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IIndicateurSMI, IndicateurSMI } from '../indicateur-smi.model';

import { IndicateurSMIService } from './indicateur-smi.service';

describe('Service Tests', () => {
  describe('IndicateurSMI Service', () => {
    let service: IndicateurSMIService;
    let httpMock: HttpTestingController;
    let elemDefault: IIndicateurSMI;
    let expectedResult: IIndicateurSMI | IIndicateurSMI[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(IndicateurSMIService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dateIdentification: currentDate,
        indicateur: 'AAAAAAA',
        formuleCalcul: 'AAAAAAA',
        cible: 0,
        seuilTolerance: 0,
        unite: 'AAAAAAA',
        periodicite: 'AAAAAAA',
        observations: 'AAAAAAA',
        vigueur: false,
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

      it('should create a IndicateurSMI', () => {
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

        service.create(new IndicateurSMI()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a IndicateurSMI', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateIdentification: currentDate.format(DATE_FORMAT),
            indicateur: 'BBBBBB',
            formuleCalcul: 'BBBBBB',
            cible: 1,
            seuilTolerance: 1,
            unite: 'BBBBBB',
            periodicite: 'BBBBBB',
            observations: 'BBBBBB',
            vigueur: true,
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

      it('should partial update a IndicateurSMI', () => {
        const patchObject = Object.assign(
          {
            dateIdentification: currentDate.format(DATE_FORMAT),
            indicateur: 'BBBBBB',
            cible: 1,
            periodicite: 'BBBBBB',
          },
          new IndicateurSMI()
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

      it('should return a list of IndicateurSMI', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateIdentification: currentDate.format(DATE_FORMAT),
            indicateur: 'BBBBBB',
            formuleCalcul: 'BBBBBB',
            cible: 1,
            seuilTolerance: 1,
            unite: 'BBBBBB',
            periodicite: 'BBBBBB',
            observations: 'BBBBBB',
            vigueur: true,
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

      it('should delete a IndicateurSMI', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addIndicateurSMIToCollectionIfMissing', () => {
        it('should add a IndicateurSMI to an empty array', () => {
          const indicateurSMI: IIndicateurSMI = { id: 123 };
          expectedResult = service.addIndicateurSMIToCollectionIfMissing([], indicateurSMI);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(indicateurSMI);
        });

        it('should not add a IndicateurSMI to an array that contains it', () => {
          const indicateurSMI: IIndicateurSMI = { id: 123 };
          const indicateurSMICollection: IIndicateurSMI[] = [
            {
              ...indicateurSMI,
            },
            { id: 456 },
          ];
          expectedResult = service.addIndicateurSMIToCollectionIfMissing(indicateurSMICollection, indicateurSMI);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a IndicateurSMI to an array that doesn't contain it", () => {
          const indicateurSMI: IIndicateurSMI = { id: 123 };
          const indicateurSMICollection: IIndicateurSMI[] = [{ id: 456 }];
          expectedResult = service.addIndicateurSMIToCollectionIfMissing(indicateurSMICollection, indicateurSMI);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(indicateurSMI);
        });

        it('should add only unique IndicateurSMI to an array', () => {
          const indicateurSMIArray: IIndicateurSMI[] = [{ id: 123 }, { id: 456 }, { id: 47177 }];
          const indicateurSMICollection: IIndicateurSMI[] = [{ id: 123 }];
          expectedResult = service.addIndicateurSMIToCollectionIfMissing(indicateurSMICollection, ...indicateurSMIArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const indicateurSMI: IIndicateurSMI = { id: 123 };
          const indicateurSMI2: IIndicateurSMI = { id: 456 };
          expectedResult = service.addIndicateurSMIToCollectionIfMissing([], indicateurSMI, indicateurSMI2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(indicateurSMI);
          expect(expectedResult).toContain(indicateurSMI2);
        });

        it('should accept null and undefined values', () => {
          const indicateurSMI: IIndicateurSMI = { id: 123 };
          expectedResult = service.addIndicateurSMIToCollectionIfMissing([], null, indicateurSMI, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(indicateurSMI);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
