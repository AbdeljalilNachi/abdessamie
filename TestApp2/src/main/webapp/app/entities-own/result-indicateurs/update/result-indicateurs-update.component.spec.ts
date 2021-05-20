jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ResultIndicateursService } from '../service/result-indicateurs.service';
import { IResultIndicateurs, ResultIndicateurs } from '../result-indicateurs.model';
import { IIndicateurSMI } from 'app/entities-own/indicateur-smi/indicateur-smi.model';
import { IndicateurSMIService } from 'app/entities-own/indicateur-smi/service/indicateur-smi.service';

import { ResultIndicateursUpdateComponent } from './result-indicateurs-update.component';

describe('Component Tests', () => {
  describe('ResultIndicateurs Management Update Component', () => {
    let comp: ResultIndicateursUpdateComponent;
    let fixture: ComponentFixture<ResultIndicateursUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let resultIndicateursService: ResultIndicateursService;
    let indicateurSMIService: IndicateurSMIService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ResultIndicateursUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ResultIndicateursUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ResultIndicateursUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      resultIndicateursService = TestBed.inject(ResultIndicateursService);
      indicateurSMIService = TestBed.inject(IndicateurSMIService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call IndicateurSMI query and add missing value', () => {
        const resultIndicateurs: IResultIndicateurs = { id: 456 };
        const indicateur: IIndicateurSMI = { id: 24202 };
        resultIndicateurs.indicateur = indicateur;

        const indicateurSMICollection: IIndicateurSMI[] = [{ id: 95375 }];
        spyOn(indicateurSMIService, 'query').and.returnValue(of(new HttpResponse({ body: indicateurSMICollection })));
        const additionalIndicateurSMIS = [indicateur];
        const expectedCollection: IIndicateurSMI[] = [...additionalIndicateurSMIS, ...indicateurSMICollection];
        spyOn(indicateurSMIService, 'addIndicateurSMIToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ resultIndicateurs });
        comp.ngOnInit();

        expect(indicateurSMIService.query).toHaveBeenCalled();
        expect(indicateurSMIService.addIndicateurSMIToCollectionIfMissing).toHaveBeenCalledWith(
          indicateurSMICollection,
          ...additionalIndicateurSMIS
        );
        expect(comp.indicateurSMISSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const resultIndicateurs: IResultIndicateurs = { id: 456 };
        const indicateur: IIndicateurSMI = { id: 3988 };
        resultIndicateurs.indicateur = indicateur;

        activatedRoute.data = of({ resultIndicateurs });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(resultIndicateurs));
        expect(comp.indicateurSMISSharedCollection).toContain(indicateur);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const resultIndicateurs = { id: 123 };
        spyOn(resultIndicateursService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ resultIndicateurs });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: resultIndicateurs }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(resultIndicateursService.update).toHaveBeenCalledWith(resultIndicateurs);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const resultIndicateurs = new ResultIndicateurs();
        spyOn(resultIndicateursService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ resultIndicateurs });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: resultIndicateurs }));
        saveSubject.complete();

        // THEN
        expect(resultIndicateursService.create).toHaveBeenCalledWith(resultIndicateurs);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const resultIndicateurs = { id: 123 };
        spyOn(resultIndicateursService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ resultIndicateurs });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(resultIndicateursService.update).toHaveBeenCalledWith(resultIndicateurs);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
