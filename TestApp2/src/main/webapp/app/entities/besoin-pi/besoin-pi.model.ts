import * as dayjs from 'dayjs';
import { IProcessusSMI } from 'app/entities/processus-smi/processus-smi.model';

export interface IBesoinPI {
  id?: number;
  dateIdentification?: dayjs.Dayjs | null;
  piPertinentes?: string | null;
  pertinente?: boolean | null;
  priseEnCharge?: boolean | null;
  afficher?: boolean | null;
  processus?: IProcessusSMI | null;
}

export class BesoinPI implements IBesoinPI {
  constructor(
    public id?: number,
    public dateIdentification?: dayjs.Dayjs | null,
    public piPertinentes?: string | null,
    public pertinente?: boolean | null,
    public priseEnCharge?: boolean | null,
    public afficher?: boolean | null,
    public processus?: IProcessusSMI | null
  ) {
    this.pertinente = this.pertinente ?? false;
    this.priseEnCharge = this.priseEnCharge ?? false;
    this.afficher = this.afficher ?? false;
  }
}

export function getBesoinPIIdentifier(besoinPI: IBesoinPI): number | undefined {
  return besoinPI.id;
}
