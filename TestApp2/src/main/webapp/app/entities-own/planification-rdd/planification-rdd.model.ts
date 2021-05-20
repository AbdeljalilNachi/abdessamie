import * as dayjs from 'dayjs';
import { Standard } from 'app/entities-own/enumerations/standard.model';

export interface IPlanificationRDD {
  id?: number;
  nRdd?: number | null;
  date?: dayjs.Dayjs | null;
  realisee?: boolean | null;
  presentationContentType?: string | null;
  presentation?: string | null;
  standard?: Standard | null;
}

export class PlanificationRDD implements IPlanificationRDD {
  constructor(
    public id?: number,
    public nRdd?: number | null,
    public date?: dayjs.Dayjs | null,
    public realisee?: boolean | null,
    public presentationContentType?: string | null,
    public presentation?: string | null,
    public standard?: Standard | null
  ) {
    this.realisee = this.realisee ?? false;
  }
}

export function getPlanificationRDDIdentifier(planificationRDD: IPlanificationRDD): number | undefined {
  return planificationRDD.id;
}
