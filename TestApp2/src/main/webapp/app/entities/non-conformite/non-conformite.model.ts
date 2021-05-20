import * as dayjs from 'dayjs';
import { IAction } from 'app/entities/action/action.model';
import { IUser } from 'app/entities/user/user.model';
import { IProcessusSMI } from 'app/entities/processus-smi/processus-smi.model';

export interface INonConformite {
  id?: number;
  date?: dayjs.Dayjs | null;
  description?: string | null;
  causesPotentielles?: string | null;
  origine?: string | null;
  action?: IAction | null;
  delegue?: IUser | null;
  processus?: IProcessusSMI | null;
}

export class NonConformite implements INonConformite {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public description?: string | null,
    public causesPotentielles?: string | null,
    public origine?: string | null,
    public action?: IAction | null,
    public delegue?: IUser | null,
    public processus?: IProcessusSMI | null
  ) {}
}

export function getNonConformiteIdentifier(nonConformite: INonConformite): number | undefined {
  return nonConformite.id;
}
