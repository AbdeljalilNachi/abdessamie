jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NonConformiteService } from '../service/non-conformite.service';
import { INonConformite, NonConformite } from '../non-conformite.model';
import { IAction } from 'app/entities-own/action/action.model';
import { ActionService } from 'app/entities-own/action/service/action.service';

import { IUser } from 'app/entities-own/user/user.model';
import { UserService } from 'app/entities-own/user/user.service';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities-own/processus-smi/service/processus-smi.service';

import { NonConformiteUpdateComponent } from './non-conformite-update.component';

describe('Component Tests', () => {
  describe('NonConformite Management Update Component', () => {
    let comp: NonConformiteUpdateComponent;
    let fixture: ComponentFixture<NonConformiteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let nonConformiteService: NonConformiteService;
    let actionService: ActionService;
    let userService: UserService;
    let processusSMIService: ProcessusSMIService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NonConformiteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(NonConformiteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NonConformiteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      nonConformiteService = TestBed.inject(NonConformiteService);
      actionService = TestBed.inject(ActionService);
      userService = TestBed.inject(UserService);
      processusSMIService = TestBed.inject(ProcessusSMIService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Action query and add missing value', () => {
        const nonConformite: INonConformite = { id: 456 };
        const action: IAction = { id: 81668 };
        nonConformite.action = action;

        const actionCollection: IAction[] = [{ id: 57437 }];
        spyOn(actionService, 'query').and.returnValue(of(new HttpResponse({ body: actionCollection })));
        const additionalActions = [action];
        const expectedCollection: IAction[] = [...additionalActions, ...actionCollection];
        spyOn(actionService, 'addActionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ nonConformite });
        comp.ngOnInit();

        expect(actionService.query).toHaveBeenCalled();
        expect(actionService.addActionToCollectionIfMissing).toHaveBeenCalledWith(actionCollection, ...additionalActions);
        expect(comp.actionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const nonConformite: INonConformite = { id: 456 };
        const delegue: IUser = { id: 18516 };
        nonConformite.delegue = delegue;

        const userCollection: IUser[] = [{ id: 71569 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [delegue];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ nonConformite });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ProcessusSMI query and add missing value', () => {
        const nonConformite: INonConformite = { id: 456 };
        const processus: IProcessusSMI = { id: 9656 };
        nonConformite.processus = processus;

        const processusSMICollection: IProcessusSMI[] = [{ id: 74043 }];
        spyOn(processusSMIService, 'query').and.returnValue(of(new HttpResponse({ body: processusSMICollection })));
        const additionalProcessusSMIS = [processus];
        const expectedCollection: IProcessusSMI[] = [...additionalProcessusSMIS, ...processusSMICollection];
        spyOn(processusSMIService, 'addProcessusSMIToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ nonConformite });
        comp.ngOnInit();

        expect(processusSMIService.query).toHaveBeenCalled();
        expect(processusSMIService.addProcessusSMIToCollectionIfMissing).toHaveBeenCalledWith(
          processusSMICollection,
          ...additionalProcessusSMIS
        );
        expect(comp.processusSMISSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const nonConformite: INonConformite = { id: 456 };
        const action: IAction = { id: 45503 };
        nonConformite.action = action;
        const delegue: IUser = { id: 52354 };
        nonConformite.delegue = delegue;
        const processus: IProcessusSMI = { id: 67360 };
        nonConformite.processus = processus;

        activatedRoute.data = of({ nonConformite });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(nonConformite));
        expect(comp.actionsSharedCollection).toContain(action);
        expect(comp.usersSharedCollection).toContain(delegue);
        expect(comp.processusSMISSharedCollection).toContain(processus);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const nonConformite = { id: 123 };
        spyOn(nonConformiteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ nonConformite });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: nonConformite }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(nonConformiteService.update).toHaveBeenCalledWith(nonConformite);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const nonConformite = new NonConformite();
        spyOn(nonConformiteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ nonConformite });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: nonConformite }));
        saveSubject.complete();

        // THEN
        expect(nonConformiteService.create).toHaveBeenCalledWith(nonConformite);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const nonConformite = { id: 123 };
        spyOn(nonConformiteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ nonConformite });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(nonConformiteService.update).toHaveBeenCalledWith(nonConformite);
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
