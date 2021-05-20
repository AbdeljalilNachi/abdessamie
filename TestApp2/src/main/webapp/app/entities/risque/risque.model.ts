import * as dayjs from 'dayjs';
import { IAction } from 'app/entities/action/action.model';
import { IUser } from 'app/entities/user/user.model';
import { IProcessusSMI } from 'app/entities/processus-smi/processus-smi.model';
import { TypeRisque } from 'app/entities/enumerations/type-risque.model';
import { EnumFive } from 'app/entities/enumerations/enum-five.model';
import { Traitement } from 'app/entities/enumerations/traitement.model';

export interface IRisque {
  id?: number;
  dateIdentification?: dayjs.Dayjs | null;
  description?: string | null;
  causePotentielle?: string | null;
  effetPotentiel?: string | null;
  type?: TypeRisque | null;
  gravite?: EnumFive | null;
  probabilite?: EnumFive | null;
  criticite?: number | null;
  traitement?: Traitement | null;
  commentaire?: string | null;
  origine?: string | null;
  action?: IAction | null;
  delegue?: IUser | null;
  processus?: IProcessusSMI | null;
}

export class Risque implements IRisque {
  constructor(
    public id?: number,
    public dateIdentification?: dayjs.Dayjs | null,
    public description?: string | null,
    public causePotentielle?: string | null,
    public effetPotentiel?: string | null,
    public type?: TypeRisque | null,
    public gravite?: EnumFive | null,
    public probabilite?: EnumFive | null,
    public criticite?: number | null,
    public traitement?: Traitement | null,
    public commentaire?: string | null,
    public origine?: string | null,
    public action?: IAction | null,
    public delegue?: IUser | null,
    public processus?: IProcessusSMI | null
  ) {}
}

export function getRisqueIdentifier(risque: IRisque): number | undefined {
  return risque.id;
}
