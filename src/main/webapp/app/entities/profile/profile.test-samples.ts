import dayjs from 'dayjs/esm';

import { Title } from 'app/entities/enumerations/title.model';
import { Gender } from 'app/entities/enumerations/gender.model';

import { IProfile, NewProfile } from './profile.model';

export const sampleWithRequiredData: IProfile = {
  id: 11865,
  title: Title['SPINSTER'],
  firstName: 'Roberto',
  lastName: 'Mohr',
  idNumber: 'solid Unions Cu',
  gender: Gender['MALE'],
  dateOfBirth: dayjs('2023-03-26'),
};

export const sampleWithPartialData: IProfile = {
  id: 28564,
  title: Title['BACHELOR'],
  firstName: 'Elna',
  secondNames: 'SMS copy Customer',
  lastName: 'Beatty',
  idNumber: 'technologies di',
  gender: Gender['MALE'],
  dateOfBirth: dayjs('2023-03-25'),
  profileImage: '../fake-data/blob/hipster.png',
  profileImageContentType: 'unknown',
  profession: 'up RAM blockchains',
};

export const sampleWithFullData: IProfile = {
  id: 11506,
  title: Title['DOCTOR'],
  firstName: 'Domenica',
  secondNames: 'Montana Senior Minnesota',
  lastName: 'Schultz',
  idNumber: 'intuitive Bedfo',
  gender: Gender['MALE'],
  dateOfBirth: dayjs('2023-03-26'),
  profileImage: '../fake-data/blob/hipster.png',
  profileImageContentType: 'unknown',
  profession: 'Micronesia Turnpike',
};

export const sampleWithNewData: NewProfile = {
  title: Title['MS'],
  firstName: 'Roscoe',
  lastName: 'Hessel',
  idNumber: 'HDD',
  gender: Gender['FEMALE'],
  dateOfBirth: dayjs('2023-03-26'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
