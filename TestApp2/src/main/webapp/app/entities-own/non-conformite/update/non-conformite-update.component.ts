import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INonConformite, NonConformite } from '../non-conformite.model';
import { NonConformiteService } from '../service/non-conformite.service';
import { IAction } from 'app/entities-own/action/action.model';
import { ActionService } from 'app/entities-own/action/service/action.service';
import { IUser } from 'app/entities-own/user/user.model';
import { UserService } from 'app/entities-own/user/user.service';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities-own/processus-smi/service/processus-smi.service';

@Component({
  selector: 'jhi-non-conformite-update',
  templateUrl: './non-conformite-update.component.html',
})
export class NonConformiteUpdateComponent implements OnInit {
  isSaving = false;

  actionsSharedCollection: IAction[] = [];
  usersSharedCollection: IUser[] = [];
  processusSMISSharedCollection: IProcessusSMI[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    description: [],
    causesPotentielles: [],
    origine: [],
    action: [],
    delegue: [],
    processus: [],
  });

  constructor(
    protected nonConformiteService: NonConformiteService,
    protected actionService: ActionService,
    protected userService: UserService,
    protected processusSMIService: ProcessusSMIService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nonConformite }) => {
      this.updateForm(nonConformite);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nonConformite = this.createFromForm();
    if (nonConformite.id !== undefined) {
      this.subscribeToSaveResponse(this.nonConformiteService.update(nonConformite));
    } else {
      this.subscribeToSaveResponse(this.nonConformiteService.create(nonConformite));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INonConformite>>): void {
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

  protected updateForm(nonConformite: INonConformite): void {
    this.editForm.patchValue({
      id: nonConformite.id,
      date: nonConformite.date,
      description: nonConformite.description,
      causesPotentielles: nonConformite.causesPotentielles,
      origine: nonConformite.origine,
      action: nonConformite.action,
      delegue: nonConformite.delegue,
      processus: nonConformite.processus,
    });

    this.actionsSharedCollection = this.actionService.addActionToCollectionIfMissing(this.actionsSharedCollection, nonConformite.action);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, nonConformite.delegue);
    this.processusSMISSharedCollection = this.processusSMIService.addProcessusSMIToCollectionIfMissing(
      this.processusSMISSharedCollection,
      nonConformite.processus
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

  protected createFromForm(): INonConformite {
    return {
      ...new NonConformite(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      description: this.editForm.get(['description'])!.value,
      causesPotentielles: this.editForm.get(['causesPotentielles'])!.value,
      origine: this.editForm.get(['origine'])!.value,
      action: this.editForm.get(['action'])!.value,
      delegue: this.editForm.get(['delegue'])!.value,
      processus: this.editForm.get(['processus'])!.value,
    };
  }
}
