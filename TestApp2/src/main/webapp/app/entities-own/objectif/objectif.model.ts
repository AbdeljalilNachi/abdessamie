import { IAction } from 'app/entities-own/action/action.model';
import { IUser } from 'app/entities-own/user/user.model';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { IIndicateurSMI } from 'app/entities-own/indicateur-smi/indicateur-smi.model';

export interface IObjectif {
  id?: number;
  axedelapolitiqueqse?: string | null;
  objectifqse?: string | null;
  origine?: string | null;
  action?: IAction | null;
  delegue?: IUser | null;
  processus?: IProcessusSMI | null;
  indicateur?: IIndicateurSMI | null;
}

export class Objectif implements IObjectif {
  constructor(
    public id?: number,
    public axedelapolitiqueqse?: string | null,
    public objectifqse?: string | null,
    public origine?: string | null,
    public action?: IAction | null,
    public delegue?: IUser | null,
    public processus?: IProcessusSMI | null,
    public indicateur?: IIndicateurSMI | null
  ) {}
}

export function getObjectifIdentifier(objectif: IObjectif): number | undefined {
  return objectif.id;
}
