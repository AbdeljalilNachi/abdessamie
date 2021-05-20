import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IResultIndicateurs, ResultIndicateurs } from '../result-indicateurs.model';
import { ResultIndicateursService } from '../service/result-indicateurs.service';
import { IIndicateurSMI } from 'app/entities-own/indicateur-smi/indicateur-smi.model';
import { IndicateurSMIService } from 'app/entities-own/indicateur-smi/service/indicateur-smi.service';

@Component({
  selector: 'jhi-result-indicateurs-update',
  templateUrl: './result-indicateurs-update.component.html',
})
export class ResultIndicateursUpdateComponent implements OnInit {
  isSaving = false;

  indicateurSMISSharedCollection: IIndicateurSMI[] = [];

  editForm = this.fb.group({
    id: [],
    annee: [],
    observation: [],
    indicateur: [],
  });

  constructor(
    protected resultIndicateursService: ResultIndicateursService,
    protected indicateurSMIService: IndicateurSMIService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resultIndicateurs }) => {
      this.updateForm(resultIndicateurs);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resultIndicateurs = this.createFromForm();
    if (resultIndicateurs.id !== undefined) {
      this.subscribeToSaveResponse(this.resultIndicateursService.update(resultIndicateurs));
    } else {
      this.subscribeToSaveResponse(this.resultIndicateursService.create(resultIndicateurs));
    }
  }

  trackIndicateurSMIById(index: number, item: IIndicateurSMI): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResultIndicateurs>>): void {
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

  protected updateForm(resultIndicateurs: IResultIndicateurs): void {
    this.editForm.patchValue({
      id: resultIndicateurs.id,
      annee: resultIndicateurs.annee,
      observation: resultIndicateurs.observation,
      indicateur: resultIndicateurs.indicateur,
    });

    this.indicateurSMISSharedCollection = this.indicateurSMIService.addIndicateurSMIToCollectionIfMissing(
      this.indicateurSMISSharedCollection,
      resultIndicateurs.indicateur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.indicateurSMIService
      .query()
      .pipe(map((res: HttpResponse<IIndicateurSMI[]>) => res.body ?? []))
      .pipe(
        map((indicateurSMIS: IIndicateurSMI[]) =>
          this.indicateurSMIService.addIndicateurSMIToCollectionIfMissing(indicateurSMIS, this.editForm.get('indicateur')!.value)
        )
      )
      .subscribe((indicateurSMIS: IIndicateurSMI[]) => (this.indicateurSMISSharedCollection = indicateurSMIS));
  }

  protected createFromForm(): IResultIndicateurs {
    return {
      ...new ResultIndicateurs(),
      id: this.editForm.get(['id'])!.value,
      annee: this.editForm.get(['annee'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      indicateur: this.editForm.get(['indicateur'])!.value,
    };
  }
}
