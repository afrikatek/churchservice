import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILeague, NewLeague } from '../league.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILeague for edit and NewLeagueFormGroupInput for create.
 */
type LeagueFormGroupInput = ILeague | PartialWithRequiredKeyOf<NewLeague>;

type LeagueFormDefaults = Pick<NewLeague, 'id'>;

type LeagueFormGroupContent = {
  id: FormControl<ILeague['id'] | NewLeague['id']>;
  name: FormControl<ILeague['name']>;
  description: FormControl<ILeague['description']>;
};

export type LeagueFormGroup = FormGroup<LeagueFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LeagueFormService {
  createLeagueFormGroup(league: LeagueFormGroupInput = { id: null }): LeagueFormGroup {
    const leagueRawValue = {
      ...this.getFormDefaults(),
      ...league,
    };
    return new FormGroup<LeagueFormGroupContent>({
      id: new FormControl(
        { value: leagueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(leagueRawValue.name, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(100)],
      }),
      description: new FormControl(leagueRawValue.description, {
        validators: [Validators.required],
      }),
    });
  }

  getLeague(form: LeagueFormGroup): ILeague | NewLeague {
    return form.getRawValue() as ILeague | NewLeague;
  }

  resetForm(form: LeagueFormGroup, league: LeagueFormGroupInput): void {
    const leagueRawValue = { ...this.getFormDefaults(), ...league };
    form.reset(
      {
        ...leagueRawValue,
        id: { value: leagueRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LeagueFormDefaults {
    return {
      id: null,
    };
  }
}
