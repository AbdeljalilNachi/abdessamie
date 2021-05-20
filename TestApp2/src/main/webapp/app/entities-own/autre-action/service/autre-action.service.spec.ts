import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAutreAction, AutreAction } from '../autre-action.model';

import { AutreActionService } from './autre-action.service';

describe('Service Tests', () => {
  describe('AutreAction Service', () => {
    let service: AutreActionService;
    let httpMock: HttpTestingController;
    let elemDefault: IAutreAction;
    let expectedResult: IAutreAction | IAutreAction[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AutreActionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        origineAction: 'AAAAAAA',
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

      it('should create a AutreAction', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AutreAction()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AutreAction', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            origineAction: 'BBBBBB',
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

      it('should partial update a AutreAction', () => {
        const patchObject = Object.assign(
          {
            origineAction: 'BBBBBB',
            origine: 'BBBBBB',
          },
          new AutreAction()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AutreAction', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            origineAction: 'BBBBBB',
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

      it('should delete a AutreAction', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAutreActionToCollectionIfMissing', () => {
        it('should add a AutreAction to an empty array', () => {
          const autreAction: IAutreAction = { id: 123 };
          expectedResult = service.addAutreActionToCollectionIfMissing([], autreAction);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(autreAction);
        });

        it('should not add a AutreAction to an array that contains it', () => {
          const autreAction: IAutreAction = { id: 123 };
          const autreActionCollection: IAutreAction[] = [
            {
              ...autreAction,
            },
            { id: 456 },
          ];
          expectedResult = service.addAutreActionToCollectionIfMissing(autreActionCollection, autreAction);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AutreAction to an array that doesn't contain it", () => {
          const autreAction: IAutreAction = { id: 123 };
          const autreActionCollection: IAutreAction[] = [{ id: 456 }];
          expectedResult = service.addAutreActionToCollectionIfMissing(autreActionCollection, autreAction);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(autreAction);
        });

        it('should add only unique AutreAction to an array', () => {
          const autreActionArray: IAutreAction[] = [{ id: 123 }, { id: 456 }, { id: 96595 }];
          const autreActionCollection: IAutreAction[] = [{ id: 123 }];
          expectedResult = service.addAutreActionToCollectionIfMissing(autreActionCollection, ...autreActionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const autreAction: IAutreAction = { id: 123 };
          const autreAction2: IAutreAction = { id: 456 };
          expectedResult = service.addAutreActionToCollectionIfMissing([], autreAction, autreAction2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(autreAction);
          expect(expectedResult).toContain(autreAction2);
        });

        it('should accept null and undefined values', () => {
          const autreAction: IAutreAction = { id: 123 };
          expectedResult = service.addAutreActionToCollectionIfMissing([], null, autreAction, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(autreAction);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
