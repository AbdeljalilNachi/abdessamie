import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INonConformite } from '../non-conformite.model';

@Component({
  selector: 'jhi-non-conformite-detail',
  templateUrl: './non-conformite-detail.component.html',
})
export class NonConformiteDetailComponent implements OnInit {
  nonConformite: INonConformite | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nonConformite }) => {
      this.nonConformite = nonConformite;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
