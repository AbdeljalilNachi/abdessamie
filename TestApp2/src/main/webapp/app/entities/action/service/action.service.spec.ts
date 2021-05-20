import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { TypeAction } from 'app/entities/enumerations/type-action.model';
import { Statut } from 'app/entities/enumerations/statut.model';
import { Efficace } from 'app/entities/enumerations/efficace.model';
import { IAction, Action } from '../action.model';

import { ActionService } from './action.service';

describe('Service Tests', () => {
  describe('Action Service', () => {
    let service: ActionService;
    let httpMock: HttpTestingController;
    let elemDefault: IAction;
    let expectedResult: IAction | IAction[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ActionService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        datePlanification: currentDate,
        action: 'AAAAAAA',
        type: TypeAction.ACTION_FACE_AU_RISQUE,
        delai: currentDate,
        avancement: 'AAAAAAA',
        realisee: false,
        critereResultat: 'AAAAAAA',
        ressourcesNecessaires: 'AAAAAAA',
        statut: Statut.EN_COURS,
        efficace: Efficace.EFFICACE,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            datePlanification: currentDate.format(DATE_FORMAT),
            delai: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Action', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            datePlanification: currentDate.format(DATE_FORMAT),
            delai: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            datePlanification: currentDate,
            delai: currentDate,
          },
          returnedFromService
        );

        service.create(new Action()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Action', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            datePlanification: currentDate.format(DATE_FORMAT),
            action: 'BBBBBB',
            type: 'BBBBBB',
            delai: currentDate.format(DATE_FORMAT),
            avancement: 'BBBBBB',
            realisee: true,
            critereResultat: 'BBBBBB',
            ressourcesNecessaires: 'BBBBBB',
            statut: 'BBBBBB',
            efficace: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            datePlanification: currentDate,
            delai: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Action', () => {
        const patchObject = Object.assign(
          {
            datePlanification: currentDate.format(DATE_FORMAT),
            type: 'BBBBBB',
            delai: currentDate.format(DATE_FORMAT),
            realisee: true,
            ressourcesNecessaires: 'BBBBBB',
            efficace: 'BBBBBB',
          },
          new Action()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            datePlanification: currentDate,
            delai: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Action', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            datePlanification: currentDate.format(DATE_FORMAT),
            action: 'BBBBBB',
            type: 'BBBBBB',
            delai: currentDate.format(DATE_FORMAT),
            avancement: 'BBBBBB',
            realisee: true,
            critereResultat: 'BBBBBB',
            ressourcesNecessaires: 'BBBBBB',
            statut: 'BBBBBB',
            efficace: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            datePlanification: currentDate,
            delai: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Action', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addActionToCollectionIfMissing', () => {
        it('should add a Action to an empty array', () => {
          const action: IAction = { id: 123 };
          expectedResult = service.addActionToCollectionIfMissing([], action);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(action);
        });

        it('should not add a Action to an array that contains it', () => {
          const action: IAction = { id: 123 };
          const actionCollection: IAction[] = [
            {
              ...action,
            },
            { id: 456 },
          ];
          expectedResult = service.addActionToCollectionIfMissing(actionCollection, action);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Action to an array that doesn't contain it", () => {
          const action: IAction = { id: 123 };
          const actionCollection: IAction[] = [{ id: 456 }];
          expectedResult = service.addActionToCollectionIfMissing(actionCollection, action);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(action);
        });

        it('should add only unique Action to an array', () => {
          const actionArray: IAction[] = [{ id: 123 }, { id: 456 }, { id: 74940 }];
          const actionCollection: IAction[] = [{ id: 123 }];
          expectedResult = service.addActionToCollectionIfMissing(actionCollection, ...actionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const action: IAction = { id: 123 };
          const action2: IAction = { id: 456 };
          expectedResult = service.addActionToCollectionIfMissing([], action, action2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(action);
          expect(expectedResult).toContain(action2);
        });

        it('should accept null and undefined values', () => {
          const action: IAction = { id: 123 };
          expectedResult = service.addActionToCollectionIfMissing([], null, action, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(action);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
