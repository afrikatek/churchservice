import dayjs from 'dayjs/esm';
import { IBaptismHistory } from 'app/entities/baptism-history/baptism-history.model';
import { IUser } from 'app/entities/user/user.model';
import { ILeague } from 'app/entities/league/league.model';
import { IMinistry } from 'app/entities/ministry/ministry.model';
import { Title } from 'app/entities/enumerations/title.model';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface IProfile {
  id: number;
  title?: Title | null;
  firstName?: string | null;
  secondNames?: string | null;
  lastName?: string | null;
  idNumber?: string | null;
  gender?: Gender | null;
  dateOfBirth?: dayjs.Dayjs | null;
  profileImage?: string | null;
  profileImageContentType?: string | null;
  profession?: string | null;
  baptismHistory?: Pick<IBaptismHistory, 'id'> | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  league?: Pick<ILeague, 'id' | 'name'> | null;
  ministry?: Pick<IMinistry, 'id' | 'name'> | null;
}

export type NewProfile = Omit<IProfile, 'id'> & { id: null };
