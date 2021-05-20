jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PolitiqueQSEService } from '../service/politique-qse.service';
import { IPolitiqueQSE, PolitiqueQSE } from '../politique-qse.model';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities-own/processus-smi/service/processus-smi.service';
import { IIndicateurSMI } from 'app/entities-own/indicateur-smi/indicateur-smi.model';
import { IndicateurSMIService } from 'app/entities-own/indicateur-smi/service/indicateur-smi.service';

import { PolitiqueQSEUpdateComponent } from './politique-qse-update.component';

describe('Component Tests', () => {
  describe('PolitiqueQSE Management Update Component', () => {
    let comp: PolitiqueQSEUpdateComponent;
    let fixture: ComponentFixture<PolitiqueQSEUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let politiqueQSEService: PolitiqueQSEService;
    let processusSMIService: ProcessusSMIService;
    let indicateurSMIService: IndicateurSMIService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PolitiqueQSEUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PolitiqueQSEUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PolitiqueQSEUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      politiqueQSEService = TestBed.inject(PolitiqueQSEService);
      processusSMIService = TestBed.inject(ProcessusSMIService);
      indicateurSMIService = TestBed.inject(IndicateurSMIService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ProcessusSMI query and add missing value', () => {
        const politiqueQSE: IPolitiqueQSE = { id: 456 };
        const processus: IProcessusSMI = { id: 66734 };
        politiqueQSE.processus = processus;

        const processusSMICollection: IProcessusSMI[] = [{ id: 56198 }];
        spyOn(processusSMIService, 'query').and.returnValue(of(new HttpResponse({ body: processusSMICollection })));
        const additionalProcessusSMIS = [processus];
        const expectedCollection: IProcessusSMI[] = [...additionalProcessusSMIS, ...processusSMICollection];
        spyOn(processusSMIService, 'addProcessusSMIToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ politiqueQSE });
        comp.ngOnInit();

        expect(processusSMIService.query).toHaveBeenCalled();
        expect(processusSMIService.addProcessusSMIToCollectionIfMissing).toHaveBeenCalledWith(
          processusSMICollection,
          ...additionalProcessusSMIS
        );
        expect(comp.processusSMISSharedCollection).toEqual(expectedCollection);
      });

      it('Should call IndicateurSMI query and add missing value', () => {
        const politiqueQSE: IPolitiqueQSE = { id: 456 };
        const indicateur: IIndicateurSMI = { id: 2518 };
        politiqueQSE.indicateur = indicateur;

        const indicateurSMICollection: IIndicateurSMI[] = [{ id: 94254 }];
        spyOn(indicateurSMIService, 'query').and.returnValue(of(new HttpResponse({ body: indicateurSMICollection })));
        const additionalIndicateurSMIS = [indicateur];
        const expectedCollection: IIndicateurSMI[] = [...additionalIndicateurSMIS, ...indicateurSMICollection];
        spyOn(indicateurSMIService, 'addIndicateurSMIToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ politiqueQSE });
        comp.ngOnInit();

        expect(indicateurSMIService.query).toHaveBeenCalled();
        expect(indicateurSMIService.addIndicateurSMIToCollectionIfMissing).toHaveBeenCalledWith(
          indicateurSMICollection,
          ...additionalIndicateurSMIS
        );
        expect(comp.indicateurSMISSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const politiqueQSE: IPolitiqueQSE = { id: 456 };
        const processus: IProcessusSMI = { id: 58316 };
        politiqueQSE.processus = processus;
        const indicateur: IIndicateurSMI = { id: 52954 };
        politiqueQSE.indicateur = indicateur;

        activatedRoute.data = of({ politiqueQSE });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(politiqueQSE));
        expect(comp.processusSMISSharedCollection).toContain(processus);
        expect(comp.indicateurSMISSharedCollection).toContain(indicateur);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const politiqueQSE = { id: 123 };
        spyOn(politiqueQSEService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ politiqueQSE });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: politiqueQSE }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(politiqueQSEService.update).toHaveBeenCalledWith(politiqueQSE);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const politiqueQSE = new PolitiqueQSE();
        spyOn(politiqueQSEService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ politiqueQSE });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: politiqueQSE }));
        saveSubject.complete();

        // THEN
        expect(politiqueQSEService.create).toHaveBeenCalledWith(politiqueQSE);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const politiqueQSE = { id: 123 };
        spyOn(politiqueQSEService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ politiqueQSE });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(politiqueQSEService.update).toHaveBeenCalledWith(politiqueQSE);
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
