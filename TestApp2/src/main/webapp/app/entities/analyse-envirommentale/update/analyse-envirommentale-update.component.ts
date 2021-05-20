import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAnalyseEnvirommentale, AnalyseEnvirommentale } from '../analyse-envirommentale.model';
import { AnalyseEnvirommentaleService } from '../service/analyse-envirommentale.service';
import { IAction } from 'app/entities/action/action.model';
import { ActionService } from 'app/entities/action/service/action.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProcessusSMI } from 'app/entities/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities/processus-smi/service/processus-smi.service';

@Component({
  selector: 'jhi-analyse-envirommentale-update',
  templateUrl: './analyse-envirommentale-update.component.html',
})
export class AnalyseEnvirommentaleUpdateComponent implements OnInit {
  isSaving = false;

  actionsSharedCollection: IAction[] = [];
  usersSharedCollection: IUser[] = [];
  processusSMISSharedCollection: IProcessusSMI[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    businessUnit: [],
    activite: [],
    aspectEnvironnemental: [],
    impactEnvironnemental: [],
    competencesRequises: [],
    situation: [],
    frequence: [],
    sensibiliteMilieu: [],
    coefficientMaitrise: [],
    gravite: [],
    criticite: [],
    maitriseExistante: [],
    origine: [],
    action: [],
    delegue: [],
    processus: [],
  });

  constructor(
    protected analyseEnvirommentaleService: AnalyseEnvirommentaleService,
    protected actionService: ActionService,
    protected userService: UserService,
    protected processusSMIService: ProcessusSMIService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ analyseEnvirommentale }) => {
      this.updateForm(analyseEnvirommentale);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const analyseEnvirommentale = this.createFromForm();
    if (analyseEnvirommentale.id !== undefined) {
      this.subscribeToSaveResponse(this.analyseEnvirommentaleService.update(analyseEnvirommentale));
    } else {
      this.subscribeToSaveResponse(this.analyseEnvirommentaleService.create(analyseEnvirommentale));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnalyseEnvirommentale>>): void {
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

  protected updateForm(analyseEnvirommentale: IAnalyseEnvirommentale): void {
    this.editForm.patchValue({
      id: analyseEnvirommentale.id,
      date: analyseEnvirommentale.date,
      businessUnit: analyseEnvirommentale.businessUnit,
      activite: analyseEnvirommentale.activite,
      aspectEnvironnemental: analyseEnvirommentale.aspectEnvironnemental,
      impactEnvironnemental: analyseEnvirommentale.impactEnvironnemental,
      competencesRequises: analyseEnvirommentale.competencesRequises,
      situation: analyseEnvirommentale.situation,
      frequence: analyseEnvirommentale.frequence,
      sensibiliteMilieu: analyseEnvirommentale.sensibiliteMilieu,
      coefficientMaitrise: analyseEnvirommentale.coefficientMaitrise,
      gravite: analyseEnvirommentale.gravite,
      criticite: analyseEnvirommentale.criticite,
      maitriseExistante: analyseEnvirommentale.maitriseExistante,
      origine: analyseEnvirommentale.origine,
      action: analyseEnvirommentale.action,
      delegue: analyseEnvirommentale.delegue,
      processus: analyseEnvirommentale.processus,
    });

    this.actionsSharedCollection = this.actionService.addActionToCollectionIfMissing(
      this.actionsSharedCollection,
      analyseEnvirommentale.action
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, analyseEnvirommentale.delegue);
    this.processusSMISSharedCollection = this.processusSMIService.addProcessusSMIToCollectionIfMissing(
      this.processusSMISSharedCollection,
      analyseEnvirommentale.processus
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

  protected createFromForm(): IAnalyseEnvirommentale {
    return {
      ...new AnalyseEnvirommentale(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      businessUnit: this.editForm.get(['businessUnit'])!.value,
      activite: this.editForm.get(['activite'])!.value,
      aspectEnvironnemental: this.editForm.get(['aspectEnvironnemental'])!.value,
      impactEnvironnemental: this.editForm.get(['impactEnvironnemental'])!.value,
      competencesRequises: this.editForm.get(['competencesRequises'])!.value,
      situation: this.editForm.get(['situation'])!.value,
      frequence: this.editForm.get(['frequence'])!.value,
      sensibiliteMilieu: this.editForm.get(['sensibiliteMilieu'])!.value,
      coefficientMaitrise: this.editForm.get(['coefficientMaitrise'])!.value,
      gravite: this.editForm.get(['gravite'])!.value,
      criticite: this.editForm.get(['criticite'])!.value,
      maitriseExistante: this.editForm.get(['maitriseExistante'])!.value,
      origine: this.editForm.get(['origine'])!.value,
      action: this.editForm.get(['action'])!.value,
      delegue: this.editForm.get(['delegue'])!.value,
      processus: this.editForm.get(['processus'])!.value,
    };
  }
}
