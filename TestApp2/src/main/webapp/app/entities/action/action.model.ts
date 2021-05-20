import * as dayjs from 'dayjs';
import { TypeAction } from 'app/entities/enumerations/type-action.model';
import { Statut } from 'app/entities/enumerations/statut.model';
import { Efficace } from 'app/entities/enumerations/efficace.model';

export interface IAction {
  id?: number;
  datePlanification?: dayjs.Dayjs | null;
  action?: string | null;
  type?: TypeAction | null;
  delai?: dayjs.Dayjs | null;
  avancement?: string | null;
  realisee?: boolean | null;
  critereResultat?: string | null;
  ressourcesNecessaires?: string | null;
  statut?: Statut | null;
  efficace?: Efficace | null;
}

export class Action implements IAction {
  constructor(
    public id?: number,
    public datePlanification?: dayjs.Dayjs | null,
    public action?: string | null,
    public type?: TypeAction | null,
    public delai?: dayjs.Dayjs | null,
    public avancement?: string | null,
    public realisee?: boolean | null,
    public critereResultat?: string | null,
    public ressourcesNecessaires?: string | null,
    public statut?: Statut | null,
    public efficace?: Efficace | null
  ) {
    this.realisee = this.realisee ?? false;
  }
}

export function getActionIdentifier(action: IAction): number | undefined {
  return action.id;
}
