import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NonConformiteDetailComponent } from './non-conformite-detail.component';

describe('Component Tests', () => {
  describe('NonConformite Management Detail Component', () => {
    let comp: NonConformiteDetailComponent;
    let fixture: ComponentFixture<NonConformiteDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [NonConformiteDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ nonConformite: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(NonConformiteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NonConformiteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load nonConformite on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.nonConformite).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
