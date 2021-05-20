import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnalyseEnvirommentale } from '../analyse-envirommentale.model';

@Component({
  selector: 'jhi-analyse-envirommentale-detail',
  templateUrl: './analyse-envirommentale-detail.component.html',
})
export class AnalyseEnvirommentaleDetailComponent implements OnInit {
  analyseEnvirommentale: IAnalyseEnvirommentale | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ analyseEnvirommentale }) => {
      this.analyseEnvirommentale = analyseEnvirommentale;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
