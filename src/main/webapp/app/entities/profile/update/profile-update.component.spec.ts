import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProfileFormService } from './profile-form.service';
import { ProfileService } from '../service/profile.service';
import { IProfile } from '../profile.model';
import { IBaptismHistory } from 'app/entities/baptism-history/baptism-history.model';
import { BaptismHistoryService } from 'app/entities/baptism-history/service/baptism-history.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ILeague } from 'app/entities/league/league.model';
import { LeagueService } from 'app/entities/league/service/league.service';
import { IMinistry } from 'app/entities/ministry/ministry.model';
import { MinistryService } from 'app/entities/ministry/service/ministry.service';

import { ProfileUpdateComponent } from './profile-update.component';

describe('Profile Management Update Component', () => {
  let comp: ProfileUpdateComponent;
  let fixture: ComponentFixture<ProfileUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let profileFormService: ProfileFormService;
  let profileService: ProfileService;
  let baptismHistoryService: BaptismHistoryService;
  let userService: UserService;
  let leagueService: LeagueService;
  let ministryService: MinistryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProfileUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProfileUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProfileUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    profileFormService = TestBed.inject(ProfileFormService);
    profileService = TestBed.inject(ProfileService);
    baptismHistoryService = TestBed.inject(BaptismHistoryService);
    userService = TestBed.inject(UserService);
    leagueService = TestBed.inject(LeagueService);
    ministryService = TestBed.inject(MinistryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call baptismHistory query and add missing value', () => {
      const profile: IProfile = { id: 456 };
      const baptismHistory: IBaptismHistory = { id: 93908 };
      profile.baptismHistory = baptismHistory;

      const baptismHistoryCollection: IBaptismHistory[] = [{ id: 15760 }];
      jest.spyOn(baptismHistoryService, 'query').mockReturnValue(of(new HttpResponse({ body: baptismHistoryCollection })));
      const expectedCollection: IBaptismHistory[] = [baptismHistory, ...baptismHistoryCollection];
      jest.spyOn(baptismHistoryService, 'addBaptismHistoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ profile });
      comp.ngOnInit();

      expect(baptismHistoryService.query).toHaveBeenCalled();
      expect(baptismHistoryService.addBaptismHistoryToCollectionIfMissing).toHaveBeenCalledWith(baptismHistoryCollection, baptismHistory);
      expect(comp.baptismHistoriesCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const profile: IProfile = { id: 456 };
      const user: IUser = { id: 81890 };
      profile.user = user;

      const userCollection: IUser[] = [{ id: 61017 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ profile });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call League query and add missing value', () => {
      const profile: IProfile = { id: 456 };
      const league: ILeague = { id: 80305 };
      profile.league = league;

      const leagueCollection: ILeague[] = [{ id: 99502 }];
      jest.spyOn(leagueService, 'query').mockReturnValue(of(new HttpResponse({ body: leagueCollection })));
      const additionalLeagues = [league];
      const expectedCollection: ILeague[] = [...additionalLeagues, ...leagueCollection];
      jest.spyOn(leagueService, 'addLeagueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ profile });
      comp.ngOnInit();

      expect(leagueService.query).toHaveBeenCalled();
      expect(leagueService.addLeagueToCollectionIfMissing).toHaveBeenCalledWith(
        leagueCollection,
        ...additionalLeagues.map(expect.objectContaining)
      );
      expect(comp.leaguesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Ministry query and add missing value', () => {
      const profile: IProfile = { id: 456 };
      const ministry: IMinistry = { id: 49580 };
      profile.ministry = ministry;

      const ministryCollection: IMinistry[] = [{ id: 43143 }];
      jest.spyOn(ministryService, 'query').mockReturnValue(of(new HttpResponse({ body: ministryCollection })));
      const additionalMinistries = [ministry];
      const expectedCollection: IMinistry[] = [...additionalMinistries, ...ministryCollection];
      jest.spyOn(ministryService, 'addMinistryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ profile });
      comp.ngOnInit();

      expect(ministryService.query).toHaveBeenCalled();
      expect(ministryService.addMinistryToCollectionIfMissing).toHaveBeenCalledWith(
        ministryCollection,
        ...additionalMinistries.map(expect.objectContaining)
      );
      expect(comp.ministriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const profile: IProfile = { id: 456 };
      const baptismHistory: IBaptismHistory = { id: 62462 };
      profile.baptismHistory = baptismHistory;
      const user: IUser = { id: 63630 };
      profile.user = user;
      const league: ILeague = { id: 20261 };
      profile.league = league;
      const ministry: IMinistry = { id: 70392 };
      profile.ministry = ministry;

      activatedRoute.data = of({ profile });
      comp.ngOnInit();

      expect(comp.baptismHistoriesCollection).toContain(baptismHistory);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.leaguesSharedCollection).toContain(league);
      expect(comp.ministriesSharedCollection).toContain(ministry);
      expect(comp.profile).toEqual(profile);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfile>>();
      const profile = { id: 123 };
      jest.spyOn(profileFormService, 'getProfile').mockReturnValue(profile);
      jest.spyOn(profileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: profile }));
      saveSubject.complete();

      // THEN
      expect(profileFormService.getProfile).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(profileService.update).toHaveBeenCalledWith(expect.objectContaining(profile));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfile>>();
      const profile = { id: 123 };
      jest.spyOn(profileFormService, 'getProfile').mockReturnValue({ id: null });
      jest.spyOn(profileService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profile: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: profile }));
      saveSubject.complete();

      // THEN
      expect(profileFormService.getProfile).toHaveBeenCalled();
      expect(profileService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfile>>();
      const profile = { id: 123 };
      jest.spyOn(profileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(profileService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBaptismHistory', () => {
      it('Should forward to baptismHistoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(baptismHistoryService, 'compareBaptismHistory');
        comp.compareBaptismHistory(entity, entity2);
        expect(baptismHistoryService.compareBaptismHistory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareLeague', () => {
      it('Should forward to leagueService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(leagueService, 'compareLeague');
        comp.compareLeague(entity, entity2);
        expect(leagueService.compareLeague).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMinistry', () => {
      it('Should forward to ministryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ministryService, 'compareMinistry');
        comp.compareMinistry(entity, entity2);
        expect(ministryService.compareMinistry).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
