export interface ILeague {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewLeague = Omit<ILeague, 'id'> & { id: null };
