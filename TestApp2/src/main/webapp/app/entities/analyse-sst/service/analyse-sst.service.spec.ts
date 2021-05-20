import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Situation } from 'app/entities/enumerations/situation.model';
import { EnumFive } from 'app/entities/enumerations/enum-five.model';
import { IAnalyseSST, AnalyseSST } from '../analyse-sst.model';

import { AnalyseSSTService } from './analyse-sst.service';

describe('Service Tests', () => {
  describe('AnalyseSST Service', () => {
    let service: AnalyseSSTService;
    let httpMock: HttpTestingController;
    let elemDefault: IAnalyseSST;
    let expectedResult: IAnalyseSST | IAnalyseSST[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AnalyseSSTService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        date: currentDate,
        buisnessUnit: 'AAAAAAA',
        uniteTravail: 'AAAAAAA',
        danger: 'AAAAAAA',
        risque: 'AAAAAAA',
        competence: 'AAAAAAA',
        situation: Situation.Normale,
        frequence: EnumFive.ONE,
        dureeExposition: EnumFive.ONE,
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

      it('should create a AnalyseSST', () => {
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

        service.create(new AnalyseSST()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AnalyseSST', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            buisnessUnit: 'BBBBBB',
            uniteTravail: 'BBBBBB',
            danger: 'BBBBBB',
            risque: 'BBBBBB',
            competence: 'BBBBBB',
            situation: 'BBBBBB',
            frequence: 'BBBBBB',
            dureeExposition: 'BBBBBB',
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

      it('should partial update a AnalyseSST', () => {
        const patchObject = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
            danger: 'BBBBBB',
            competence: 'BBBBBB',
            situation: 'BBBBBB',
            gravite: 'BBBBBB',
            origine: 'BBBBBB',
          },
          new AnalyseSST()
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

      it('should return a list of AnalyseSST', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            buisnessUnit: 'BBBBBB',
            uniteTravail: 'BBBBBB',
            danger: 'BBBBBB',
            risque: 'BBBBBB',
            competence: 'BBBBBB',
            situation: 'BBBBBB',
            frequence: 'BBBBBB',
            dureeExposition: 'BBBBBB',
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

      it('should delete a AnalyseSST', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAnalyseSSTToCollectionIfMissing', () => {
        it('should add a AnalyseSST to an empty array', () => {
          const analyseSST: IAnalyseSST = { id: 123 };
          expectedResult = service.addAnalyseSSTToCollectionIfMissing([], analyseSST);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(analyseSST);
        });

        it('should not add a AnalyseSST to an array that contains it', () => {
          const analyseSST: IAnalyseSST = { id: 123 };
          const analyseSSTCollection: IAnalyseSST[] = [
            {
              ...analyseSST,
            },
            { id: 456 },
          ];
          expectedResult = service.addAnalyseSSTToCollectionIfMissing(analyseSSTCollection, analyseSST);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AnalyseSST to an array that doesn't contain it", () => {
          const analyseSST: IAnalyseSST = { id: 123 };
          const analyseSSTCollection: IAnalyseSST[] = [{ id: 456 }];
          expectedResult = service.addAnalyseSSTToCollectionIfMissing(analyseSSTCollection, analyseSST);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(analyseSST);
        });

        it('should add only unique AnalyseSST to an array', () => {
          const analyseSSTArray: IAnalyseSST[] = [{ id: 123 }, { id: 456 }, { id: 21134 }];
          const analyseSSTCollection: IAnalyseSST[] = [{ id: 123 }];
          expectedResult = service.addAnalyseSSTToCollectionIfMissing(analyseSSTCollection, ...analyseSSTArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const analyseSST: IAnalyseSST = { id: 123 };
          const analyseSST2: IAnalyseSST = { id: 456 };
          expectedResult = service.addAnalyseSSTToCollectionIfMissing([], analyseSST, analyseSST2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(analyseSST);
          expect(expectedResult).toContain(analyseSST2);
        });

        it('should accept null and undefined values', () => {
          const analyseSST: IAnalyseSST = { id: 123 };
          expectedResult = service.addAnalyseSSTToCollectionIfMissing([], null, analyseSST, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(analyseSST);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
