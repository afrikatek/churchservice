import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProfile, NewProfile } from '../profile.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProfile for edit and NewProfileFormGroupInput for create.
 */
type ProfileFormGroupInput = IProfile | PartialWithRequiredKeyOf<NewProfile>;

type ProfileFormDefaults = Pick<NewProfile, 'id'>;

type ProfileFormGroupContent = {
  id: FormControl<IProfile['id'] | NewProfile['id']>;
  title: FormControl<IProfile['title']>;
  firstName: FormControl<IProfile['firstName']>;
  secondNames: FormControl<IProfile['secondNames']>;
  lastName: FormControl<IProfile['lastName']>;
  idNumber: FormControl<IProfile['idNumber']>;
  gender: FormControl<IProfile['gender']>;
  dateOfBirth: FormControl<IProfile['dateOfBirth']>;
  profileImage: FormControl<IProfile['profileImage']>;
  profileImageContentType: FormControl<IProfile['profileImageContentType']>;
  profession: FormControl<IProfile['profession']>;
  baptismHistory: FormControl<IProfile['baptismHistory']>;
  user: FormControl<IProfile['user']>;
  league: FormControl<IProfile['league']>;
  ministry: FormControl<IProfile['ministry']>;
};

export type ProfileFormGroup = FormGroup<ProfileFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProfileFormService {
  createProfileFormGroup(profile: ProfileFormGroupInput = { id: null }): ProfileFormGroup {
    const profileRawValue = {
      ...this.getFormDefaults(),
      ...profile,
    };
    return new FormGroup<ProfileFormGroupContent>({
      id: new FormControl(
        { value: profileRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(profileRawValue.title, {
        validators: [Validators.required],
      }),
      firstName: new FormControl(profileRawValue.firstName, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(50)],
      }),
      secondNames: new FormControl(profileRawValue.secondNames, {
        validators: [Validators.minLength(3), Validators.maxLength(150)],
      }),
      lastName: new FormControl(profileRawValue.lastName, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(50)],
      }),
      idNumber: new FormControl(profileRawValue.idNumber, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(15)],
      }),
      gender: new FormControl(profileRawValue.gender, {
        validators: [Validators.required],
      }),
      dateOfBirth: new FormControl(profileRawValue.dateOfBirth, {
        validators: [Validators.required],
      }),
      profileImage: new FormControl(profileRawValue.profileImage),
      profileImageContentType: new FormControl(profileRawValue.profileImageContentType),
      profession: new FormControl(profileRawValue.profession, {
        validators: [Validators.minLength(3), Validators.maxLength(100)],
      }),
      baptismHistory: new FormControl(profileRawValue.baptismHistory),
      user: new FormControl(profileRawValue.user, {
        validators: [Validators.required],
      }),
      league: new FormControl(profileRawValue.league),
      ministry: new FormControl(profileRawValue.ministry),
    });
  }

  getProfile(form: ProfileFormGroup): IProfile | NewProfile {
    return form.getRawValue() as IProfile | NewProfile;
  }

  resetForm(form: ProfileFormGroup, profile: ProfileFormGroupInput): void {
    const profileRawValue = { ...this.getFormDefaults(), ...profile };
    form.reset(
      {
        ...profileRawValue,
        id: { value: profileRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProfileFormDefaults {
    return {
      id: null,
    };
  }
}
