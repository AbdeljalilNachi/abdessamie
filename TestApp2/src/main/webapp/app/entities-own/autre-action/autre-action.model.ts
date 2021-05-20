import { IAction } from 'app/entities-own/action/action.model';
import { IUser } from 'app/entities-own/user/user.model';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';

export interface IAutreAction {
  id?: number;
  origineAction?: string | null;
  origine?: string | null;
  action?: IAction | null;
  delegue?: IUser | null;
  processus?: IProcessusSMI | null;
}

export class AutreAction implements IAutreAction {
  constructor(
    public id?: number,
    public origineAction?: string | null,
    public origine?: string | null,
    public action?: IAction | null,
    public delegue?: IUser | null,
    public processus?: IProcessusSMI | null
  ) {}
}

export function getAutreActionIdentifier(autreAction: IAutreAction): number | undefined {
  return autreAction.id;
}
