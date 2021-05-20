import * as dayjs from 'dayjs';
import { IUser } from 'app/entities-own/user/user.model';
import { IAudit } from 'app/entities-own/audit/audit.model';
import { TypeProcessus } from 'app/entities-own/enumerations/type-processus.model';

export interface IProcessusSMI {
  id?: number;
  processus?: string | null;
  date?: dayjs.Dayjs | null;
  version?: number | null;
  finalite?: string | null;
  ficheProcessusContentType?: string | null;
  ficheProcessus?: string | null;
  type?: TypeProcessus | null;
  vigueur?: boolean | null;
  pilote?: IUser | null;
  audit?: IAudit | null;
}

export class ProcessusSMI implements IProcessusSMI {
  constructor(
    public id?: number,
    public processus?: string | null,
    public date?: dayjs.Dayjs | null,
    public version?: number | null,
    public finalite?: string | null,
    public ficheProcessusContentType?: string | null,
    public ficheProcessus?: string | null,
    public type?: TypeProcessus | null,
    public vigueur?: boolean | null,
    public pilote?: IUser | null,
    public audit?: IAudit | null
  ) {
    this.vigueur = this.vigueur ?? false;
  }
}

export function getProcessusSMIIdentifier(processusSMI: IProcessusSMI): number | undefined {
  return processusSMI.id;
}
