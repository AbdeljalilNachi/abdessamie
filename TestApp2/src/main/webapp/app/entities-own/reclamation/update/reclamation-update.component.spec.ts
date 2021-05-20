jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReclamationService } from '../service/reclamation.service';
import { IReclamation, Reclamation } from '../reclamation.model';
import { IAction } from 'app/entities-own/action/action.model';
import { ActionService } from 'app/entities-own/action/service/action.service';

import { IUser } from 'app/entities-own/user/user.model';
import { UserService } from 'app/entities-own/user/user.service';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities-own/processus-smi/service/processus-smi.service';

import { ReclamationUpdateComponent } from './reclamation-update.component';

describe('Component Tests', () => {
  describe('Reclamation Management Update Component', () => {
    let comp: ReclamationUpdateComponent;
    let fixture: ComponentFixture<ReclamationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let reclamationService: ReclamationService;
    let actionService: ActionService;
    let userService: UserService;
    let processusSMIService: ProcessusSMIService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReclamationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ReclamationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReclamationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      reclamationService = TestBed.inject(ReclamationService);
      actionService = TestBed.inject(ActionService);
      userService = TestBed.inject(UserService);
      processusSMIService = TestBed.inject(ProcessusSMIService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Action query and add missing value', () => {
        const reclamation: IReclamation = { id: 456 };
        const action: IAction = { id: 44048 };
        reclamation.action = action;

        const actionCollection: IAction[] = [{ id: 92354 }];
        spyOn(actionService, 'query').and.returnValue(of(new HttpResponse({ body: actionCollection })));
        const additionalActions = [action];
        const expectedCollection: IAction[] = [...additionalActions, ...actionCollection];
        spyOn(actionService, 'addActionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ reclamation });
        comp.ngOnInit();

        expect(actionService.query).toHaveBeenCalled();
        expect(actionService.addActionToCollectionIfMissing).toHaveBeenCalledWith(actionCollection, ...additionalActions);
        expect(comp.actionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const reclamation: IReclamation = { id: 456 };
        const delegue: IUser = { id: 66362 };
        reclamation.delegue = delegue;

        const userCollection: IUser[] = [{ id: 84496 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [delegue];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ reclamation });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ProcessusSMI query and add missing value', () => {
        const reclamation: IReclamation = { id: 456 };
        const processus: IProcessusSMI = { id: 85074 };
        reclamation.processus = processus;

        const processusSMICollection: IProcessusSMI[] = [{ id: 72453 }];
        spyOn(processusSMIService, 'query').and.returnValue(of(new HttpResponse({ body: processusSMICollection })));
        const additionalProcessusSMIS = [processus];
        const expectedCollection: IProcessusSMI[] = [...additionalProcessusSMIS, ...processusSMICollection];
        spyOn(processusSMIService, 'addProcessusSMIToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ reclamation });
        comp.ngOnInit();

        expect(processusSMIService.query).toHaveBeenCalled();
        expect(processusSMIService.addProcessusSMIToCollectionIfMissing).toHaveBeenCalledWith(
          processusSMICollection,
          ...additionalProcessusSMIS
        );
        expect(comp.processusSMISSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const reclamation: IReclamation = { id: 456 };
        const action: IAction = { id: 92805 };
        reclamation.action = action;
        const delegue: IUser = { id: 8555 };
        reclamation.delegue = delegue;
        const processus: IProcessusSMI = { id: 89712 };
        reclamation.processus = processus;

        activatedRoute.data = of({ reclamation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(reclamation));
        expect(comp.actionsSharedCollection).toContain(action);
        expect(comp.usersSharedCollection).toContain(delegue);
        expect(comp.processusSMISSharedCollection).toContain(processus);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const reclamation = { id: 123 };
        spyOn(reclamationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ reclamation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: reclamation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(reclamationService.update).toHaveBeenCalledWith(reclamation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const reclamation = new Reclamation();
        spyOn(reclamationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ reclamation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: reclamation }));
        saveSubject.complete();

        // THEN
        expect(reclamationService.create).toHaveBeenCalledWith(reclamation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const reclamation = { id: 123 };
        spyOn(reclamationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ reclamation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(reclamationService.update).toHaveBeenCalledWith(reclamation);
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
