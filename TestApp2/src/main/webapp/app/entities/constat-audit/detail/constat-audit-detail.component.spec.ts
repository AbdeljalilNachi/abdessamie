import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConstatAuditDetailComponent } from './constat-audit-detail.component';

describe('Component Tests', () => {
  describe('ConstatAudit Management Detail Component', () => {
    let comp: ConstatAuditDetailComponent;
    let fixture: ComponentFixture<ConstatAuditDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ConstatAuditDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ constatAudit: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ConstatAuditDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConstatAuditDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load constatAudit on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.constatAudit).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
