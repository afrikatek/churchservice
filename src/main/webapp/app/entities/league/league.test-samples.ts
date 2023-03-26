import { ILeague, NewLeague } from './league.model';

export const sampleWithRequiredData: ILeague = {
  id: 11233,
  name: 'streamline Automated Manager',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: ILeague = {
  id: 76819,
  name: 'payment Small',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ILeague = {
  id: 44,
  name: 'interfaces firewall',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewLeague = {
  name: 'Tasty invoice',
  description: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
