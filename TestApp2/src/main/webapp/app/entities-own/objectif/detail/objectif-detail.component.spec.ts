import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ObjectifDetailComponent } from './objectif-detail.component';

describe('Component Tests', () => {
  describe('Objectif Management Detail Component', () => {
    let comp: ObjectifDetailComponent;
    let fixture: ComponentFixture<ObjectifDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ObjectifDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ objectif: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ObjectifDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ObjectifDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load objectif on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.objectif).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
