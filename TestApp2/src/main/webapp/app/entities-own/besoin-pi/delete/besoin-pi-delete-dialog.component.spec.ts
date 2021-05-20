jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { BesoinPIService } from '../service/besoin-pi.service';

import { BesoinPIDeleteDialogComponent } from './besoin-pi-delete-dialog.component';

describe('Component Tests', () => {
  describe('BesoinPI Management Delete Component', () => {
    let comp: BesoinPIDeleteDialogComponent;
    let fixture: ComponentFixture<BesoinPIDeleteDialogComponent>;
    let service: BesoinPIService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BesoinPIDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(BesoinPIDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BesoinPIDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(BesoinPIService);
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
