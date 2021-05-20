import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AnalyseSWOTDetailComponent } from './analyse-swot-detail.component';

describe('Component Tests', () => {
  describe('AnalyseSWOT Management Detail Component', () => {
    let comp: AnalyseSWOTDetailComponent;
    let fixture: ComponentFixture<AnalyseSWOTDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AnalyseSWOTDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ analyseSWOT: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AnalyseSWOTDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AnalyseSWOTDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load analyseSWOT on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.analyseSWOT).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
