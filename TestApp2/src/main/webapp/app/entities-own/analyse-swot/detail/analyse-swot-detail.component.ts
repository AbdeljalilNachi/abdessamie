import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnalyseSWOT } from '../analyse-swot.model';

@Component({
  selector: 'jhi-analyse-swot-detail',
  templateUrl: './analyse-swot-detail.component.html',
})
export class AnalyseSWOTDetailComponent implements OnInit {
  analyseSWOT: IAnalyseSWOT | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ analyseSWOT }) => {
      this.analyseSWOT = analyseSWOT;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
