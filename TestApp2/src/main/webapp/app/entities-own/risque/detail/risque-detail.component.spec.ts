import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RisqueDetailComponent } from './risque-detail.component';

describe('Component Tests', () => {
  describe('Risque Management Detail Component', () => {
    let comp: RisqueDetailComponent;
    let fixture: ComponentFixture<RisqueDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RisqueDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ risque: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RisqueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RisqueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load risque on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.risque).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
