jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AnalyseSWOTService } from '../service/analyse-swot.service';
import { IAnalyseSWOT, AnalyseSWOT } from '../analyse-swot.model';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities-own/processus-smi/service/processus-smi.service';

import { AnalyseSWOTUpdateComponent } from './analyse-swot-update.component';

describe('Component Tests', () => {
  describe('AnalyseSWOT Management Update Component', () => {
    let comp: AnalyseSWOTUpdateComponent;
    let fixture: ComponentFixture<AnalyseSWOTUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let analyseSWOTService: AnalyseSWOTService;
    let processusSMIService: ProcessusSMIService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AnalyseSWOTUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AnalyseSWOTUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AnalyseSWOTUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      analyseSWOTService = TestBed.inject(AnalyseSWOTService);
      processusSMIService = TestBed.inject(ProcessusSMIService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ProcessusSMI query and add missing value', () => {
        const analyseSWOT: IAnalyseSWOT = { id: 456 };
        const processus: IProcessusSMI = { id: 98111 };
        analyseSWOT.processus = processus;

        const processusSMICollection: IProcessusSMI[] = [{ id: 52462 }];
        spyOn(processusSMIService, 'query').and.returnValue(of(new HttpResponse({ body: processusSMICollection })));
        const additionalProcessusSMIS = [processus];
        const expectedCollection: IProcessusSMI[] = [...additionalProcessusSMIS, ...processusSMICollection];
        spyOn(processusSMIService, 'addProcessusSMIToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ analyseSWOT });
        comp.ngOnInit();

        expect(processusSMIService.query).toHaveBeenCalled();
        expect(processusSMIService.addProcessusSMIToCollectionIfMissing).toHaveBeenCalledWith(
          processusSMICollection,
          ...additionalProcessusSMIS
        );
        expect(comp.processusSMISSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const analyseSWOT: IAnalyseSWOT = { id: 456 };
        const processus: IProcessusSMI = { id: 89349 };
        analyseSWOT.processus = processus;

        activatedRoute.data = of({ analyseSWOT });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(analyseSWOT));
        expect(comp.processusSMISSharedCollection).toContain(processus);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const analyseSWOT = { id: 123 };
        spyOn(analyseSWOTService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ analyseSWOT });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: analyseSWOT }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(analyseSWOTService.update).toHaveBeenCalledWith(analyseSWOT);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const analyseSWOT = new AnalyseSWOT();
        spyOn(analyseSWOTService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ analyseSWOT });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: analyseSWOT }));
        saveSubject.complete();

        // THEN
        expect(analyseSWOTService.create).toHaveBeenCalledWith(analyseSWOT);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const analyseSWOT = { id: 123 };
        spyOn(analyseSWOTService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ analyseSWOT });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(analyseSWOTService.update).toHaveBeenCalledWith(analyseSWOT);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
