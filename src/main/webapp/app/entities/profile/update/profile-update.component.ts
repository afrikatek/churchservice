import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProfileFormService, ProfileFormGroup } from './profile-form.service';
import { IProfile } from '../profile.model';
import { ProfileService } from '../service/profile.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IBaptismHistory } from 'app/entities/baptism-history/baptism-history.model';
import { BaptismHistoryService } from 'app/entities/baptism-history/service/baptism-history.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ILeague } from 'app/entities/league/league.model';
import { LeagueService } from 'app/entities/league/service/league.service';
import { IMinistry } from 'app/entities/ministry/ministry.model';
import { MinistryService } from 'app/entities/ministry/service/ministry.service';
import { Title } from 'app/entities/enumerations/title.model';
import { Gender } from 'app/entities/enumerations/gender.model';

@Component({
  selector: 'jhi-profile-update',
  templateUrl: './profile-update.component.html',
})
export class ProfileUpdateComponent implements OnInit {
  isSaving = false;
  profile: IProfile | null = null;
  titleValues = Object.keys(Title);
  genderValues = Object.keys(Gender);

  baptismHistoriesCollection: IBaptismHistory[] = [];
  usersSharedCollection: IUser[] = [];
  leaguesSharedCollection: ILeague[] = [];
  ministriesSharedCollection: IMinistry[] = [];

  editForm: ProfileFormGroup = this.profileFormService.createProfileFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected profileService: ProfileService,
    protected profileFormService: ProfileFormService,
    protected baptismHistoryService: BaptismHistoryService,
    protected userService: UserService,
    protected leagueService: LeagueService,
    protected ministryService: MinistryService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBaptismHistory = (o1: IBaptismHistory | null, o2: IBaptismHistory | null): boolean =>
    this.baptismHistoryService.compareBaptismHistory(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareLeague = (o1: ILeague | null, o2: ILeague | null): boolean => this.leagueService.compareLeague(o1, o2);

  compareMinistry = (o1: IMinistry | null, o2: IMinistry | null): boolean => this.ministryService.compareMinistry(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profile }) => {
      this.profile = profile;
      if (profile) {
        this.updateForm(profile);
      }

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
        this.eventManager.broadcast(new EventWithContent<AlertError>('churchserviceApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const profile = this.profileFormService.getProfile(this.editForm);
    if (profile.id !== null) {
      this.subscribeToSaveResponse(this.profileService.update(profile));
    } else {
      this.subscribeToSaveResponse(this.profileService.create(profile));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfile>>): void {
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

  protected updateForm(profile: IProfile): void {
    this.profile = profile;
    this.profileFormService.resetForm(this.editForm, profile);

    this.baptismHistoriesCollection = this.baptismHistoryService.addBaptismHistoryToCollectionIfMissing<IBaptismHistory>(
      this.baptismHistoriesCollection,
      profile.baptismHistory
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, profile.user);
    this.leaguesSharedCollection = this.leagueService.addLeagueToCollectionIfMissing<ILeague>(this.leaguesSharedCollection, profile.league);
    this.ministriesSharedCollection = this.ministryService.addMinistryToCollectionIfMissing<IMinistry>(
      this.ministriesSharedCollection,
      profile.ministry
    );
  }

  protected loadRelationshipsOptions(): void {
    this.baptismHistoryService
      .query({ filter: 'profile-is-null' })
      .pipe(map((res: HttpResponse<IBaptismHistory[]>) => res.body ?? []))
      .pipe(
        map((baptismHistories: IBaptismHistory[]) =>
          this.baptismHistoryService.addBaptismHistoryToCollectionIfMissing<IBaptismHistory>(baptismHistories, this.profile?.baptismHistory)
        )
      )
      .subscribe((baptismHistories: IBaptismHistory[]) => (this.baptismHistoriesCollection = baptismHistories));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.profile?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.leagueService
      .query()
      .pipe(map((res: HttpResponse<ILeague[]>) => res.body ?? []))
      .pipe(map((leagues: ILeague[]) => this.leagueService.addLeagueToCollectionIfMissing<ILeague>(leagues, this.profile?.league)))
      .subscribe((leagues: ILeague[]) => (this.leaguesSharedCollection = leagues));

    this.ministryService
      .query()
      .pipe(map((res: HttpResponse<IMinistry[]>) => res.body ?? []))
      .pipe(
        map((ministries: IMinistry[]) =>
          this.ministryService.addMinistryToCollectionIfMissing<IMinistry>(ministries, this.profile?.ministry)
        )
      )
      .subscribe((ministries: IMinistry[]) => (this.ministriesSharedCollection = ministries));
  }
}
