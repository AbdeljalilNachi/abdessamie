import { IResultIndicateurs } from 'app/entities-own/result-indicateurs/result-indicateurs.model';
import { Mois } from 'app/entities-own/enumerations/mois.model';

export interface IResultatIndicateur {
  id?: number;
  mois?: Mois | null;
  cible?: number | null;
  resultat?: number | null;
  resultIndicateurs?: IResultIndicateurs | null;
}

export class ResultatIndicateur implements IResultatIndicateur {
  constructor(
    public id?: number,
    public mois?: Mois | null,
    public cible?: number | null,
    public resultat?: number | null,
    public resultIndicateurs?: IResultIndicateurs | null
  ) {}
}

export function getResultatIndicateurIdentifier(resultatIndicateur: IResultatIndicateur): number | undefined {
  return resultatIndicateur.id;
}
