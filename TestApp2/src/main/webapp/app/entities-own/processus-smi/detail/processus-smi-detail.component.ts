import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProcessusSMI } from '../processus-smi.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-processus-smi-detail',
  templateUrl: './processus-smi-detail.component.html',
})
export class ProcessusSMIDetailComponent implements OnInit {
  processusSMI: IProcessusSMI | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ processusSMI }) => {
      this.processusSMI = processusSMI;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
