import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IObjectif } from '../objectif.model';

@Component({
  selector: 'jhi-objectif-detail',
  templateUrl: './objectif-detail.component.html',
})
export class ObjectifDetailComponent implements OnInit {
  objectif: IObjectif | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ objectif }) => {
      this.objectif = objectif;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
