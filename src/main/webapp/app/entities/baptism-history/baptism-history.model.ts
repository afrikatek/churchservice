import dayjs from 'dayjs/esm';

export interface IBaptismHistory {
  id: number;
  lutheran?: boolean | null;
  previousParish?: string | null;
  baptised?: boolean | null;
  baptismDate?: dayjs.Dayjs | null;
  baptisedAt?: dayjs.Dayjs | null;
  confirmed?: boolean | null;
  confirmationDate?: dayjs.Dayjs | null;
  parishConfirmed?: string | null;
  married?: boolean | null;
  marriageDate?: dayjs.Dayjs | null;
  parishMarriedAt?: string | null;
}

export type NewBaptismHistory = Omit<IBaptismHistory, 'id'> & { id: null };
