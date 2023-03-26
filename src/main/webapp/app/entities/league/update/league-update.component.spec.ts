import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LeagueFormService } from './league-form.service';
import { LeagueService } from '../service/league.service';
import { ILeague } from '../league.model';

import { LeagueUpdateComponent } from './league-update.component';

describe('League Management Update Component', () => {
  let comp: LeagueUpdateComponent;
  let fixture: ComponentFixture<LeagueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leagueFormService: LeagueFormService;
  let leagueService: LeagueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LeagueUpdateComponent],
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
      .overrideTemplate(LeagueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeagueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leagueFormService = TestBed.inject(LeagueFormService);
    leagueService = TestBed.inject(LeagueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const league: ILeague = { id: 456 };

      activatedRoute.data = of({ league });
      comp.ngOnInit();

      expect(comp.league).toEqual(league);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeague>>();
      const league = { id: 123 };
      jest.spyOn(leagueFormService, 'getLeague').mockReturnValue(league);
      jest.spyOn(leagueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ league });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: league }));
      saveSubject.complete();

      // THEN
      expect(leagueFormService.getLeague).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(leagueService.update).toHaveBeenCalledWith(expect.objectContaining(league));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeague>>();
      const league = { id: 123 };
      jest.spyOn(leagueFormService, 'getLeague').mockReturnValue({ id: null });
      jest.spyOn(leagueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ league: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: league }));
      saveSubject.complete();

      // THEN
      expect(leagueFormService.getLeague).toHaveBeenCalled();
      expect(leagueService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeague>>();
      const league = { id: 123 };
      jest.spyOn(leagueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ league });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leagueService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
