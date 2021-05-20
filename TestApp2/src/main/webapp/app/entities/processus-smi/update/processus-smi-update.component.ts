import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProcessusSMI, ProcessusSMI } from '../processus-smi.model';
import { ProcessusSMIService } from '../service/processus-smi.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-processus-smi-update',
  templateUrl: './processus-smi-update.component.html',
})
export class ProcessusSMIUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    processus: [],
    date: [],
    version: [],
    finalite: [],
    ficheProcessus: [],
    ficheProcessusContentType: [],
    type: [],
    vigueur: [],
    pilote: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected processusSMIService: ProcessusSMIService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ processusSMI }) => {
      this.updateForm(processusSMI);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('newbetterflyApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const processusSMI = this.createFromForm();
    if (processusSMI.id !== undefined) {
      this.subscribeToSaveResponse(this.processusSMIService.update(processusSMI));
    } else {
      this.subscribeToSaveResponse(this.processusSMIService.create(processusSMI));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProcessusSMI>>): void {
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

  protected updateForm(processusSMI: IProcessusSMI): void {
    this.editForm.patchValue({
      id: processusSMI.id,
      processus: processusSMI.processus,
      date: processusSMI.date,
      version: processusSMI.version,
      finalite: processusSMI.finalite,
      ficheProcessus: processusSMI.ficheProcessus,
      ficheProcessusContentType: processusSMI.ficheProcessusContentType,
      type: processusSMI.type,
      vigueur: processusSMI.vigueur,
      pilote: processusSMI.pilote,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, processusSMI.pilote);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('pilote')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IProcessusSMI {
    return {
      ...new ProcessusSMI(),
      id: this.editForm.get(['id'])!.value,
      processus: this.editForm.get(['processus'])!.value,
      date: this.editForm.get(['date'])!.value,
      version: this.editForm.get(['version'])!.value,
      finalite: this.editForm.get(['finalite'])!.value,
      ficheProcessusContentType: this.editForm.get(['ficheProcessusContentType'])!.value,
      ficheProcessus: this.editForm.get(['ficheProcessus'])!.value,
      type: this.editForm.get(['type'])!.value,
      vigueur: this.editForm.get(['vigueur'])!.value,
      pilote: this.editForm.get(['pilote'])!.value,
    };
  }
}
