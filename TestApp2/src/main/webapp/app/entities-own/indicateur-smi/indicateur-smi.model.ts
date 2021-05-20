import * as dayjs from 'dayjs';
import { IUser } from 'app/entities-own/user/user.model';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';

export interface IIndicateurSMI {
  id?: number;
  dateIdentification?: dayjs.Dayjs | null;
  indicateur?: string | null;
  formuleCalcul?: string | null;
  cible?: number | null;
  seuilTolerance?: number | null;
  unite?: string | null;
  periodicite?: string | null;
  observations?: string | null;
  vigueur?: boolean | null;
  responsableDeCalcul?: IUser | null;
  processus?: IProcessusSMI | null;
}

export class IndicateurSMI implements IIndicateurSMI {
  constructor(
    public id?: number,
    public dateIdentification?: dayjs.Dayjs | null,
    public indicateur?: string | null,
    public formuleCalcul?: string | null,
    public cible?: number | null,
    public seuilTolerance?: number | null,
    public unite?: string | null,
    public periodicite?: string | null,
    public observations?: string | null,
    public vigueur?: boolean | null,
    public responsableDeCalcul?: IUser | null,
    public processus?: IProcessusSMI | null
  ) {
    this.vigueur = this.vigueur ?? false;
  }
}

export function getIndicateurSMIIdentifier(indicateurSMI: IIndicateurSMI): number | undefined {
  return indicateurSMI.id;
}
