import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ResultIndicateursDetailComponent } from './result-indicateurs-detail.component';

describe('Component Tests', () => {
  describe('ResultIndicateurs Management Detail Component', () => {
    let comp: ResultIndicateursDetailComponent;
    let fixture: ComponentFixture<ResultIndicateursDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ResultIndicateursDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ resultIndicateurs: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ResultIndicateursDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ResultIndicateursDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load resultIndicateurs on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.resultIndicateurs).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
