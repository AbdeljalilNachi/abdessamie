import { IAction } from 'app/entities-own/action/action.model';
import { IUser } from 'app/entities-own/user/user.model';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { IAudit } from 'app/entities-own/audit/audit.model';
import { TypeConstatAudit } from 'app/entities-own/enumerations/type-constat-audit.model';

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
