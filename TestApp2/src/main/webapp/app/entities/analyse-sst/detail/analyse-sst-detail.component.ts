import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnalyseSST } from '../analyse-sst.model';

@Component({
  selector: 'jhi-analyse-sst-detail',
  templateUrl: './analyse-sst-detail.component.html',
})
export class AnalyseSSTDetailComponent implements OnInit {
  analyseSST: IAnalyseSST | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ analyseSST }) => {
      this.analyseSST = analyseSST;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
