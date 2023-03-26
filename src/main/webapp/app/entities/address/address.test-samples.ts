import { AddressType } from 'app/entities/enumerations/address-type.model';

import { IAddress, NewAddress } from './address.model';

export const sampleWithRequiredData: IAddress = {
  id: 88754,
  street: '../fake-data/blob/hipster.txt',
  city: 'Prohaskaton',
  province: 'application Avon',
  country: 'Tokelau',
  addressType: AddressType['POSTAL_ADDRESS'],
};

export const sampleWithPartialData: IAddress = {
  id: 94531,
  street: '../fake-data/blob/hipster.txt',
  city: 'Garden Grove',
  province: 'efficient Montana',
  country: 'Spain',
  addressType: AddressType['POSTAL_ADDRESS'],
  telephoneHome: 'Cape Missouri',
  cellphone: 'Outdoors',
};

export const sampleWithFullData: IAddress = {
  id: 80702,
  street: '../fake-data/blob/hipster.txt',
  city: 'West Camilla',
  province: 'black',
  country: 'Benin',
  addressType: AddressType['PHYSICAL_ADDRESS'],
  telephoneWork: 'Handcrafted Out',
  telephoneHome: 'hacking network',
  cellphone: 'intranet',
};

export const sampleWithNewData: NewAddress = {
  street: '../fake-data/blob/hipster.txt',
  city: 'Lake Candidahaven',
  province: 'dynamic Birr experiences',
  country: 'Dominica',
  addressType: AddressType['PHYSICAL_ADDRESS'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
