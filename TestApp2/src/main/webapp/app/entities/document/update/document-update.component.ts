import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDocument, Document } from '../document.model';
import { DocumentService } from '../service/document.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProcessusSMI } from 'app/entities/processus-smi/processus-smi.model';
import { ProcessusSMIService } from 'app/entities/processus-smi/service/processus-smi.service';

@Component({
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html',
})
export class DocumentUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  processusSMISSharedCollection: IProcessusSMI[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    intitule: [],
    code: [],
    version: [],
    type: [],
    pieceJointe: [],
    pieceJointeContentType: [],
    enApplication: [],
    appouve: [],
    acces: [],
    processus: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected documentService: DocumentService,
    protected userService: UserService,
    protected processusSMIService: ProcessusSMIService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ document }) => {
      this.updateForm(document);

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
    const document = this.createFromForm();
    if (document.id !== undefined) {
      this.subscribeToSaveResponse(this.documentService.update(document));
    } else {
      this.subscribeToSaveResponse(this.documentService.create(document));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackProcessusSMIById(index: number, item: IProcessusSMI): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocument>>): void {
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

  protected updateForm(document: IDocument): void {
    this.editForm.patchValue({
      id: document.id,
      date: document.date,
      intitule: document.intitule,
      code: document.code,
      version: document.version,
      type: document.type,
      pieceJointe: document.pieceJointe,
      pieceJointeContentType: document.pieceJointeContentType,
      enApplication: document.enApplication,
      appouve: document.appouve,
      acces: document.acces,
      processus: document.processus,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, document.acces);
    this.processusSMISSharedCollection = this.processusSMIService.addProcessusSMIToCollectionIfMissing(
      this.processusSMISSharedCollection,
      document.processus
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('acces')!.value)))
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

  protected createFromForm(): IDocument {
    return {
      ...new Document(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      intitule: this.editForm.get(['intitule'])!.value,
      code: this.editForm.get(['code'])!.value,
      version: this.editForm.get(['version'])!.value,
      type: this.editForm.get(['type'])!.value,
      pieceJointeContentType: this.editForm.get(['pieceJointeContentType'])!.value,
      pieceJointe: this.editForm.get(['pieceJointe'])!.value,
      enApplication: this.editForm.get(['enApplication'])!.value,
      appouve: this.editForm.get(['appouve'])!.value,
      acces: this.editForm.get(['acces'])!.value,
      processus: this.editForm.get(['processus'])!.value,
    };
  }
}
