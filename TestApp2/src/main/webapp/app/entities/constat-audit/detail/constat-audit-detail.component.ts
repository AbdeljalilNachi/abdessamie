import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConstatAudit } from '../constat-audit.model';

@Component({
  selector: 'jhi-constat-audit-detail',
  templateUrl: './constat-audit-detail.component.html',
})
export class ConstatAuditDetailComponent implements OnInit {
  constatAudit: IConstatAudit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ constatAudit }) => {
      this.constatAudit = constatAudit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
