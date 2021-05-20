import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AnalyseSSTDetailComponent } from './analyse-sst-detail.component';

describe('Component Tests', () => {
  describe('AnalyseSST Management Detail Component', () => {
    let comp: AnalyseSSTDetailComponent;
    let fixture: ComponentFixture<AnalyseSSTDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AnalyseSSTDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ analyseSST: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AnalyseSSTDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AnalyseSSTDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load analyseSST on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.analyseSST).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
