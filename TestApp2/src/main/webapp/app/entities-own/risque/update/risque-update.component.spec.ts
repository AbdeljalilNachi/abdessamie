jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RisqueService } from '../service/risque.service';
import { IRisque, Risque } from '../risque.model';
import { IAction } from 'app/entities-own/action/action.model';
import { ActionService } from 'app/entities-own/action/service/action.service';

import { IUser } from 'app/entities-own/user/user.model';
import { UserService } from 'app/entities-own/user/user.service';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities-own/processus-smi/service/processus-smi.service';

import { RisqueUpdateComponent } from './risque-update.component';

describe('Component Tests', () => {
  describe('Risque Management Update Component', () => {
    let comp: RisqueUpdateComponent;
    let fixture: ComponentFixture<RisqueUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let risqueService: RisqueService;
    let actionService: ActionService;
    let userService: UserService;
    let processusSMIService: ProcessusSMIService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RisqueUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RisqueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RisqueUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      risqueService = TestBed.inject(RisqueService);
      actionService = TestBed.inject(ActionService);
      userService = TestBed.inject(UserService);
      processusSMIService = TestBed.inject(ProcessusSMIService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Action query and add missing value', () => {
        const risque: IRisque = { id: 456 };
        const action: IAction = { id: 72954 };
        risque.action = action;

        const actionCollection: IAction[] = [{ id: 22555 }];
        spyOn(actionService, 'query').and.returnValue(of(new HttpResponse({ body: actionCollection })));
        const additionalActions = [action];
        const expectedCollection: IAction[] = [...additionalActions, ...actionCollection];
        spyOn(actionService, 'addActionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ risque });
        comp.ngOnInit();

        expect(actionService.query).toHaveBeenCalled();
        expect(actionService.addActionToCollectionIfMissing).toHaveBeenCalledWith(actionCollection, ...additionalActions);
        expect(comp.actionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const risque: IRisque = { id: 456 };
        const delegue: IUser = { id: 78292 };
        risque.delegue = delegue;

        const userCollection: IUser[] = [{ id: 69083 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [delegue];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ risque });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ProcessusSMI query and add missing value', () => {
        const risque: IRisque = { id: 456 };
        const processus: IProcessusSMI = { id: 77844 };
        risque.processus = processus;

        const processusSMICollection: IProcessusSMI[] = [{ id: 95562 }];
        spyOn(processusSMIService, 'query').and.returnValue(of(new HttpResponse({ body: processusSMICollection })));
        const additionalProcessusSMIS = [processus];
        const expectedCollection: IProcessusSMI[] = [...additionalProcessusSMIS, ...processusSMICollection];
        spyOn(processusSMIService, 'addProcessusSMIToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ risque });
        comp.ngOnInit();

        expect(processusSMIService.query).toHaveBeenCalled();
        expect(processusSMIService.addProcessusSMIToCollectionIfMissing).toHaveBeenCalledWith(
          processusSMICollection,
          ...additionalProcessusSMIS
        );
        expect(comp.processusSMISSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const risque: IRisque = { id: 456 };
        const action: IAction = { id: 44893 };
        risque.action = action;
        const delegue: IUser = { id: 87770 };
        risque.delegue = delegue;
        const processus: IProcessusSMI = { id: 17803 };
        risque.processus = processus;

        activatedRoute.data = of({ risque });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(risque));
        expect(comp.actionsSharedCollection).toContain(action);
        expect(comp.usersSharedCollection).toContain(delegue);
        expect(comp.processusSMISSharedCollection).toContain(processus);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const risque = { id: 123 };
        spyOn(risqueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ risque });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: risque }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(risqueService.update).toHaveBeenCalledWith(risque);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const risque = new Risque();
        spyOn(risqueService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ risque });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: risque }));
        saveSubject.complete();

        // THEN
        expect(risqueService.create).toHaveBeenCalledWith(risque);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const risque = { id: 123 };
        spyOn(risqueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ risque });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(risqueService.update).toHaveBeenCalledWith(risque);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackActionById', () => {
        it('Should return tracked Action primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackActionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackProcessusSMIById', () => {
        it('Should return tracked ProcessusSMI primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProcessusSMIById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
