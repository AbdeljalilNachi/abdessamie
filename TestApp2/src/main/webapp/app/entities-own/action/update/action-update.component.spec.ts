jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ActionService } from '../service/action.service';
import { IAction, Action } from '../action.model';

import { ActionUpdateComponent } from './action-update.component';

describe('Component Tests', () => {
  describe('Action Management Update Component', () => {
    let comp: ActionUpdateComponent;
    let fixture: ComponentFixture<ActionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let actionService: ActionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ActionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ActionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ActionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      actionService = TestBed.inject(ActionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const action: IAction = { id: 456 };

        activatedRoute.data = of({ action });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(action));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const action = { id: 123 };
        spyOn(actionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ action });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: action }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(actionService.update).toHaveBeenCalledWith(action);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const action = new Action();
        spyOn(actionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ action });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: action }));
        saveSubject.complete();

        // THEN
        expect(actionService.create).toHaveBeenCalledWith(action);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const action = { id: 123 };
        spyOn(actionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ action });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(actionService.update).toHaveBeenCalledWith(action);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
