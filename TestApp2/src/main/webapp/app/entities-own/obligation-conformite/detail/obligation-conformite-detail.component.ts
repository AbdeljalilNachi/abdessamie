import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IObligationConformite } from '../obligation-conformite.model';

@Component({
  selector: 'jhi-obligation-conformite-detail',
  templateUrl: './obligation-conformite-detail.component.html',
})
export class ObligationConformiteDetailComponent implements OnInit {
  obligationConformite: IObligationConformite | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ obligationConformite }) => {
      this.obligationConformite = obligationConformite;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
