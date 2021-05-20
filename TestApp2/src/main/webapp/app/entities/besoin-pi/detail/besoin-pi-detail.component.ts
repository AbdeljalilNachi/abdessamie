import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBesoinPI } from '../besoin-pi.model';

@Component({
  selector: 'jhi-besoin-pi-detail',
  templateUrl: './besoin-pi-detail.component.html',
})
export class BesoinPIDetailComponent implements OnInit {
  besoinPI: IBesoinPI | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ besoinPI }) => {
      this.besoinPI = besoinPI;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
