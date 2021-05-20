import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ObligationConformiteDetailComponent } from './obligation-conformite-detail.component';

describe('Component Tests', () => {
  describe('ObligationConformite Management Detail Component', () => {
    let comp: ObligationConformiteDetailComponent;
    let fixture: ComponentFixture<ObligationConformiteDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ObligationConformiteDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ obligationConformite: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ObligationConformiteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ObligationConformiteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load obligationConformite on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.obligationConformite).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
