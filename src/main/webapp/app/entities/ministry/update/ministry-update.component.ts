import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { MinistryFormService, MinistryFormGroup } from './ministry-form.service';
import { IMinistry } from '../ministry.model';
import { MinistryService } from '../service/ministry.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-ministry-update',
  templateUrl: './ministry-update.component.html',
})
export class MinistryUpdateComponent implements OnInit {
  isSaving = false;
  ministry: IMinistry | null = null;

  editForm: MinistryFormGroup = this.ministryFormService.createMinistryFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected ministryService: MinistryService,
    protected ministryFormService: MinistryFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ministry }) => {
      this.ministry = ministry;
      if (ministry) {
        this.updateForm(ministry);
      }
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
        this.eventManager.broadcast(new EventWithContent<AlertError>('churchserviceApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ministry = this.ministryFormService.getMinistry(this.editForm);
    if (ministry.id !== null) {
      this.subscribeToSaveResponse(this.ministryService.update(ministry));
    } else {
      this.subscribeToSaveResponse(this.ministryService.create(ministry));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMinistry>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
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

  protected updateForm(ministry: IMinistry): void {
    this.ministry = ministry;
    this.ministryFormService.resetForm(this.editForm, ministry);
  }
}
