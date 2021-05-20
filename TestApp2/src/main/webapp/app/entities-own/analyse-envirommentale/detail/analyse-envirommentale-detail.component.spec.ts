import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AnalyseEnvirommentaleDetailComponent } from './analyse-envirommentale-detail.component';

describe('Component Tests', () => {
  describe('AnalyseEnvirommentale Management Detail Component', () => {
    let comp: AnalyseEnvirommentaleDetailComponent;
    let fixture: ComponentFixture<AnalyseEnvirommentaleDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AnalyseEnvirommentaleDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ analyseEnvirommentale: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AnalyseEnvirommentaleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AnalyseEnvirommentaleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load analyseEnvirommentale on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.analyseEnvirommentale).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
