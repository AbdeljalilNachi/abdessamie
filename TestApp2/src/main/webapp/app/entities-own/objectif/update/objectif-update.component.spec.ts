jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ObjectifService } from '../service/objectif.service';
import { IObjectif, Objectif } from '../objectif.model';
import { IAction } from 'app/entities-own/action/action.model';
import { ActionService } from 'app/entities-own/action/service/action.service';

import { IUser } from 'app/entities-own/user/user.model';
import { UserService } from 'app/entities-own/user/user.service';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities-own/processus-smi/service/processus-smi.service';
import { IIndicateurSMI } from 'app/entities-own/indicateur-smi/indicateur-smi.model';
import { IndicateurSMIService } from 'app/entities-own/indicateur-smi/service/indicateur-smi.service';

import { ObjectifUpdateComponent } from './objectif-update.component';

describe('Component Tests', () => {
  describe('Objectif Management Update Component', () => {
    let comp: ObjectifUpdateComponent;
    let fixture: ComponentFixture<ObjectifUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let objectifService: ObjectifService;
    let actionService: ActionService;
    let userService: UserService;
    let processusSMIService: ProcessusSMIService;
    let indicateurSMIService: IndicateurSMIService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ObjectifUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ObjectifUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ObjectifUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      objectifService = TestBed.inject(ObjectifService);
      actionService = TestBed.inject(ActionService);
      userService = TestBed.inject(UserService);
      processusSMIService = TestBed.inject(ProcessusSMIService);
      indicateurSMIService = TestBed.inject(IndicateurSMIService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Action query and add missing value', () => {
        const objectif: IObjectif = { id: 456 };
        const action: IAction = { id: 17583 };
        objectif.action = action;

        const actionCollection: IAction[] = [{ id: 61694 }];
        spyOn(actionService, 'query').and.returnValue(of(new HttpResponse({ body: actionCollection })));
        const additionalActions = [action];
        const expectedCollection: IAction[] = [...additionalActions, ...actionCollection];
        spyOn(actionService, 'addActionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ objectif });
        comp.ngOnInit();

        expect(actionService.query).toHaveBeenCalled();
        expect(actionService.addActionToCollectionIfMissing).toHaveBeenCalledWith(actionCollection, ...additionalActions);
        expect(comp.actionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const objectif: IObjectif = { id: 456 };
        const delegue: IUser = { id: 98651 };
        objectif.delegue = delegue;

        const userCollection: IUser[] = [{ id: 16458 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [delegue];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ objectif });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ProcessusSMI query and add missing value', () => {
        const objectif: IObjectif = { id: 456 };
        const processus: IProcessusSMI = { id: 55156 };
        objectif.processus = processus;

        const processusSMICollection: IProcessusSMI[] = [{ id: 37811 }];
        spyOn(processusSMIService, 'query').and.returnValue(of(new HttpResponse({ body: processusSMICollection })));
        const additionalProcessusSMIS = [processus];
        const expectedCollection: IProcessusSMI[] = [...additionalProcessusSMIS, ...processusSMICollection];
        spyOn(processusSMIService, 'addProcessusSMIToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ objectif });
        comp.ngOnInit();

        expect(processusSMIService.query).toHaveBeenCalled();
        expect(processusSMIService.addProcessusSMIToCollectionIfMissing).toHaveBeenCalledWith(
          processusSMICollection,
          ...additionalProcessusSMIS
        );
        expect(comp.processusSMISSharedCollection).toEqual(expectedCollection);
      });

      it('Should call IndicateurSMI query and add missing value', () => {
        const objectif: IObjectif = { id: 456 };
        const indicateur: IIndicateurSMI = { id: 10968 };
        objectif.indicateur = indicateur;

        const indicateurSMICollection: IIndicateurSMI[] = [{ id: 30217 }];
        spyOn(indicateurSMIService, 'query').and.returnValue(of(new HttpResponse({ body: indicateurSMICollection })));
        const additionalIndicateurSMIS = [indicateur];
        const expectedCollection: IIndicateurSMI[] = [...additionalIndicateurSMIS, ...indicateurSMICollection];
        spyOn(indicateurSMIService, 'addIndicateurSMIToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ objectif });
        comp.ngOnInit();

        expect(indicateurSMIService.query).toHaveBeenCalled();
        expect(indicateurSMIService.addIndicateurSMIToCollectionIfMissing).toHaveBeenCalledWith(
          indicateurSMICollection,
          ...additionalIndicateurSMIS
        );
        expect(comp.indicateurSMISSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const objectif: IObjectif = { id: 456 };
        const action: IAction = { id: 85320 };
        objectif.action = action;
        const delegue: IUser = { id: 97077 };
        objectif.delegue = delegue;
        const processus: IProcessusSMI = { id: 1542 };
        objectif.processus = processus;
        const indicateur: IIndicateurSMI = { id: 23270 };
        objectif.indicateur = indicateur;

        activatedRoute.data = of({ objectif });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(objectif));
        expect(comp.actionsSharedCollection).toContain(action);
        expect(comp.usersSharedCollection).toContain(delegue);
        expect(comp.processusSMISSharedCollection).toContain(processus);
        expect(comp.indicateurSMISSharedCollection).toContain(indicateur);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const objectif = { id: 123 };
        spyOn(objectifService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ objectif });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: objectif }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(objectifService.update).toHaveBeenCalledWith(objectif);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const objectif = new Objectif();
        spyOn(objectifService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ objectif });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: objectif }));
        saveSubject.complete();

        // THEN
        expect(objectifService.create).toHaveBeenCalledWith(objectif);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const objectif = { id: 123 };
        spyOn(objectifService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ objectif });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(objectifService.update).toHaveBeenCalledWith(objectif);
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

      describe('trackIndicateurSMIById', () => {
        it('Should return tracked IndicateurSMI primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackIndicateurSMIById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
