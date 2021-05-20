import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IResultatIndicateur, ResultatIndicateur } from '../resultat-indicateur.model';
import { ResultatIndicateurService } from '../service/resultat-indicateur.service';
import { IResultIndicateurs } from 'app/entities/result-indicateurs/result-indicateurs.model';
import { ResultIndicateursService } from 'app/entities/result-indicateurs/service/result-indicateurs.service';

@Component({
  selector: 'jhi-resultat-indicateur-update',
  templateUrl: './resultat-indicateur-update.component.html',
})
export class ResultatIndicateurUpdateComponent implements OnInit {
  isSaving = false;

  resultIndicateursSharedCollection: IResultIndicateurs[] = [];

  editForm = this.fb.group({
    id: [],
    mois: [],
    cible: [],
    resultat: [],
    resultIndicateurs: [],
  });

  constructor(
    protected resultatIndicateurService: ResultatIndicateurService,
    protected resultIndicateursService: ResultIndicateursService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resultatIndicateur }) => {
      this.updateForm(resultatIndicateur);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resultatIndicateur = this.createFromForm();
    if (resultatIndicateur.id !== undefined) {
      this.subscribeToSaveResponse(this.resultatIndicateurService.update(resultatIndicateur));
    } else {
      this.subscribeToSaveResponse(this.resultatIndicateurService.create(resultatIndicateur));
    }
  }

  trackResultIndicateursById(index: number, item: IResultIndicateurs): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResultatIndicateur>>): void {
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

  protected updateForm(resultatIndicateur: IResultatIndicateur): void {
    this.editForm.patchValue({
      id: resultatIndicateur.id,
      mois: resultatIndicateur.mois,
      cible: resultatIndicateur.cible,
      resultat: resultatIndicateur.resultat,
      resultIndicateurs: resultatIndicateur.resultIndicateurs,
    });

    this.resultIndicateursSharedCollection = this.resultIndicateursService.addResultIndicateursToCollectionIfMissing(
      this.resultIndicateursSharedCollection,
      resultatIndicateur.resultIndicateurs
    );
  }

  protected loadRelationshipsOptions(): void {
    this.resultIndicateursService
      .query()
      .pipe(map((res: HttpResponse<IResultIndicateurs[]>) => res.body ?? []))
      .pipe(
        map((resultIndicateurs: IResultIndicateurs[]) =>
          this.resultIndicateursService.addResultIndicateursToCollectionIfMissing(
            resultIndicateurs,
            this.editForm.get('resultIndicateurs')!.value
          )
        )
      )
      .subscribe((resultIndicateurs: IResultIndicateurs[]) => (this.resultIndicateursSharedCollection = resultIndicateurs));
  }

  protected createFromForm(): IResultatIndicateur {
    return {
      ...new ResultatIndicateur(),
      id: this.editForm.get(['id'])!.value,
      mois: this.editForm.get(['mois'])!.value,
      cible: this.editForm.get(['cible'])!.value,
      resultat: this.editForm.get(['resultat'])!.value,
      resultIndicateurs: this.editForm.get(['resultIndicateurs'])!.value,
    };
  }
}
