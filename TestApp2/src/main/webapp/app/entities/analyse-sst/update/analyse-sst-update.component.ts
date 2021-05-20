import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAnalyseSST, AnalyseSST } from '../analyse-sst.model';
import { AnalyseSSTService } from '../service/analyse-sst.service';
import { IAction } from 'app/entities/action/action.model';
import { ActionService } from 'app/entities/action/service/action.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProcessusSMI } from 'app/entities/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities/processus-smi/service/processus-smi.service';

@Component({
  selector: 'jhi-analyse-sst-update',
  templateUrl: './analyse-sst-update.component.html',
})
export class AnalyseSSTUpdateComponent implements OnInit {
  isSaving = false;

  actionsSharedCollection: IAction[] = [];
  usersSharedCollection: IUser[] = [];
  processusSMISSharedCollection: IProcessusSMI[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    buisnessUnit: [],
    uniteTravail: [],
    danger: [],
    risque: [],
    competence: [],
    situation: [],
    frequence: [],
    dureeExposition: [],
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
    protected analyseSSTService: AnalyseSSTService,
    protected actionService: ActionService,
    protected userService: UserService,
    protected processusSMIService: ProcessusSMIService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ analyseSST }) => {
      this.updateForm(analyseSST);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const analyseSST = this.createFromForm();
    if (analyseSST.id !== undefined) {
      this.subscribeToSaveResponse(this.analyseSSTService.update(analyseSST));
    } else {
      this.subscribeToSaveResponse(this.analyseSSTService.create(analyseSST));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnalyseSST>>): void {
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

  protected updateForm(analyseSST: IAnalyseSST): void {
    this.editForm.patchValue({
      id: analyseSST.id,
      date: analyseSST.date,
      buisnessUnit: analyseSST.buisnessUnit,
      uniteTravail: analyseSST.uniteTravail,
      danger: analyseSST.danger,
      risque: analyseSST.risque,
      competence: analyseSST.competence,
      situation: analyseSST.situation,
      frequence: analyseSST.frequence,
      dureeExposition: analyseSST.dureeExposition,
      coefficientMaitrise: analyseSST.coefficientMaitrise,
      gravite: analyseSST.gravite,
      criticite: analyseSST.criticite,
      maitriseExistante: analyseSST.maitriseExistante,
      origine: analyseSST.origine,
      action: analyseSST.action,
      delegue: analyseSST.delegue,
      processus: analyseSST.processus,
    });

    this.actionsSharedCollection = this.actionService.addActionToCollectionIfMissing(this.actionsSharedCollection, analyseSST.action);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, analyseSST.delegue);
    this.processusSMISSharedCollection = this.processusSMIService.addProcessusSMIToCollectionIfMissing(
      this.processusSMISSharedCollection,
      analyseSST.processus
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

  protected createFromForm(): IAnalyseSST {
    return {
      ...new AnalyseSST(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      buisnessUnit: this.editForm.get(['buisnessUnit'])!.value,
      uniteTravail: this.editForm.get(['uniteTravail'])!.value,
      danger: this.editForm.get(['danger'])!.value,
      risque: this.editForm.get(['risque'])!.value,
      competence: this.editForm.get(['competence'])!.value,
      situation: this.editForm.get(['situation'])!.value,
      frequence: this.editForm.get(['frequence'])!.value,
      dureeExposition: this.editForm.get(['dureeExposition'])!.value,
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
