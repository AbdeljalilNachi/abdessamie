import * as dayjs from 'dayjs';
import { IProcessusSMI } from 'app/entities/processus-smi/processus-smi.model';
import { IIndicateurSMI } from 'app/entities/indicateur-smi/indicateur-smi.model';

export interface IPolitiqueQSE {
  id?: number;
  date?: dayjs.Dayjs | null;
  axePolitiqueQSE?: string | null;
  objectifQSE?: string | null;
  vigueur?: boolean | null;
  processus?: IProcessusSMI | null;
  indicateur?: IIndicateurSMI | null;
}

export class PolitiqueQSE implements IPolitiqueQSE {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public axePolitiqueQSE?: string | null,
    public objectifQSE?: string | null,
    public vigueur?: boolean | null,
    public processus?: IProcessusSMI | null,
    public indicateur?: IIndicateurSMI | null
  ) {
    this.vigueur = this.vigueur ?? false;
  }
}

export function getPolitiqueQSEIdentifier(politiqueQSE: IPolitiqueQSE): number | undefined {
  return politiqueQSE.id;
}
