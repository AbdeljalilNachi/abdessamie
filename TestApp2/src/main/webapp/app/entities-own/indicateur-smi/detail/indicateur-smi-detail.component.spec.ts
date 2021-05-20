import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IndicateurSMIDetailComponent } from './indicateur-smi-detail.component';

describe('Component Tests', () => {
  describe('IndicateurSMI Management Detail Component', () => {
    let comp: IndicateurSMIDetailComponent;
    let fixture: ComponentFixture<IndicateurSMIDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [IndicateurSMIDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ indicateurSMI: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(IndicateurSMIDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IndicateurSMIDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load indicateurSMI on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.indicateurSMI).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
