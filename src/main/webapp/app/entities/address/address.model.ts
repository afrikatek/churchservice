import { IProfile } from 'app/entities/profile/profile.model';
import { AddressType } from 'app/entities/enumerations/address-type.model';

export interface IAddress {
  id: number;
  street?: string | null;
  city?: string | null;
  province?: string | null;
  country?: string | null;
  addressType?: AddressType | null;
  telephoneWork?: string | null;
  telephoneHome?: string | null;
  cellphone?: string | null;
  profile?: Pick<IProfile, 'id'> | null;
}

export type NewAddress = Omit<IAddress, 'id'> & { id: null };
