import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPolitiqueQSE, PolitiqueQSE } from '../politique-qse.model';
import { PolitiqueQSEService } from '../service/politique-qse.service';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities-own/processus-smi/service/processus-smi.service';
import { IIndicateurSMI } from 'app/entities-own/indicateur-smi/indicateur-smi.model';
import { IndicateurSMIService } from 'app/entities-own/indicateur-smi/service/indicateur-smi.service';

@Component({
  selector: 'jhi-politique-qse-update',
  templateUrl: './politique-qse-update.component.html',
})
export class PolitiqueQSEUpdateComponent implements OnInit {
  isSaving = false;

  processusSMISSharedCollection: IProcessusSMI[] = [];
  indicateurSMISSharedCollection: IIndicateurSMI[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    axePolitiqueQSE: [],
    objectifQSE: [],
    vigueur: [],
    processus: [],
    indicateur: [],
  });

  constructor(
    protected politiqueQSEService: PolitiqueQSEService,
    protected processusSMIService: ProcessusSMIService,
    protected indicateurSMIService: IndicateurSMIService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ politiqueQSE }) => {
      this.updateForm(politiqueQSE);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const politiqueQSE = this.createFromForm();
    if (politiqueQSE.id !== undefined) {
      this.subscribeToSaveResponse(this.politiqueQSEService.update(politiqueQSE));
    } else {
      this.subscribeToSaveResponse(this.politiqueQSEService.create(politiqueQSE));
    }
  }

  trackProcessusSMIById(index: number, item: IProcessusSMI): number {
    return item.id!;
  }

  trackIndicateurSMIById(index: number, item: IIndicateurSMI): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPolitiqueQSE>>): void {
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

  protected updateForm(politiqueQSE: IPolitiqueQSE): void {
    this.editForm.patchValue({
      id: politiqueQSE.id,
      date: politiqueQSE.date,
      axePolitiqueQSE: politiqueQSE.axePolitiqueQSE,
      objectifQSE: politiqueQSE.objectifQSE,
      vigueur: politiqueQSE.vigueur,
      processus: politiqueQSE.processus,
      indicateur: politiqueQSE.indicateur,
    });

    this.processusSMISSharedCollection = this.processusSMIService.addProcessusSMIToCollectionIfMissing(
      this.processusSMISSharedCollection,
      politiqueQSE.processus
    );
    this.indicateurSMISSharedCollection = this.indicateurSMIService.addIndicateurSMIToCollectionIfMissing(
      this.indicateurSMISSharedCollection,
      politiqueQSE.indicateur
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

  protected createFromForm(): IPolitiqueQSE {
    return {
      ...new PolitiqueQSE(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      axePolitiqueQSE: this.editForm.get(['axePolitiqueQSE'])!.value,
      objectifQSE: this.editForm.get(['objectifQSE'])!.value,
      vigueur: this.editForm.get(['vigueur'])!.value,
      processus: this.editForm.get(['processus'])!.value,
      indicateur: this.editForm.get(['indicateur'])!.value,
    };
  }
}
