import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IObjectif, Objectif } from '../objectif.model';
import { ObjectifService } from '../service/objectif.service';
import { IAction } from 'app/entities/action/action.model';
import { ActionService } from 'app/entities/action/service/action.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProcessusSMI } from 'app/entities/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities/processus-smi/service/processus-smi.service';
import { IIndicateurSMI } from 'app/entities/indicateur-smi/indicateur-smi.model';
import { IndicateurSMIService } from 'app/entities/indicateur-smi/service/indicateur-smi.service';

@Component({
  selector: 'jhi-objectif-update',
  templateUrl: './objectif-update.component.html',
})
export class ObjectifUpdateComponent implements OnInit {
  isSaving = false;

  actionsSharedCollection: IAction[] = [];
  usersSharedCollection: IUser[] = [];
  processusSMISSharedCollection: IProcessusSMI[] = [];
  indicateurSMISSharedCollection: IIndicateurSMI[] = [];

  editForm = this.fb.group({
    id: [],
    axedelapolitiqueqse: [],
    objectifqse: [],
    origine: [],
    action: [],
    delegue: [],
    processus: [],
    indicateur: [],
  });

  constructor(
    protected objectifService: ObjectifService,
    protected actionService: ActionService,
    protected userService: UserService,
    protected processusSMIService: ProcessusSMIService,
    protected indicateurSMIService: IndicateurSMIService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ objectif }) => {
      this.updateForm(objectif);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const objectif = this.createFromForm();
    if (objectif.id !== undefined) {
      this.subscribeToSaveResponse(this.objectifService.update(objectif));
    } else {
      this.subscribeToSaveResponse(this.objectifService.create(objectif));
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

  trackIndicateurSMIById(index: number, item: IIndicateurSMI): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IObjectif>>): void {
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

  protected updateForm(objectif: IObjectif): void {
    this.editForm.patchValue({
      id: objectif.id,
      axedelapolitiqueqse: objectif.axedelapolitiqueqse,
      objectifqse: objectif.objectifqse,
      origine: objectif.origine,
      action: objectif.action,
      delegue: objectif.delegue,
      processus: objectif.processus,
      indicateur: objectif.indicateur,
    });

    this.actionsSharedCollection = this.actionService.addActionToCollectionIfMissing(this.actionsSharedCollection, objectif.action);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, objectif.delegue);
    this.processusSMISSharedCollection = this.processusSMIService.addProcessusSMIToCollectionIfMissing(
      this.processusSMISSharedCollection,
      objectif.processus
    );
    this.indicateurSMISSharedCollection = this.indicateurSMIService.addIndicateurSMIToCollectionIfMissing(
      this.indicateurSMISSharedCollection,
      objectif.indicateur
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

  protected createFromForm(): IObjectif {
    return {
      ...new Objectif(),
      id: this.editForm.get(['id'])!.value,
      axedelapolitiqueqse: this.editForm.get(['axedelapolitiqueqse'])!.value,
      objectifqse: this.editForm.get(['objectifqse'])!.value,
      origine: this.editForm.get(['origine'])!.value,
      action: this.editForm.get(['action'])!.value,
      delegue: this.editForm.get(['delegue'])!.value,
      processus: this.editForm.get(['processus'])!.value,
      indicateur: this.editForm.get(['indicateur'])!.value,
    };
  }
}
