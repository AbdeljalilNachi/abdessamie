import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ResultatIndicateurDetailComponent } from './resultat-indicateur-detail.component';

describe('Component Tests', () => {
  describe('ResultatIndicateur Management Detail Component', () => {
    let comp: ResultatIndicateurDetailComponent;
    let fixture: ComponentFixture<ResultatIndicateurDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ResultatIndicateurDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ resultatIndicateur: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ResultatIndicateurDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ResultatIndicateurDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load resultatIndicateur on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.resultatIndicateur).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
