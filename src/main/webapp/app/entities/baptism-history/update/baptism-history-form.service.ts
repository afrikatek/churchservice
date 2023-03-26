import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBaptismHistory, NewBaptismHistory } from '../baptism-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBaptismHistory for edit and NewBaptismHistoryFormGroupInput for create.
 */
type BaptismHistoryFormGroupInput = IBaptismHistory | PartialWithRequiredKeyOf<NewBaptismHistory>;

type BaptismHistoryFormDefaults = Pick<NewBaptismHistory, 'id' | 'lutheran' | 'baptised' | 'confirmed' | 'married'>;

type BaptismHistoryFormGroupContent = {
  id: FormControl<IBaptismHistory['id'] | NewBaptismHistory['id']>;
  lutheran: FormControl<IBaptismHistory['lutheran']>;
  previousParish: FormControl<IBaptismHistory['previousParish']>;
  baptised: FormControl<IBaptismHistory['baptised']>;
  baptismDate: FormControl<IBaptismHistory['baptismDate']>;
  baptisedAt: FormControl<IBaptismHistory['baptisedAt']>;
  confirmed: FormControl<IBaptismHistory['confirmed']>;
  confirmationDate: FormControl<IBaptismHistory['confirmationDate']>;
  parishConfirmed: FormControl<IBaptismHistory['parishConfirmed']>;
  married: FormControl<IBaptismHistory['married']>;
  marriageDate: FormControl<IBaptismHistory['marriageDate']>;
  parishMarriedAt: FormControl<IBaptismHistory['parishMarriedAt']>;
};

export type BaptismHistoryFormGroup = FormGroup<BaptismHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BaptismHistoryFormService {
  createBaptismHistoryFormGroup(baptismHistory: BaptismHistoryFormGroupInput = { id: null }): BaptismHistoryFormGroup {
    const baptismHistoryRawValue = {
      ...this.getFormDefaults(),
      ...baptismHistory,
    };
    return new FormGroup<BaptismHistoryFormGroupContent>({
      id: new FormControl(
        { value: baptismHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      lutheran: new FormControl(baptismHistoryRawValue.lutheran, {
        validators: [Validators.required],
      }),
      previousParish: new FormControl(baptismHistoryRawValue.previousParish, {
        validators: [Validators.minLength(3), Validators.maxLength(20)],
      }),
      baptised: new FormControl(baptismHistoryRawValue.baptised),
      baptismDate: new FormControl(baptismHistoryRawValue.baptismDate),
      baptisedAt: new FormControl(baptismHistoryRawValue.baptisedAt),
      confirmed: new FormControl(baptismHistoryRawValue.confirmed),
      confirmationDate: new FormControl(baptismHistoryRawValue.confirmationDate),
      parishConfirmed: new FormControl(baptismHistoryRawValue.parishConfirmed, {
        validators: [Validators.minLength(3), Validators.maxLength(20)],
      }),
      married: new FormControl(baptismHistoryRawValue.married),
      marriageDate: new FormControl(baptismHistoryRawValue.marriageDate),
      parishMarriedAt: new FormControl(baptismHistoryRawValue.parishMarriedAt, {
        validators: [Validators.minLength(3), Validators.maxLength(20)],
      }),
    });
  }

  getBaptismHistory(form: BaptismHistoryFormGroup): IBaptismHistory | NewBaptismHistory {
    return form.getRawValue() as IBaptismHistory | NewBaptismHistory;
  }

  resetForm(form: BaptismHistoryFormGroup, baptismHistory: BaptismHistoryFormGroupInput): void {
    const baptismHistoryRawValue = { ...this.getFormDefaults(), ...baptismHistory };
    form.reset(
      {
        ...baptismHistoryRawValue,
        id: { value: baptismHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BaptismHistoryFormDefaults {
    return {
      id: null,
      lutheran: false,
      baptised: false,
      confirmed: false,
      married: false,
    };
  }
}
