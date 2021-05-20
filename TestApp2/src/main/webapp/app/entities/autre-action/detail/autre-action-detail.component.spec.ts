import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AutreActionDetailComponent } from './autre-action-detail.component';

describe('Component Tests', () => {
  describe('AutreAction Management Detail Component', () => {
    let comp: AutreActionDetailComponent;
    let fixture: ComponentFixture<AutreActionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AutreActionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ autreAction: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AutreActionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AutreActionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load autreAction on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.autreAction).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
