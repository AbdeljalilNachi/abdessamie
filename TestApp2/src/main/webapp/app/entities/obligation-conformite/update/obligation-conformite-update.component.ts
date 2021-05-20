import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IObligationConformite, ObligationConformite } from '../obligation-conformite.model';
import { ObligationConformiteService } from '../service/obligation-conformite.service';
import { IAction } from 'app/entities/action/action.model';
import { ActionService } from 'app/entities/action/service/action.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProcessusSMI } from 'app/entities/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities/processus-smi/service/processus-smi.service';

@Component({
  selector: 'jhi-obligation-conformite-update',
  templateUrl: './obligation-conformite-update.component.html',
})
export class ObligationConformiteUpdateComponent implements OnInit {
  isSaving = false;

  actionsSharedCollection: IAction[] = [];
  usersSharedCollection: IUser[] = [];
  processusSMISSharedCollection: IProcessusSMI[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    rubrique: [],
    reference: [],
    num: [],
    exigence: [],
    applicable: [],
    conforme: [],
    statut: [],
    observation: [],
    origine: [],
    action: [],
    delegue: [],
    processus: [],
  });

  constructor(
    protected obligationConformiteService: ObligationConformiteService,
    protected actionService: ActionService,
    protected userService: UserService,
    protected processusSMIService: ProcessusSMIService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ obligationConformite }) => {
      this.updateForm(obligationConformite);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const obligationConformite = this.createFromForm();
    if (obligationConformite.id !== undefined) {
      this.subscribeToSaveResponse(this.obligationConformiteService.update(obligationConformite));
    } else {
      this.subscribeToSaveResponse(this.obligationConformiteService.create(obligationConformite));
    }
  }

  trackActionById(index: number, item: IAction): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackProcessusSMIById(index: number, item: IProcessusSMI): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IObligationConformite>>): void {
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

  protected updateForm(obligationConformite: IObligationConformite): void {
    this.editForm.patchValue({
      id: obligationConformite.id,
      date: obligationConformite.date,
      rubrique: obligationConformite.rubrique,
      reference: obligationConformite.reference,
      num: obligationConformite.num,
      exigence: obligationConformite.exigence,
      applicable: obligationConformite.applicable,
      conforme: obligationConformite.conforme,
      statut: obligationConformite.statut,
      observation: obligationConformite.observation,
      origine: obligationConformite.origine,
      action: obligationConformite.action,
      delegue: obligationConformite.delegue,
      processus: obligationConformite.processus,
    });

    this.actionsSharedCollection = this.actionService.addActionToCollectionIfMissing(
      this.actionsSharedCollection,
      obligationConformite.action
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, obligationConformite.delegue);
    this.processusSMISSharedCollection = this.processusSMIService.addProcessusSMIToCollectionIfMissing(
      this.processusSMISSharedCollection,
      obligationConformite.processus
    );
  }

  protected loadRelationshipsOptions(): void {
    this.actionService
      .query()
      .pipe(map((res: HttpResponse<IAction[]>) => res.body ?? []))
      .pipe(map((actions: IAction[]) => this.actionService.addActionToCollectionIfMissing(actions, this.editForm.get('action')!.value)))
      .subscribe((actions: IAction[]) => (this.actionsSharedCollection = actions));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('delegue')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

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

  protected createFromForm(): IObligationConformite {
    return {
      ...new ObligationConformite(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      rubrique: this.editForm.get(['rubrique'])!.value,
      reference: this.editForm.get(['reference'])!.value,
      num: this.editForm.get(['num'])!.value,
      exigence: this.editForm.get(['exigence'])!.value,
      applicable: this.editForm.get(['applicable'])!.value,
      conforme: this.editForm.get(['conforme'])!.value,
      statut: this.editForm.get(['statut'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      origine: this.editForm.get(['origine'])!.value,
      action: this.editForm.get(['action'])!.value,
      delegue: this.editForm.get(['delegue'])!.value,
      processus: this.editForm.get(['processus'])!.value,
    };
  }
}
