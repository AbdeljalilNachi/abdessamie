import * as dayjs from 'dayjs';
import { IAction } from 'app/entities-own/action/action.model';
import { IUser } from 'app/entities-own/user/user.model';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { Rubrique } from 'app/entities-own/enumerations/rubrique.model';

export interface IObligationConformite {
  id?: number;
  date?: dayjs.Dayjs | null;
  rubrique?: Rubrique | null;
  reference?: string | null;
  num?: number | null;
  exigence?: string | null;
  applicable?: boolean | null;
  conforme?: boolean | null;
  statut?: number | null;
  observation?: string | null;
  origine?: string | null;
  action?: IAction | null;
  delegue?: IUser | null;
  processus?: IProcessusSMI | null;
}

export class ObligationConformite implements IObligationConformite {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public rubrique?: Rubrique | null,
    public reference?: string | null,
    public num?: number | null,
    public exigence?: string | null,
    public applicable?: boolean | null,
    public conforme?: boolean | null,
    public statut?: number | null,
    public observation?: string | null,
    public origine?: string | null,
    public action?: IAction | null,
    public delegue?: IUser | null,
    public processus?: IProcessusSMI | null
  ) {
    this.applicable = this.applicable ?? false;
    this.conforme = this.conforme ?? false;
  }
}

export function getObligationConformiteIdentifier(obligationConformite: IObligationConformite): number | undefined {
  return obligationConformite.id;
}
