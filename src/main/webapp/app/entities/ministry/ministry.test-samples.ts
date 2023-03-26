import { IMinistry, NewMinistry } from './ministry.model';

export const sampleWithRequiredData: IMinistry = {
  id: 55642,
  name: 'rich tan channels',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IMinistry = {
  id: 90241,
  name: 'Accounts',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IMinistry = {
  id: 19165,
  name: 'virtual Investor purple',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewMinistry = {
  name: 'web-enabled payment',
  description: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
