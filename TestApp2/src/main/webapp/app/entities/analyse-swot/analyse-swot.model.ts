import * as dayjs from 'dayjs';
import { IProcessusSMI } from 'app/entities/processus-smi/processus-smi.model';
import { TypeAnalyseSWOT } from 'app/entities/enumerations/type-analyse-swot.model';

export interface IAnalyseSWOT {
  id?: number;
  dateIdentification?: dayjs.Dayjs | null;
  description?: string | null;
  pilote?: string | null;
  type?: TypeAnalyseSWOT | null;
  bu?: string | null;
  commentaire?: string | null;
  afficher?: boolean | null;
  processus?: IProcessusSMI | null;
}

export class AnalyseSWOT implements IAnalyseSWOT {
  constructor(
    public id?: number,
    public dateIdentification?: dayjs.Dayjs | null,
    public description?: string | null,
    public pilote?: string | null,
    public type?: TypeAnalyseSWOT | null,
    public bu?: string | null,
    public commentaire?: string | null,
    public afficher?: boolean | null,
    public processus?: IProcessusSMI | null
  ) {
    this.afficher = this.afficher ?? false;
  }
}

export function getAnalyseSWOTIdentifier(analyseSWOT: IAnalyseSWOT): number | undefined {
  return analyseSWOT.id;
}
