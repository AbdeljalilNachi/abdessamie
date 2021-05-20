import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { IRisque, Risque } from '../risque.model';
import { RisqueService } from '../service/risque.service';
import { IAction } from 'app/entities-own/action/action.model';
import { ActionService } from 'app/entities-own/action/service/action.service';
import { IUser } from 'app/entities-own/user/user.model';
import { UserService } from 'app/entities-own/user/user.service';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities-own/processus-smi/service/processus-smi.service';


@Component({
  selector: 'jhi-risque-update',
  templateUrl: './risque-update.component.html',
})
export class RisqueUpdateComponent implements OnInit {
  isSaving = false;

  actionsSharedCollection: IAction[] = [];
  usersSharedCollection: IUser[] = [];
  processusSMISSharedCollection: IProcessusSMI[] = [];

  editForm = this.fb.group({
    id: [],
    dateIdentification: [],
    description: [],
    causePotentielle: [],
    effetPotentiel: [],
    type: [],
    gravite: [],
    probabilite: [],
    criticite: [],
    traitement: [],
    commentaire: [],
    origine: [],
    action: [],
    delegue: [],
    processus: [],
  });


  
  
  constructor(
    protected risqueService: RisqueService,
    protected actionService: ActionService,
    protected userService: UserService,
    protected processusSMIService: ProcessusSMIService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}


  onChange(): void{
    let gra = 1;
    let pro = 1;
    let cri = 1;
    switch(this.editForm.get(['gravite'])!.value) { 
    
      case "ONE": { 
         gra = 1;
         break; 
      } 
      case "TWO": { 
        gra = 2;
         break; 
      } 
      case "THREE": { 
        gra = 3;
        break; 
     } 
  
   } 
   switch(this.editForm.get(['probabilite'])!.value) { 
    case "ONE": { 
         pro = 1;      
       break; 
    } 
    case "TWO": { 
        pro = 2;
       break; 
    } 
    case "THREE": { 
        pro = 3;
      break; 
   } 

 } 
 cri = (gra * pro);

 (<HTMLInputElement>document.getElementById("field_criticite")).value = cri as unknown as string ;


    }



  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ risque }) => {
      this.updateForm(risque);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const risque = this.createFromForm();
    if (risque.id !== undefined) {
      this.subscribeToSaveResponse(this.risqueService.update(risque));
    } else {
      this.subscribeToSaveResponse(this.risqueService.create(risque));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRisque>>): void {
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

  protected updateForm(risque: IRisque): void {
    this.editForm.patchValue({
      id: risque.id,
      dateIdentification: risque.dateIdentification,
      description: risque.description,
      causePotentielle: risque.causePotentielle,
      effetPotentiel: risque.effetPotentiel,
      type: risque.type,
      gravite: risque.gravite,
      probabilite: risque.probabilite,
      criticite: risque.criticite,
      traitement: risque.traitement,
      commentaire: risque.commentaire,
      origine: risque.origine,
      action: risque.action,
      delegue: risque.delegue,
      processus: risque.processus,
    });

    this.actionsSharedCollection = this.actionService.addActionToCollectionIfMissing(this.actionsSharedCollection, risque.action);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, risque.delegue);
    this.processusSMISSharedCollection = this.processusSMIService.addProcessusSMIToCollectionIfMissing(
      this.processusSMISSharedCollection,
      risque.processus
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

  protected createFromForm(): IRisque {
    return {
      ...new Risque(),
      id: this.editForm.get(['id'])!.value,
      dateIdentification: this.editForm.get(['dateIdentification'])!.value,
      description: this.editForm.get(['description'])!.value,
      causePotentielle: this.editForm.get(['causePotentielle'])!.value,
      effetPotentiel: this.editForm.get(['effetPotentiel'])!.value,
      type: this.editForm.get(['type'])!.value,
      gravite: this.editForm.get(['gravite'])!.value,
      probabilite: this.editForm.get(['probabilite'])!.value,
      criticite: this.editForm.get(['criticite'])!.value,
      traitement: this.editForm.get(['traitement'])!.value,
      commentaire: this.editForm.get(['commentaire'])!.value,
      origine: this.editForm.get(['origine'])!.value,
      action: this.editForm.get(['action'])!.value,
      delegue: this.editForm.get(['delegue'])!.value,
      processus: this.editForm.get(['processus'])!.value,
    };
  }
}
