import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { TypeAnalyseSWOT } from 'app/entities/enumerations/type-analyse-swot.model';
import { IAnalyseSWOT, AnalyseSWOT } from '../analyse-swot.model';

import { AnalyseSWOTService } from './analyse-swot.service';

describe('Service Tests', () => {
  describe('AnalyseSWOT Service', () => {
    let service: AnalyseSWOTService;
    let httpMock: HttpTestingController;
    let elemDefault: IAnalyseSWOT;
    let expectedResult: IAnalyseSWOT | IAnalyseSWOT[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AnalyseSWOTService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dateIdentification: currentDate,
        description: 'AAAAAAA',
        pilote: 'AAAAAAA',
        type: TypeAnalyseSWOT.FORCE,
        bu: 'AAAAAAA',
        commentaire: 'AAAAAAA',
        afficher: false,
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

      it('should create a AnalyseSWOT', () => {
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

        service.create(new AnalyseSWOT()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AnalyseSWOT', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateIdentification: currentDate.format(DATE_FORMAT),
            description: 'BBBBBB',
            pilote: 'BBBBBB',
            type: 'BBBBBB',
            bu: 'BBBBBB',
            commentaire: 'BBBBBB',
            afficher: true,
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

      it('should partial update a AnalyseSWOT', () => {
        const patchObject = Object.assign(
          {
            type: 'BBBBBB',
            bu: 'BBBBBB',
          },
          new AnalyseSWOT()
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

      it('should return a list of AnalyseSWOT', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateIdentification: currentDate.format(DATE_FORMAT),
            description: 'BBBBBB',
            pilote: 'BBBBBB',
            type: 'BBBBBB',
            bu: 'BBBBBB',
            commentaire: 'BBBBBB',
            afficher: true,
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

      it('should delete a AnalyseSWOT', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAnalyseSWOTToCollectionIfMissing', () => {
        it('should add a AnalyseSWOT to an empty array', () => {
          const analyseSWOT: IAnalyseSWOT = { id: 123 };
          expectedResult = service.addAnalyseSWOTToCollectionIfMissing([], analyseSWOT);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(analyseSWOT);
        });

        it('should not add a AnalyseSWOT to an array that contains it', () => {
          const analyseSWOT: IAnalyseSWOT = { id: 123 };
          const analyseSWOTCollection: IAnalyseSWOT[] = [
            {
              ...analyseSWOT,
            },
            { id: 456 },
          ];
          expectedResult = service.addAnalyseSWOTToCollectionIfMissing(analyseSWOTCollection, analyseSWOT);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AnalyseSWOT to an array that doesn't contain it", () => {
          const analyseSWOT: IAnalyseSWOT = { id: 123 };
          const analyseSWOTCollection: IAnalyseSWOT[] = [{ id: 456 }];
          expectedResult = service.addAnalyseSWOTToCollectionIfMissing(analyseSWOTCollection, analyseSWOT);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(analyseSWOT);
        });

        it('should add only unique AnalyseSWOT to an array', () => {
          const analyseSWOTArray: IAnalyseSWOT[] = [{ id: 123 }, { id: 456 }, { id: 68038 }];
          const analyseSWOTCollection: IAnalyseSWOT[] = [{ id: 123 }];
          expectedResult = service.addAnalyseSWOTToCollectionIfMissing(analyseSWOTCollection, ...analyseSWOTArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const analyseSWOT: IAnalyseSWOT = { id: 123 };
          const analyseSWOT2: IAnalyseSWOT = { id: 456 };
          expectedResult = service.addAnalyseSWOTToCollectionIfMissing([], analyseSWOT, analyseSWOT2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(analyseSWOT);
          expect(expectedResult).toContain(analyseSWOT2);
        });

        it('should accept null and undefined values', () => {
          const analyseSWOT: IAnalyseSWOT = { id: 123 };
          expectedResult = service.addAnalyseSWOTToCollectionIfMissing([], null, analyseSWOT, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(analyseSWOT);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
