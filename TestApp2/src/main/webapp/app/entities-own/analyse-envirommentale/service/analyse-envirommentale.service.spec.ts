import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Situation } from 'app/entities-own/enumerations/situation.model';
import { EnumFive } from 'app/entities-own/enumerations/enum-five.model';
import { IAnalyseEnvirommentale, AnalyseEnvirommentale } from '../analyse-envirommentale.model';

import { AnalyseEnvirommentaleService } from './analyse-envirommentale.service';

describe('Service Tests', () => {
  describe('AnalyseEnvirommentale Service', () => {
    let service: AnalyseEnvirommentaleService;
    let httpMock: HttpTestingController;
    let elemDefault: IAnalyseEnvirommentale;
    let expectedResult: IAnalyseEnvirommentale | IAnalyseEnvirommentale[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AnalyseEnvirommentaleService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        date: currentDate,
        businessUnit: 'AAAAAAA',
        activite: 'AAAAAAA',
        aspectEnvironnemental: 'AAAAAAA',
        impactEnvironnemental: 'AAAAAAA',
        competencesRequises: 'AAAAAAA',
        situation: Situation.Normale,
        frequence: EnumFive.ONE,
        sensibiliteMilieu: EnumFive.ONE,
        coefficientMaitrise: EnumFive.ONE,
        gravite: EnumFive.ONE,
        criticite: 0,
        maitriseExistante: 'AAAAAAA',
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

      it('should create a AnalyseEnvirommentale', () => {
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

        service.create(new AnalyseEnvirommentale()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AnalyseEnvirommentale', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            businessUnit: 'BBBBBB',
            activite: 'BBBBBB',
            aspectEnvironnemental: 'BBBBBB',
            impactEnvironnemental: 'BBBBBB',
            competencesRequises: 'BBBBBB',
            situation: 'BBBBBB',
            frequence: 'BBBBBB',
            sensibiliteMilieu: 'BBBBBB',
            coefficientMaitrise: 'BBBBBB',
            gravite: 'BBBBBB',
            criticite: 1,
            maitriseExistante: 'BBBBBB',
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

      it('should partial update a AnalyseEnvirommentale', () => {
        const patchObject = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
            businessUnit: 'BBBBBB',
            sensibiliteMilieu: 'BBBBBB',
            maitriseExistante: 'BBBBBB',
          },
          new AnalyseEnvirommentale()
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

      it('should return a list of AnalyseEnvirommentale', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            businessUnit: 'BBBBBB',
            activite: 'BBBBBB',
            aspectEnvironnemental: 'BBBBBB',
            impactEnvironnemental: 'BBBBBB',
            competencesRequises: 'BBBBBB',
            situation: 'BBBBBB',
            frequence: 'BBBBBB',
            sensibiliteMilieu: 'BBBBBB',
            coefficientMaitrise: 'BBBBBB',
            gravite: 'BBBBBB',
            criticite: 1,
            maitriseExistante: 'BBBBBB',
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

      it('should delete a AnalyseEnvirommentale', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAnalyseEnvirommentaleToCollectionIfMissing', () => {
        it('should add a AnalyseEnvirommentale to an empty array', () => {
          const analyseEnvirommentale: IAnalyseEnvirommentale = { id: 123 };
          expectedResult = service.addAnalyseEnvirommentaleToCollectionIfMissing([], analyseEnvirommentale);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(analyseEnvirommentale);
        });

        it('should not add a AnalyseEnvirommentale to an array that contains it', () => {
          const analyseEnvirommentale: IAnalyseEnvirommentale = { id: 123 };
          const analyseEnvirommentaleCollection: IAnalyseEnvirommentale[] = [
            {
              ...analyseEnvirommentale,
            },
            { id: 456 },
          ];
          expectedResult = service.addAnalyseEnvirommentaleToCollectionIfMissing(analyseEnvirommentaleCollection, analyseEnvirommentale);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AnalyseEnvirommentale to an array that doesn't contain it", () => {
          const analyseEnvirommentale: IAnalyseEnvirommentale = { id: 123 };
          const analyseEnvirommentaleCollection: IAnalyseEnvirommentale[] = [{ id: 456 }];
          expectedResult = service.addAnalyseEnvirommentaleToCollectionIfMissing(analyseEnvirommentaleCollection, analyseEnvirommentale);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(analyseEnvirommentale);
        });

        it('should add only unique AnalyseEnvirommentale to an array', () => {
          const analyseEnvirommentaleArray: IAnalyseEnvirommentale[] = [{ id: 123 }, { id: 456 }, { id: 95933 }];
          const analyseEnvirommentaleCollection: IAnalyseEnvirommentale[] = [{ id: 123 }];
          expectedResult = service.addAnalyseEnvirommentaleToCollectionIfMissing(
            analyseEnvirommentaleCollection,
            ...analyseEnvirommentaleArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const analyseEnvirommentale: IAnalyseEnvirommentale = { id: 123 };
          const analyseEnvirommentale2: IAnalyseEnvirommentale = { id: 456 };
          expectedResult = service.addAnalyseEnvirommentaleToCollectionIfMissing([], analyseEnvirommentale, analyseEnvirommentale2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(analyseEnvirommentale);
          expect(expectedResult).toContain(analyseEnvirommentale2);
        });

        it('should accept null and undefined values', () => {
          const analyseEnvirommentale: IAnalyseEnvirommentale = { id: 123 };
          expectedResult = service.addAnalyseEnvirommentaleToCollectionIfMissing([], null, analyseEnvirommentale, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(analyseEnvirommentale);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
