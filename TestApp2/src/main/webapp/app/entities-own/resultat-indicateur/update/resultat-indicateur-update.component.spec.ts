jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ResultatIndicateurService } from '../service/resultat-indicateur.service';
import { IResultatIndicateur, ResultatIndicateur } from '../resultat-indicateur.model';
import { IResultIndicateurs } from 'app/entities-own/result-indicateurs/result-indicateurs.model';
import { ResultIndicateursService } from 'app/entities-own/result-indicateurs/service/result-indicateurs.service';

import { ResultatIndicateurUpdateComponent } from './resultat-indicateur-update.component';

describe('Component Tests', () => {
  describe('ResultatIndicateur Management Update Component', () => {
    let comp: ResultatIndicateurUpdateComponent;
    let fixture: ComponentFixture<ResultatIndicateurUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let resultatIndicateurService: ResultatIndicateurService;
    let resultIndicateursService: ResultIndicateursService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ResultatIndicateurUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ResultatIndicateurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ResultatIndicateurUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      resultatIndicateurService = TestBed.inject(ResultatIndicateurService);
      resultIndicateursService = TestBed.inject(ResultIndicateursService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ResultIndicateurs query and add missing value', () => {
        const resultatIndicateur: IResultatIndicateur = { id: 456 };
        const resultIndicateurs: IResultIndicateurs = { id: 38171 };
        resultatIndicateur.resultIndicateurs = resultIndicateurs;

        const resultIndicateursCollection: IResultIndicateurs[] = [{ id: 81081 }];
        spyOn(resultIndicateursService, 'query').and.returnValue(of(new HttpResponse({ body: resultIndicateursCollection })));
        const additionalResultIndicateurs = [resultIndicateurs];
        const expectedCollection: IResultIndicateurs[] = [...additionalResultIndicateurs, ...resultIndicateursCollection];
        spyOn(resultIndicateursService, 'addResultIndicateursToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ resultatIndicateur });
        comp.ngOnInit();

        expect(resultIndicateursService.query).toHaveBeenCalled();
        expect(resultIndicateursService.addResultIndicateursToCollectionIfMissing).toHaveBeenCalledWith(
          resultIndicateursCollection,
          ...additionalResultIndicateurs
        );
        expect(comp.resultIndicateursSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const resultatIndicateur: IResultatIndicateur = { id: 456 };
        const resultIndicateurs: IResultIndicateurs = { id: 37136 };
        resultatIndicateur.resultIndicateurs = resultIndicateurs;

        activatedRoute.data = of({ resultatIndicateur });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(resultatIndicateur));
        expect(comp.resultIndicateursSharedCollection).toContain(resultIndicateurs);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const resultatIndicateur = { id: 123 };
        spyOn(resultatIndicateurService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ resultatIndicateur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: resultatIndicateur }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(resultatIndicateurService.update).toHaveBeenCalledWith(resultatIndicateur);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const resultatIndicateur = new ResultatIndicateur();
        spyOn(resultatIndicateurService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ resultatIndicateur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: resultatIndicateur }));
        saveSubject.complete();

        // THEN
        expect(resultatIndicateurService.create).toHaveBeenCalledWith(resultatIndicateur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const resultatIndicateur = { id: 123 };
        spyOn(resultatIndicateurService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ resultatIndicateur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(resultatIndicateurService.update).toHaveBeenCalledWith(resultatIndicateur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackResultIndicateursById', () => {
        it('Should return tracked ResultIndicateurs primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackResultIndicateursById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
