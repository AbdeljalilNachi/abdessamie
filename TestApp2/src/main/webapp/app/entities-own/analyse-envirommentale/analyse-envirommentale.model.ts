import * as dayjs from 'dayjs';
import { IAction } from 'app/entities-own/action/action.model';
import { IUser } from 'app/entities-own/user/user.model';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { Situation } from 'app/entities-own/enumerations/situation.model';
import { EnumFive } from 'app/entities-own/enumerations/enum-five.model';

export interface IAnalyseEnvirommentale {
  id?: number;
  date?: dayjs.Dayjs | null;
  businessUnit?: string | null;
  activite?: string | null;
  aspectEnvironnemental?: string | null;
  impactEnvironnemental?: string | null;
  competencesRequises?: string | null;
  situation?: Situation | null;
  frequence?: EnumFive | null;
  sensibiliteMilieu?: EnumFive | null;
  coefficientMaitrise?: EnumFive | null;
  gravite?: EnumFive | null;
  criticite?: number | null;
  maitriseExistante?: string | null;
  origine?: string | null;
  action?: IAction | null;
  delegue?: IUser | null;
  processus?: IProcessusSMI | null;
}

export class AnalyseEnvirommentale implements IAnalyseEnvirommentale {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public businessUnit?: string | null,
    public activite?: string | null,
    public aspectEnvironnemental?: string | null,
    public impactEnvironnemental?: string | null,
    public competencesRequises?: string | null,
    public situation?: Situation | null,
    public frequence?: EnumFive | null,
    public sensibiliteMilieu?: EnumFive | null,
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

export function getAnalyseEnvirommentaleIdentifier(analyseEnvirommentale: IAnalyseEnvirommentale): number | undefined {
  return analyseEnvirommentale.id;
}
