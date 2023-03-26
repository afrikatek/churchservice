import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAddress, NewAddress } from '../address.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAddress for edit and NewAddressFormGroupInput for create.
 */
type AddressFormGroupInput = IAddress | PartialWithRequiredKeyOf<NewAddress>;

type AddressFormDefaults = Pick<NewAddress, 'id'>;

type AddressFormGroupContent = {
  id: FormControl<IAddress['id'] | NewAddress['id']>;
  street: FormControl<IAddress['street']>;
  city: FormControl<IAddress['city']>;
  province: FormControl<IAddress['province']>;
  country: FormControl<IAddress['country']>;
  addressType: FormControl<IAddress['addressType']>;
  telephoneWork: FormControl<IAddress['telephoneWork']>;
  telephoneHome: FormControl<IAddress['telephoneHome']>;
  cellphone: FormControl<IAddress['cellphone']>;
  profile: FormControl<IAddress['profile']>;
};

export type AddressFormGroup = FormGroup<AddressFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AddressFormService {
  createAddressFormGroup(address: AddressFormGroupInput = { id: null }): AddressFormGroup {
    const addressRawValue = {
      ...this.getFormDefaults(),
      ...address,
    };
    return new FormGroup<AddressFormGroupContent>({
      id: new FormControl(
        { value: addressRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      street: new FormControl(addressRawValue.street, {
        validators: [Validators.required],
      }),
      city: new FormControl(addressRawValue.city, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(50)],
      }),
      province: new FormControl(addressRawValue.province, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(50)],
      }),
      country: new FormControl(addressRawValue.country, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(50)],
      }),
      addressType: new FormControl(addressRawValue.addressType, {
        validators: [Validators.required],
      }),
      telephoneWork: new FormControl(addressRawValue.telephoneWork, {
        validators: [Validators.minLength(3), Validators.maxLength(15)],
      }),
      telephoneHome: new FormControl(addressRawValue.telephoneHome, {
        validators: [Validators.minLength(3), Validators.maxLength(15)],
      }),
      cellphone: new FormControl(addressRawValue.cellphone, {
        validators: [Validators.minLength(3), Validators.maxLength(15)],
      }),
      profile: new FormControl(addressRawValue.profile),
    });
  }

  getAddress(form: AddressFormGroup): IAddress | NewAddress {
    return form.getRawValue() as IAddress | NewAddress;
  }

  resetForm(form: AddressFormGroup, address: AddressFormGroupInput): void {
    const addressRawValue = { ...this.getFormDefaults(), ...address };
    form.reset(
      {
        ...addressRawValue,
        id: { value: addressRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AddressFormDefaults {
    return {
      id: null,
    };
  }
}
