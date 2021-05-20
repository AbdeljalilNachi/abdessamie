import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBesoinPI, BesoinPI } from '../besoin-pi.model';
import { BesoinPIService } from '../service/besoin-pi.service';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities-own/processus-smi/service/processus-smi.service';

@Component({
  selector: 'jhi-besoin-pi-update',
  templateUrl: './besoin-pi-update.component.html',
})
export class BesoinPIUpdateComponent implements OnInit {
  isSaving = false;

  processusSMISSharedCollection: IProcessusSMI[] = [];

  editForm = this.fb.group({
    id: [],
    dateIdentification: [],
    piPertinentes: [],
    pertinente: [],
    priseEnCharge: [],
    afficher: [],
    processus: [],
  });

  constructor(
    protected besoinPIService: BesoinPIService,
    protected processusSMIService: ProcessusSMIService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ besoinPI }) => {
      this.updateForm(besoinPI);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const besoinPI = this.createFromForm();
    if (besoinPI.id !== undefined) {
      this.subscribeToSaveResponse(this.besoinPIService.update(besoinPI));
    } else {
      this.subscribeToSaveResponse(this.besoinPIService.create(besoinPI));
    }
  }

  trackProcessusSMIById(index: number, item: IProcessusSMI): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBesoinPI>>): void {
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

  protected updateForm(besoinPI: IBesoinPI): void {
    this.editForm.patchValue({
      id: besoinPI.id,
      dateIdentification: besoinPI.dateIdentification,
      piPertinentes: besoinPI.piPertinentes,
      pertinente: besoinPI.pertinente,
      priseEnCharge: besoinPI.priseEnCharge,
      afficher: besoinPI.afficher,
      processus: besoinPI.processus,
    });

    this.processusSMISSharedCollection = this.processusSMIService.addProcessusSMIToCollectionIfMissing(
      this.processusSMISSharedCollection,
      besoinPI.processus
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

  protected createFromForm(): IBesoinPI {
    return {
      ...new BesoinPI(),
      id: this.editForm.get(['id'])!.value,
      dateIdentification: this.editForm.get(['dateIdentification'])!.value,
      piPertinentes: this.editForm.get(['piPertinentes'])!.value,
      pertinente: this.editForm.get(['pertinente'])!.value,
      priseEnCharge: this.editForm.get(['priseEnCharge'])!.value,
      afficher: this.editForm.get(['afficher'])!.value,
      processus: this.editForm.get(['processus'])!.value,
    };
  }
}
