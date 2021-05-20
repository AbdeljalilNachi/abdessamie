import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPolitiqueQSE } from '../politique-qse.model';

@Component({
  selector: 'jhi-politique-qse-detail',
  templateUrl: './politique-qse-detail.component.html',
})
export class PolitiqueQSEDetailComponent implements OnInit {
  politiqueQSE: IPolitiqueQSE | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ politiqueQSE }) => {
      this.politiqueQSE = politiqueQSE;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
