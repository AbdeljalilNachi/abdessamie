import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResultatIndicateur } from '../resultat-indicateur.model';

@Component({
  selector: 'jhi-resultat-indicateur-detail',
  templateUrl: './resultat-indicateur-detail.component.html',
})
export class ResultatIndicateurDetailComponent implements OnInit {
  resultatIndicateur: IResultatIndicateur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resultatIndicateur }) => {
      this.resultatIndicateur = resultatIndicateur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
