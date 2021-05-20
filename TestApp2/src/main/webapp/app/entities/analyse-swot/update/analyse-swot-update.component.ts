import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAnalyseSWOT, AnalyseSWOT } from '../analyse-swot.model';
import { AnalyseSWOTService } from '../service/analyse-swot.service';
import { IProcessusSMI } from 'app/entities/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities/processus-smi/service/processus-smi.service';

@Component({
  selector: 'jhi-analyse-swot-update',
  templateUrl: './analyse-swot-update.component.html',
})
export class AnalyseSWOTUpdateComponent implements OnInit {
  isSaving = false;

  processusSMISSharedCollection: IProcessusSMI[] = [];

  editForm = this.fb.group({
    id: [],
    dateIdentification: [],
    description: [],
    pilote: [],
    type: [],
    bu: [],
    commentaire: [],
    afficher: [],
    processus: [],
  });

  constructor(
    protected analyseSWOTService: AnalyseSWOTService,
    protected processusSMIService: ProcessusSMIService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ analyseSWOT }) => {
      this.updateForm(analyseSWOT);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const analyseSWOT = this.createFromForm();
    if (analyseSWOT.id !== undefined) {
      this.subscribeToSaveResponse(this.analyseSWOTService.update(analyseSWOT));
    } else {
      this.subscribeToSaveResponse(this.analyseSWOTService.create(analyseSWOT));
    }
  }

  trackProcessusSMIById(index: number, item: IProcessusSMI): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnalyseSWOT>>): void {
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

  protected updateForm(analyseSWOT: IAnalyseSWOT): void {
    this.editForm.patchValue({
      id: analyseSWOT.id,
      dateIdentification: analyseSWOT.dateIdentification,
      description: analyseSWOT.description,
      pilote: analyseSWOT.pilote,
      type: analyseSWOT.type,
      bu: analyseSWOT.bu,
      commentaire: analyseSWOT.commentaire,
      afficher: analyseSWOT.afficher,
      processus: analyseSWOT.processus,
    });

    this.processusSMISSharedCollection = this.processusSMIService.addProcessusSMIToCollectionIfMissing(
      this.processusSMISSharedCollection,
      analyseSWOT.processus
    );
  }

  protected loadRelationshipsOptions(): void {
    this.processusSMIService
      .query()
      .pipe(map((res: HttpResponse<IProcessusSMI[]>) => res.body ?? []))
      .pipe(
        map((processusSMIS: IProcessusSMI[]) =>
          this.processusSMIService.addProcessusSMIToCollectionIfMissing(processusSMIS, this.editForm.get('processus')!.value)
        )
      )
      .subscribe((processusSMIS: IProcessusSMI[]) => (this.processusSMISSharedCollection = processusSMIS));
  }

  protected createFromForm(): IAnalyseSWOT {
    return {
      ...new AnalyseSWOT(),
      id: this.editForm.get(['id'])!.value,
      dateIdentification: this.editForm.get(['dateIdentification'])!.value,
      description: this.editForm.get(['description'])!.value,
      pilote: this.editForm.get(['pilote'])!.value,
      type: this.editForm.get(['type'])!.value,
      bu: this.editForm.get(['bu'])!.value,
      commentaire: this.editForm.get(['commentaire'])!.value,
      afficher: this.editForm.get(['afficher'])!.value,
      processus: this.editForm.get(['processus'])!.value,
    };
  }
}
