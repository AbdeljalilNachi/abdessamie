import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIndicateurSMI } from '../indicateur-smi.model';

@Component({
  selector: 'jhi-indicateur-smi-detail',
  templateUrl: './indicateur-smi-detail.component.html',
})
export class IndicateurSMIDetailComponent implements OnInit {
  indicateurSMI: IIndicateurSMI | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ indicateurSMI }) => {
      this.indicateurSMI = indicateurSMI;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
