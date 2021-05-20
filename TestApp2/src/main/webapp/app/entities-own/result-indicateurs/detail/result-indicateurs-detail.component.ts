import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResultIndicateurs } from '../result-indicateurs.model';

@Component({
  selector: 'jhi-result-indicateurs-detail',
  templateUrl: './result-indicateurs-detail.component.html',
})
export class ResultIndicateursDetailComponent implements OnInit {
  resultIndicateurs: IResultIndicateurs | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resultIndicateurs }) => {
      this.resultIndicateurs = resultIndicateurs;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
