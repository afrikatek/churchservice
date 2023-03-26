import dayjs from 'dayjs/esm';

import { IBaptismHistory, NewBaptismHistory } from './baptism-history.model';

export const sampleWithRequiredData: IBaptismHistory = {
  id: 89574,
  lutheran: false,
};

export const sampleWithPartialData: IBaptismHistory = {
  id: 49986,
  lutheran: true,
  baptised: true,
  baptismDate: dayjs('2023-03-26'),
  baptisedAt: dayjs('2023-03-25'),
  confirmed: true,
  confirmationDate: dayjs('2023-03-26'),
  married: true,
  parishMarriedAt: 'Bike',
};

export const sampleWithFullData: IBaptismHistory = {
  id: 98957,
  lutheran: true,
  previousParish: 'deposit bandwidth',
  baptised: true,
  baptismDate: dayjs('2023-03-26'),
  baptisedAt: dayjs('2023-03-26'),
  confirmed: false,
  confirmationDate: dayjs('2023-03-26'),
  parishConfirmed: 'New Alaska white',
  married: false,
  marriageDate: dayjs('2023-03-26'),
  parishMarriedAt: 'portals Cotton parad',
};

export const sampleWithNewData: NewBaptismHistory = {
  lutheran: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
