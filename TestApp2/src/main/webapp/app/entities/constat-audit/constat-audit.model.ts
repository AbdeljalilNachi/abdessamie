import { IAction } from 'app/entities/action/action.model';
import { IUser } from 'app/entities/user/user.model';
import { IProcessusSMI } from 'app/entities/processus-smi/processus-smi.model';
import { IAudit } from 'app/entities/audit/audit.model';
import { TypeConstatAudit } from 'app/entities/enumerations/type-constat-audit.model';

export interface IConstatAudit {
  id?: number;
  type?: TypeConstatAudit | null;
  constat?: string | null;
  origine?: string | null;
  action?: IAction | null;
  delegue?: IUser | null;
  processus?: IProcessusSMI | null;
  audit?: IAudit | null;
}

export class ConstatAudit implements IConstatAudit {
  constructor(
    public id?: number,
    public type?: TypeConstatAudit | null,
    public constat?: string | null,
    public origine?: string | null,
    public action?: IAction | null,
    public delegue?: IUser | null,
    public processus?: IProcessusSMI | null,
    public audit?: IAudit | null
  ) {}
}

export function getConstatAuditIdentifier(constatAudit: IConstatAudit): number | undefined {
  return constatAudit.id;
}
