jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ConstatAuditService } from '../service/constat-audit.service';
import { IConstatAudit, ConstatAudit } from '../constat-audit.model';
import { IAction } from 'app/entities-own/action/action.model';
import { ActionService } from 'app/entities-own/action/service/action.service';

import { IUser } from 'app/entities-own/user/user.model';
import { UserService } from 'app/entities-own/user/user.service';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities-own/processus-smi/service/processus-smi.service';
import { IAudit } from 'app/entities-own/audit/audit.model';
import { AuditService } from 'app/entities-own/audit/service/audit.service';

import { ConstatAuditUpdateComponent } from './constat-audit-update.component';

describe('Component Tests', () => {
  describe('ConstatAudit Management Update Component', () => {
    let comp: ConstatAuditUpdateComponent;
    let fixture: ComponentFixture<ConstatAuditUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let constatAuditService: ConstatAuditService;
    let actionService: ActionService;
    let userService: UserService;
    let processusSMIService: ProcessusSMIService;
    let auditService: AuditService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ConstatAuditUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ConstatAuditUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConstatAuditUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      constatAuditService = TestBed.inject(ConstatAuditService);
      actionService = TestBed.inject(ActionService);
      userService = TestBed.inject(UserService);
      processusSMIService = TestBed.inject(ProcessusSMIService);
      auditService = TestBed.inject(AuditService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Action query and add missing value', () => {
        const constatAudit: IConstatAudit = { id: 456 };
        const action: IAction = { id: 7464 };
        constatAudit.action = action;

        const actionCollection: IAction[] = [{ id: 83238 }];
        spyOn(actionService, 'query').and.returnValue(of(new HttpResponse({ body: actionCollection })));
        const additionalActions = [action];
        const expectedCollection: IAction[] = [...additionalActions, ...actionCollection];
        spyOn(actionService, 'addActionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ constatAudit });
        comp.ngOnInit();

        expect(actionService.query).toHaveBeenCalled();
        expect(actionService.addActionToCollectionIfMissing).toHaveBeenCalledWith(actionCollection, ...additionalActions);
        expect(comp.actionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const constatAudit: IConstatAudit = { id: 456 };
        const delegue: IUser = { id: 58194 };
        constatAudit.delegue = delegue;

        const userCollection: IUser[] = [{ id: 63649 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [delegue];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ constatAudit });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ProcessusSMI query and add missing value', () => {
        const constatAudit: IConstatAudit = { id: 456 };
        const processus: IProcessusSMI = { id: 42988 };
        constatAudit.processus = processus;

        const processusSMICollection: IProcessusSMI[] = [{ id: 66089 }];
        spyOn(processusSMIService, 'query').and.returnValue(of(new HttpResponse({ body: processusSMICollection })));
        const additionalProcessusSMIS = [processus];
        const expectedCollection: IProcessusSMI[] = [...additionalProcessusSMIS, ...processusSMICollection];
        spyOn(processusSMIService, 'addProcessusSMIToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ constatAudit });
        comp.ngOnInit();

        expect(processusSMIService.query).toHaveBeenCalled();
        expect(processusSMIService.addProcessusSMIToCollectionIfMissing).toHaveBeenCalledWith(
          processusSMICollection,
          ...additionalProcessusSMIS
        );
        expect(comp.processusSMISSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Audit query and add missing value', () => {
        const constatAudit: IConstatAudit = { id: 456 };
        const audit: IAudit = { id: 90971 };
        constatAudit.audit = audit;

        const auditCollection: IAudit[] = [{ id: 66880 }];
        spyOn(auditService, 'query').and.returnValue(of(new HttpResponse({ body: auditCollection })));
        const additionalAudits = [audit];
        const expectedCollection: IAudit[] = [...additionalAudits, ...auditCollection];
        spyOn(auditService, 'addAuditToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ constatAudit });
        comp.ngOnInit();

        expect(auditService.query).toHaveBeenCalled();
        expect(auditService.addAuditToCollectionIfMissing).toHaveBeenCalledWith(auditCollection, ...additionalAudits);
        expect(comp.auditsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const constatAudit: IConstatAudit = { id: 456 };
        const action: IAction = { id: 151 };
        constatAudit.action = action;
        const delegue: IUser = { id: 65999 };
        constatAudit.delegue = delegue;
        const processus: IProcessusSMI = { id: 71597 };
        constatAudit.processus = processus;
        const audit: IAudit = { id: 8203 };
        constatAudit.audit = audit;

        activatedRoute.data = of({ constatAudit });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(constatAudit));
        expect(comp.actionsSharedCollection).toContain(action);
        expect(comp.usersSharedCollection).toContain(delegue);
        expect(comp.processusSMISSharedCollection).toContain(processus);
        expect(comp.auditsSharedCollection).toContain(audit);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const constatAudit = { id: 123 };
        spyOn(constatAuditService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ constatAudit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: constatAudit }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(constatAuditService.update).toHaveBeenCalledWith(constatAudit);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const constatAudit = new ConstatAudit();
        spyOn(constatAuditService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ constatAudit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: constatAudit }));
        saveSubject.complete();

        // THEN
        expect(constatAuditService.create).toHaveBeenCalledWith(constatAudit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const constatAudit = { id: 123 };
        spyOn(constatAuditService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ constatAudit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(constatAuditService.update).toHaveBeenCalledWith(constatAudit);
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

      describe('trackAuditById', () => {
        it('Should return tracked Audit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAuditById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
