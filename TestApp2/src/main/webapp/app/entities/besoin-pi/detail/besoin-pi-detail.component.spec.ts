import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BesoinPIDetailComponent } from './besoin-pi-detail.component';

describe('Component Tests', () => {
  describe('BesoinPI Management Detail Component', () => {
    let comp: BesoinPIDetailComponent;
    let fixture: ComponentFixture<BesoinPIDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BesoinPIDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ besoinPI: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BesoinPIDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BesoinPIDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load besoinPI on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.besoinPI).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
