import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAudit, Audit } from '../audit.model';
import { AuditService } from '../service/audit.service';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities-own/processus-smi/service/processus-smi.service';
import { IUser } from 'app/entities-own/user/user.model';
import { UserService } from 'app/entities-own/user/user.service';
import { DatePipe } from '@angular/common'

@Component({
  selector: 'jhi-audit-update',
  templateUrl: './audit-update.component.html',
})
export class AuditUpdateComponent implements OnInit {
  isSaving = false;

  processusSMISSharedCollection: IProcessusSMI[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    dateAudit: [],
    typeAudit: [],
    standard: [],
    iD: [],
    statut: [],
    conclusion: [],
    processus: [],
    auditeur: [],
  });

  constructor(
    public datepipe: DatePipe,
    protected auditService: AuditService,
    protected processusSMIService: ProcessusSMIService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}





IdOnChange() : void{

(<HTMLInputElement>document.getElementById("field_iD")).value = "Audit"+" "+ (this.editForm.get(['typeAudit'])!.value as unknown as string) +" "+ (this.editForm.get(['dateAudit'])!.value.toString() as unknown as string) ;

}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ audit }) => {
      this.updateForm(audit);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const audit = this.createFromForm();
    if (audit.id !== undefined) {
      this.subscribeToSaveResponse(this.auditService.update(audit));
    } else {
      this.subscribeToSaveResponse(this.auditService.create(audit));
    }
  }

  trackProcessusSMIById(index: number, item: IProcessusSMI): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAudit>>): void {
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

  protected updateForm(audit: IAudit): void {
    this.editForm.patchValue({
      id: audit.id,
      dateAudit: audit.dateAudit,
      typeAudit: audit.typeAudit,
      standard: audit.standard,
      iD: audit.iD,
      statut: audit.statut,
      conclusion: audit.conclusion,
      processus: audit.processus,
      auditeur: audit.auditeur,
    });

    this.processusSMISSharedCollection = this.processusSMIService.addProcessusSMIToCollectionIfMissing(
      this.processusSMISSharedCollection,
      audit.processus
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, audit.auditeur);
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

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('auditeur')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IAudit {
    return {
      ...new Audit(),
      id: this.editForm.get(['id'])!.value,
      dateAudit: this.editForm.get(['dateAudit'])!.value,
      typeAudit: this.editForm.get(['typeAudit'])!.value,
      standard: this.editForm.get(['standard'])!.value,
      iD: this.editForm.get(['iD'])!.value,
      statut: this.editForm.get(['statut'])!.value,
      conclusion: this.editForm.get(['conclusion'])!.value,
      processus: this.editForm.get(['processus'])!.value,
      auditeur: this.editForm.get(['auditeur'])!.value,
    };
  }
}
