import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlanificationRDD } from '../planification-rdd.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-planification-rdd-detail',
  templateUrl: './planification-rdd-detail.component.html',
})
export class PlanificationRDDDetailComponent implements OnInit {
  planificationRDD: IPlanificationRDD | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planificationRDD }) => {
      this.planificationRDD = planificationRDD;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
