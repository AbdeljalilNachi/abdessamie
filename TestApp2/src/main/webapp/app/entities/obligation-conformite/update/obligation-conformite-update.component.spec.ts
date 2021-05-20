jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ObligationConformiteService } from '../service/obligation-conformite.service';
import { IObligationConformite, ObligationConformite } from '../obligation-conformite.model';
import { IAction } from 'app/entities/action/action.model';
import { ActionService } from 'app/entities/action/service/action.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProcessusSMI } from 'app/entities/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities/processus-smi/service/processus-smi.service';

import { ObligationConformiteUpdateComponent } from './obligation-conformite-update.component';

describe('Component Tests', () => {
  describe('ObligationConformite Management Update Component', () => {
    let comp: ObligationConformiteUpdateComponent;
    let fixture: ComponentFixture<ObligationConformiteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let obligationConformiteService: ObligationConformiteService;
    let actionService: ActionService;
    let userService: UserService;
    let processusSMIService: ProcessusSMIService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ObligationConformiteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ObligationConformiteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ObligationConformiteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      obligationConformiteService = TestBed.inject(ObligationConformiteService);
      actionService = TestBed.inject(ActionService);
      userService = TestBed.inject(UserService);
      processusSMIService = TestBed.inject(ProcessusSMIService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Action query and add missing value', () => {
        const obligationConformite: IObligationConformite = { id: 456 };
        const action: IAction = { id: 69085 };
        obligationConformite.action = action;

        const actionCollection: IAction[] = [{ id: 55506 }];
        spyOn(actionService, 'query').and.returnValue(of(new HttpResponse({ body: actionCollection })));
        const additionalActions = [action];
        const expectedCollection: IAction[] = [...additionalActions, ...actionCollection];
        spyOn(actionService, 'addActionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ obligationConformite });
        comp.ngOnInit();

        expect(actionService.query).toHaveBeenCalled();
        expect(actionService.addActionToCollectionIfMissing).toHaveBeenCalledWith(actionCollection, ...additionalActions);
        expect(comp.actionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const obligationConformite: IObligationConformite = { id: 456 };
        const delegue: IUser = { id: 68795 };
        obligationConformite.delegue = delegue;

        const userCollection: IUser[] = [{ id: 73098 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [delegue];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ obligationConformite });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ProcessusSMI query and add missing value', () => {
        const obligationConformite: IObligationConformite = { id: 456 };
        const processus: IProcessusSMI = { id: 11946 };
        obligationConformite.processus = processus;

        const processusSMICollection: IProcessusSMI[] = [{ id: 66016 }];
        spyOn(processusSMIService, 'query').and.returnValue(of(new HttpResponse({ body: processusSMICollection })));
        const additionalProcessusSMIS = [processus];
        const expectedCollection: IProcessusSMI[] = [...additionalProcessusSMIS, ...processusSMICollection];
        spyOn(processusSMIService, 'addProcessusSMIToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ obligationConformite });
        comp.ngOnInit();

        expect(processusSMIService.query).toHaveBeenCalled();
        expect(processusSMIService.addProcessusSMIToCollectionIfMissing).toHaveBeenCalledWith(
          processusSMICollection,
          ...additionalProcessusSMIS
        );
        expect(comp.processusSMISSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const obligationConformite: IObligationConformite = { id: 456 };
        const action: IAction = { id: 89581 };
        obligationConformite.action = action;
        const delegue: IUser = { id: 96571 };
        obligationConformite.delegue = delegue;
        const processus: IProcessusSMI = { id: 7338 };
        obligationConformite.processus = processus;

        activatedRoute.data = of({ obligationConformite });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(obligationConformite));
        expect(comp.actionsSharedCollection).toContain(action);
        expect(comp.usersSharedCollection).toContain(delegue);
        expect(comp.processusSMISSharedCollection).toContain(processus);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const obligationConformite = { id: 123 };
        spyOn(obligationConformiteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ obligationConformite });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: obligationConformite }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(obligationConformiteService.update).toHaveBeenCalledWith(obligationConformite);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const obligationConformite = new ObligationConformite();
        spyOn(obligationConformiteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ obligationConformite });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: obligationConformite }));
        saveSubject.complete();

        // THEN
        expect(obligationConformiteService.create).toHaveBeenCalledWith(obligationConformite);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const obligationConformite = { id: 123 };
        spyOn(obligationConformiteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ obligationConformite });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(obligationConformiteService.update).toHaveBeenCalledWith(obligationConformite);
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
