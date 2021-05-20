import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TypeConstatAudit } from 'app/entities/enumerations/type-constat-audit.model';
import { IConstatAudit, ConstatAudit } from '../constat-audit.model';

import { ConstatAuditService } from './constat-audit.service';

describe('Service Tests', () => {
  describe('ConstatAudit Service', () => {
    let service: ConstatAuditService;
    let httpMock: HttpTestingController;
    let elemDefault: IConstatAudit;
    let expectedResult: IConstatAudit | IConstatAudit[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ConstatAuditService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        type: TypeConstatAudit.PF,
        constat: 'AAAAAAA',
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

      it('should create a ConstatAudit', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ConstatAudit()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ConstatAudit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            type: 'BBBBBB',
            constat: 'BBBBBB',
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

      it('should partial update a ConstatAudit', () => {
        const patchObject = Object.assign(
          {
            constat: 'BBBBBB',
            origine: 'BBBBBB',
          },
          new ConstatAudit()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ConstatAudit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            type: 'BBBBBB',
            constat: 'BBBBBB',
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

      it('should delete a ConstatAudit', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addConstatAuditToCollectionIfMissing', () => {
        it('should add a ConstatAudit to an empty array', () => {
          const constatAudit: IConstatAudit = { id: 123 };
          expectedResult = service.addConstatAuditToCollectionIfMissing([], constatAudit);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(constatAudit);
        });

        it('should not add a ConstatAudit to an array that contains it', () => {
          const constatAudit: IConstatAudit = { id: 123 };
          const constatAuditCollection: IConstatAudit[] = [
            {
              ...constatAudit,
            },
            { id: 456 },
          ];
          expectedResult = service.addConstatAuditToCollectionIfMissing(constatAuditCollection, constatAudit);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ConstatAudit to an array that doesn't contain it", () => {
          const constatAudit: IConstatAudit = { id: 123 };
          const constatAuditCollection: IConstatAudit[] = [{ id: 456 }];
          expectedResult = service.addConstatAuditToCollectionIfMissing(constatAuditCollection, constatAudit);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(constatAudit);
        });

        it('should add only unique ConstatAudit to an array', () => {
          const constatAuditArray: IConstatAudit[] = [{ id: 123 }, { id: 456 }, { id: 80239 }];
          const constatAuditCollection: IConstatAudit[] = [{ id: 123 }];
          expectedResult = service.addConstatAuditToCollectionIfMissing(constatAuditCollection, ...constatAuditArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const constatAudit: IConstatAudit = { id: 123 };
          const constatAudit2: IConstatAudit = { id: 456 };
          expectedResult = service.addConstatAuditToCollectionIfMissing([], constatAudit, constatAudit2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(constatAudit);
          expect(expectedResult).toContain(constatAudit2);
        });

        it('should accept null and undefined values', () => {
          const constatAudit: IConstatAudit = { id: 123 };
          expectedResult = service.addConstatAuditToCollectionIfMissing([], null, constatAudit, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(constatAudit);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
