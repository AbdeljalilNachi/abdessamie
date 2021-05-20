import * as dayjs from 'dayjs';
import { IUser } from 'app/entities-own/user/user.model';
import { IProcessusSMI } from 'app/entities-own/processus-smi/processus-smi.model';
import { TypeDocument } from 'app/entities-own/enumerations/type-document.model';

export interface IDocument {
  id?: number;
  date?: dayjs.Dayjs | null;
  intitule?: string | null;
  code?: string | null;
  version?: number | null;
  type?: TypeDocument | null;
  pieceJointeContentType?: string | null;
  pieceJointe?: string | null;
  enApplication?: boolean | null;
  appouve?: boolean | null;
  acces?: IUser | null;
  processus?: IProcessusSMI | null;
}

export class Document implements IDocument {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public intitule?: string | null,
    public code?: string | null,
    public version?: number | null,
    public type?: TypeDocument | null,
    public pieceJointeContentType?: string | null,
    public pieceJointe?: string | null,
    public enApplication?: boolean | null,
    public appouve?: boolean | null,
    public acces?: IUser | null,
    public processus?: IProcessusSMI | null
  ) {
    this.enApplication = this.enApplication ?? false;
    this.appouve = this.appouve ?? false;
  }
}

export function getDocumentIdentifier(document: IDocument): number | undefined {
  return document.id;
}
