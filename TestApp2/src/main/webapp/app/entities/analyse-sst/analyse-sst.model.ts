import * as dayjs from 'dayjs';
import { IAction } from 'app/entities/action/action.model';
import { IUser } from 'app/entities/user/user.model';
import { IProcessusSMI } from 'app/entities/processus-smi/processus-smi.model';
import { Situation } from 'app/entities/enumerations/situation.model';
import { EnumFive } from 'app/entities/enumerations/enum-five.model';

export interface IAnalyseSST {
  id?: number;
  date?: dayjs.Dayjs | null;
  buisnessUnit?: string | null;
  uniteTravail?: string | null;
  danger?: string | null;
  risque?: string | null;
  competence?: string | null;
  situation?: Situation | null;
  frequence?: EnumFive | null;
  dureeExposition?: EnumFive | null;
  coefficientMaitrise?: EnumFive | null;
  gravite?: EnumFive | null;
  criticite?: number | null;
  maitriseExistante?: string | null;
  origine?: string | null;
  action?: IAction | null;
  delegue?: IUser | null;
  processus?: IProcessusSMI | null;
}

export class AnalyseSST implements IAnalyseSST {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public buisnessUnit?: string | null,
    public uniteTravail?: string | null,
    public danger?: string | null,
    public risque?: string | null,
    public competence?: string | null,
    public situation?: Situation | null,
    public frequence?: EnumFive | null,
    public dureeExposition?: EnumFive | null,
    public coefficientMaitrise?: EnumFive | null,
    public gravite?: EnumFive | null,
    public criticite?: number | null,
    public maitriseExistante?: string | null,
    public origine?: string | null,
    public action?: IAction | null,
    public delegue?: IUser | null,
    public processus?: IProcessusSMI | null
  ) {}
}

export function getAnalyseSSTIdentifier(analyseSST: IAnalyseSST): number | undefined {
  return analyseSST.id;
}
