import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PolitiqueQSEDetailComponent } from './politique-qse-detail.component';

describe('Component Tests', () => {
  describe('PolitiqueQSE Management Detail Component', () => {
    let comp: PolitiqueQSEDetailComponent;
    let fixture: ComponentFixture<PolitiqueQSEDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PolitiqueQSEDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ politiqueQSE: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PolitiqueQSEDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PolitiqueQSEDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load politiqueQSE on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.politiqueQSE).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
