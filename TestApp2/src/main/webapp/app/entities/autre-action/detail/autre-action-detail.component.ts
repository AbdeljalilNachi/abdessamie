import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAutreAction } from '../autre-action.model';

@Component({
  selector: 'jhi-autre-action-detail',
  templateUrl: './autre-action-detail.component.html',
})
export class AutreActionDetailComponent implements OnInit {
  autreAction: IAutreAction | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ autreAction }) => {
      this.autreAction = autreAction;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
