import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMinistry, NewMinistry } from '../ministry.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMinistry for edit and NewMinistryFormGroupInput for create.
 */
type MinistryFormGroupInput = IMinistry | PartialWithRequiredKeyOf<NewMinistry>;

type MinistryFormDefaults = Pick<NewMinistry, 'id'>;

type MinistryFormGroupContent = {
  id: FormControl<IMinistry['id'] | NewMinistry['id']>;
  name: FormControl<IMinistry['name']>;
  description: FormControl<IMinistry['description']>;
};

export type MinistryFormGroup = FormGroup<MinistryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MinistryFormService {
  createMinistryFormGroup(ministry: MinistryFormGroupInput = { id: null }): MinistryFormGroup {
    const ministryRawValue = {
      ...this.getFormDefaults(),
      ...ministry,
    };
    return new FormGroup<MinistryFormGroupContent>({
      id: new FormControl(
        { value: ministryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(ministryRawValue.name, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(100)],
      }),
      description: new FormControl(ministryRawValue.description, {
        validators: [Validators.required],
      }),
    });
  }

  getMinistry(form: MinistryFormGroup): IMinistry | NewMinistry {
    return form.getRawValue() as IMinistry | NewMinistry;
  }

  resetForm(form: MinistryFormGroup, ministry: MinistryFormGroupInput): void {
    const ministryRawValue = { ...this.getFormDefaults(), ...ministry };
    form.reset(
      {
        ...ministryRawValue,
        id: { value: ministryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MinistryFormDefaults {
    return {
      id: null,
    };
  }
}
