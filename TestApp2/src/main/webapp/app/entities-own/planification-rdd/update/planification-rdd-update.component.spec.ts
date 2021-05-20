jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PlanificationRDDService } from '../service/planification-rdd.service';
import { IPlanificationRDD, PlanificationRDD } from '../planification-rdd.model';

import { PlanificationRDDUpdateComponent } from './planification-rdd-update.component';

describe('Component Tests', () => {
  describe('PlanificationRDD Management Update Component', () => {
    let comp: PlanificationRDDUpdateComponent;
    let fixture: ComponentFixture<PlanificationRDDUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let planificationRDDService: PlanificationRDDService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PlanificationRDDUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PlanificationRDDUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlanificationRDDUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      planificationRDDService = TestBed.inject(PlanificationRDDService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const planificationRDD: IPlanificationRDD = { id: 456 };

        activatedRoute.data = of({ planificationRDD });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(planificationRDD));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const planificationRDD = { id: 123 };
        spyOn(planificationRDDService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ planificationRDD });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: planificationRDD }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(planificationRDDService.update).toHaveBeenCalledWith(planificationRDD);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const planificationRDD = new PlanificationRDD();
        spyOn(planificationRDDService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ planificationRDD });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: planificationRDD }));
        saveSubject.complete();

        // THEN
        expect(planificationRDDService.create).toHaveBeenCalledWith(planificationRDD);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const planificationRDD = { id: 123 };
        spyOn(planificationRDDService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ planificationRDD });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(planificationRDDService.update).toHaveBeenCalledWith(planificationRDD);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
