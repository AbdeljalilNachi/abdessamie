import * as dayjs from 'dayjs';
import { IAction } from 'app/entities-own/action/action.model';
import { IUser } from 'app/entities-own/user/user.model';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';

export interface IReclamation {
  id?: number;
  date?: dayjs.Dayjs | null;
  description?: string | null;
  justifiee?: boolean | null;
  client?: string | null;
  piecejointeContentType?: string | null;
  piecejointe?: string | null;
  origine?: string | null;
  action?: IAction | null;
  delegue?: IUser | null;
  processus?: IProcessusSMI | null;
}

export class Reclamation implements IReclamation {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public description?: string | null,
    public justifiee?: boolean | null,
    public client?: string | null,
    public piecejointeContentType?: string | null,
    public piecejointe?: string | null,
    public origine?: string | null,
    public action?: IAction | null,
    public delegue?: IUser | null,
    public processus?: IProcessusSMI | null
  ) {
    this.justifiee = this.justifiee ?? false;
  }
}

export function getReclamationIdentifier(reclamation: IReclamation): number | undefined {
  return reclamation.id;
}
