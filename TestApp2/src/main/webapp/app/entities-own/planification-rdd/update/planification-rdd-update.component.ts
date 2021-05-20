import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPlanificationRDD, PlanificationRDD } from '../planification-rdd.model';
import { PlanificationRDDService } from '../service/planification-rdd.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-planification-rdd-update',
  templateUrl: './planification-rdd-update.component.html',
})
export class PlanificationRDDUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nRdd: [],
    date: [],
    realisee: [],
    presentation: [],
    presentationContentType: [],
    standard: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected planificationRDDService: PlanificationRDDService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planificationRDD }) => {
      this.updateForm(planificationRDD);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('newbetterflyApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const planificationRDD = this.createFromForm();
    if (planificationRDD.id !== undefined) {
      this.subscribeToSaveResponse(this.planificationRDDService.update(planificationRDD));
    } else {
      this.subscribeToSaveResponse(this.planificationRDDService.create(planificationRDD));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlanificationRDD>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(planificationRDD: IPlanificationRDD): void {
    this.editForm.patchValue({
      id: planificationRDD.id,
      nRdd: planificationRDD.nRdd,
      date: planificationRDD.date,
      realisee: planificationRDD.realisee,
      presentation: planificationRDD.presentation,
      presentationContentType: planificationRDD.presentationContentType,
      standard: planificationRDD.standard,
    });
  }

  protected createFromForm(): IPlanificationRDD {
    return {
      ...new PlanificationRDD(),
      id: this.editForm.get(['id'])!.value,
      nRdd: this.editForm.get(['nRdd'])!.value,
      date: this.editForm.get(['date'])!.value,
      realisee: this.editForm.get(['realisee'])!.value,
      presentationContentType: this.editForm.get(['presentationContentType'])!.value,
      presentation: this.editForm.get(['presentation'])!.value,
      standard: this.editForm.get(['standard'])!.value,
    };
  }
}
