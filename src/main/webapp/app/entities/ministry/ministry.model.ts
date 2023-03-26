export interface IMinistry {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewMinistry = Omit<IMinistry, 'id'> & { id: null };
