jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AuditService } from '../service/audit.service';

import { AuditDeleteDialogComponent } from './audit-delete-dialog.component';

describe('Component Tests', () => {
  describe('Audit Management Delete Component', () => {
    let comp: AuditDeleteDialogComponent;
    let fixture: ComponentFixture<AuditDeleteDialogComponent>;
    let service: AuditService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AuditDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(AuditDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AuditDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AuditService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
