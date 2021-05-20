import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { TypeProcessus } from 'app/entities/enumerations/type-processus.model';
import { IProcessusSMI, ProcessusSMI } from '../processus-smi.model';

import { ProcessusSMIService } from './processus-smi.service';

describe('Service Tests', () => {
  describe('ProcessusSMI Service', () => {
    let service: ProcessusSMIService;
    let httpMock: HttpTestingController;
    let elemDefault: IProcessusSMI;
    let expectedResult: IProcessusSMI | IProcessusSMI[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ProcessusSMIService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        processus: 'AAAAAAA',
        date: currentDate,
        version: 0,
        finalite: 'AAAAAAA',
        ficheProcessusContentType: 'image/png',
        ficheProcessus: 'AAAAAAA',
        type: TypeProcessus.MANAGEMENT,
        vigueur: false,
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

      it('should create a ProcessusSMI', () => {
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

        service.create(new ProcessusSMI()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ProcessusSMI', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            processus: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            version: 1,
            finalite: 'BBBBBB',
            ficheProcessus: 'BBBBBB',
            type: 'BBBBBB',
            vigueur: true,
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

      it('should partial update a ProcessusSMI', () => {
        const patchObject = Object.assign(
          {
            processus: 'BBBBBB',
            version: 1,
            finalite: 'BBBBBB',
          },
          new ProcessusSMI()
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

      it('should return a list of ProcessusSMI', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            processus: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            version: 1,
            finalite: 'BBBBBB',
            ficheProcessus: 'BBBBBB',
            type: 'BBBBBB',
            vigueur: true,
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

      it('should delete a ProcessusSMI', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addProcessusSMIToCollectionIfMissing', () => {
        it('should add a ProcessusSMI to an empty array', () => {
          const processusSMI: IProcessusSMI = { id: 123 };
          expectedResult = service.addProcessusSMIToCollectionIfMissing([], processusSMI);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(processusSMI);
        });

        it('should not add a ProcessusSMI to an array that contains it', () => {
          const processusSMI: IProcessusSMI = { id: 123 };
          const processusSMICollection: IProcessusSMI[] = [
            {
              ...processusSMI,
            },
            { id: 456 },
          ];
          expectedResult = service.addProcessusSMIToCollectionIfMissing(processusSMICollection, processusSMI);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ProcessusSMI to an array that doesn't contain it", () => {
          const processusSMI: IProcessusSMI = { id: 123 };
          const processusSMICollection: IProcessusSMI[] = [{ id: 456 }];
          expectedResult = service.addProcessusSMIToCollectionIfMissing(processusSMICollection, processusSMI);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(processusSMI);
        });

        it('should add only unique ProcessusSMI to an array', () => {
          const processusSMIArray: IProcessusSMI[] = [{ id: 123 }, { id: 456 }, { id: 91695 }];
          const processusSMICollection: IProcessusSMI[] = [{ id: 123 }];
          expectedResult = service.addProcessusSMIToCollectionIfMissing(processusSMICollection, ...processusSMIArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const processusSMI: IProcessusSMI = { id: 123 };
          const processusSMI2: IProcessusSMI = { id: 456 };
          expectedResult = service.addProcessusSMIToCollectionIfMissing([], processusSMI, processusSMI2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(processusSMI);
          expect(expectedResult).toContain(processusSMI2);
        });

        it('should accept null and undefined values', () => {
          const processusSMI: IProcessusSMI = { id: 123 };
          expectedResult = service.addProcessusSMIToCollectionIfMissing([], null, processusSMI, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(processusSMI);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
