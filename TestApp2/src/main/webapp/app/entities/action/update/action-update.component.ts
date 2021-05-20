import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAction, Action } from '../action.model';
import { ActionService } from '../service/action.service';

@Component({
  selector: 'jhi-action-update',
  templateUrl: './action-update.component.html',
})
export class ActionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    datePlanification: [],
    action: [],
    type: [],
    delai: [],
    avancement: [],
    realisee: [],
    critereResultat: [],
    ressourcesNecessaires: [],
    statut: [],
    efficace: [],
  });

  constructor(protected actionService: ActionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ action }) => {
      this.updateForm(action);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const action = this.createFromForm();
    if (action.id !== undefined) {
      this.subscribeToSaveResponse(this.actionService.update(action));
    } else {
      this.subscribeToSaveResponse(this.actionService.create(action));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAction>>): void {
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

  protected updateForm(action: IAction): void {
    this.editForm.patchValue({
      id: action.id,
      datePlanification: action.datePlanification,
      action: action.action,
      type: action.type,
      delai: action.delai,
      avancement: action.avancement,
      realisee: action.realisee,
      critereResultat: action.critereResultat,
      ressourcesNecessaires: action.ressourcesNecessaires,
      statut: action.statut,
      efficace: action.efficace,
    });
  }

  protected createFromForm(): IAction {
    return {
      ...new Action(),
      id: this.editForm.get(['id'])!.value,
      datePlanification: this.editForm.get(['datePlanification'])!.value,
      action: this.editForm.get(['action'])!.value,
      type: this.editForm.get(['type'])!.value,
      delai: this.editForm.get(['delai'])!.value,
      avancement: this.editForm.get(['avancement'])!.value,
      realisee: this.editForm.get(['realisee'])!.value,
      critereResultat: this.editForm.get(['critereResultat'])!.value,
      ressourcesNecessaires: this.editForm.get(['ressourcesNecessaires'])!.value,
      statut: this.editForm.get(['statut'])!.value,
      efficace: this.editForm.get(['efficace'])!.value,
    };
  }
}
