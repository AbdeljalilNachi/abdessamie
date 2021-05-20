import { IResultatIndicateur } from 'app/entities/resultat-indicateur/resultat-indicateur.model';
import { IIndicateurSMI } from 'app/entities/indicateur-smi/indicateur-smi.model';

export interface IResultIndicateurs {
  id?: number;
  annee?: number | null;
  observation?: string | null;
  resultats?: IResultatIndicateur[] | null;
  indicateur?: IIndicateurSMI | null;
}

export class ResultIndicateurs implements IResultIndicateurs {
  constructor(
    public id?: number,
    public annee?: number | null,
    public observation?: string | null,
    public resultats?: IResultatIndicateur[] | null,
    public indicateur?: IIndicateurSMI | null
  ) {}
}

export function getResultIndicateursIdentifier(resultIndicateurs: IResultIndicateurs): number | undefined {
  return resultIndicateurs.id;
}
