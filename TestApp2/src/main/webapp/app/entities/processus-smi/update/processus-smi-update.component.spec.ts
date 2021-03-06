jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProcessusSMIService } from '../service/processus-smi.service';
import { IProcessusSMI, ProcessusSMI } from '../processus-smi.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ProcessusSMIUpdateComponent } from './processus-smi-update.component';

describe('Component Tests', () => {
  describe('ProcessusSMI Management Update Component', () => {
    let comp: ProcessusSMIUpdateComponent;
    let fixture: ComponentFixture<ProcessusSMIUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let processusSMIService: ProcessusSMIService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProcessusSMIUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProcessusSMIUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProcessusSMIUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      processusSMIService = TestBed.inject(ProcessusSMIService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const processusSMI: IProcessusSMI = { id: 456 };
        const pilote: IUser = { id: 86282 };
        processusSMI.pilote = pilote;

        const userCollection: IUser[] = [{ id: 29294 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [pilote];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ processusSMI });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const processusSMI: IProcessusSMI = { id: 456 };
        const pilote: IUser = { id: 69424 };
        processusSMI.pilote = pilote;

        activatedRoute.data = of({ processusSMI });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(processusSMI));
        expect(comp.usersSharedCollection).toContain(pilote);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const processusSMI = { id: 123 };
        spyOn(processusSMIService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ processusSMI });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: processusSMI }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(processusSMIService.update).toHaveBeenCalledWith(processusSMI);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const processusSMI = new ProcessusSMI();
        spyOn(processusSMIService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ processusSMI });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: processusSMI }));
        saveSubject.complete();

        // THEN
        expect(processusSMIService.create).toHaveBeenCalledWith(processusSMI);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const processusSMI = { id: 123 };
        spyOn(processusSMIService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ processusSMI });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(processusSMIService.update).toHaveBeenCalledWith(processusSMI);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
