jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BesoinPIService } from '../service/besoin-pi.service';
import { IBesoinPI, BesoinPI } from '../besoin-pi.model';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities-own/processus-smi/service/processus-smi.service';

import { BesoinPIUpdateComponent } from './besoin-pi-update.component';

describe('Component Tests', () => {
  describe('BesoinPI Management Update Component', () => {
    let comp: BesoinPIUpdateComponent;
    let fixture: ComponentFixture<BesoinPIUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let besoinPIService: BesoinPIService;
    let processusSMIService: ProcessusSMIService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BesoinPIUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BesoinPIUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BesoinPIUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      besoinPIService = TestBed.inject(BesoinPIService);
      processusSMIService = TestBed.inject(ProcessusSMIService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ProcessusSMI query and add missing value', () => {
        const besoinPI: IBesoinPI = { id: 456 };
        const processus: IProcessusSMI = { id: 60050 };
        besoinPI.processus = processus;

        const processusSMICollection: IProcessusSMI[] = [{ id: 7592 }];
        spyOn(processusSMIService, 'query').and.returnValue(of(new HttpResponse({ body: processusSMICollection })));
        const additionalProcessusSMIS = [processus];
        const expectedCollection: IProcessusSMI[] = [...additionalProcessusSMIS, ...processusSMICollection];
        spyOn(processusSMIService, 'addProcessusSMIToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ besoinPI });
        comp.ngOnInit();

        expect(processusSMIService.query).toHaveBeenCalled();
        expect(processusSMIService.addProcessusSMIToCollectionIfMissing).toHaveBeenCalledWith(
          processusSMICollection,
          ...additionalProcessusSMIS
        );
        expect(comp.processusSMISSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const besoinPI: IBesoinPI = { id: 456 };
        const processus: IProcessusSMI = { id: 64867 };
        besoinPI.processus = processus;

        activatedRoute.data = of({ besoinPI });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(besoinPI));
        expect(comp.processusSMISSharedCollection).toContain(processus);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const besoinPI = { id: 123 };
        spyOn(besoinPIService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ besoinPI });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: besoinPI }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(besoinPIService.update).toHaveBeenCalledWith(besoinPI);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const besoinPI = new BesoinPI();
        spyOn(besoinPIService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ besoinPI });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: besoinPI }));
        saveSubject.complete();

        // THEN
        expect(besoinPIService.create).toHaveBeenCalledWith(besoinPI);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const besoinPI = { id: 123 };
        spyOn(besoinPIService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ besoinPI });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(besoinPIService.update).toHaveBeenCalledWith(besoinPI);
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
