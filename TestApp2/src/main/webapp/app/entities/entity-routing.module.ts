import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'action',
        data: { pageTitle: 'newbetterflyApp.action.home.title' },
        loadChildren: () => import('./action/action.module').then(m => m.ActionModule),
      },
      {
        path: 'analyse-envirommentale',
        data: { pageTitle: 'newbetterflyApp.analyseEnvirommentale.home.title' },
        loadChildren: () => import('./analyse-envirommentale/analyse-envirommentale.module').then(m => m.AnalyseEnvirommentaleModule),
      },
      {
        path: 'analyse-sst',
        data: { pageTitle: 'newbetterflyApp.analyseSST.home.title' },
        loadChildren: () => import('./analyse-sst/analyse-sst.module').then(m => m.AnalyseSSTModule),
      },
      {
        path: 'analyse-swot',
        data: { pageTitle: 'newbetterflyApp.analyseSWOT.home.title' },
        loadChildren: () => import('./analyse-swot/analyse-swot.module').then(m => m.AnalyseSWOTModule),
      },
      {
        path: 'audit',
        data: { pageTitle: 'newbetterflyApp.audit.home.title' },
        loadChildren: () => import('./audit/audit.module').then(m => m.AuditModule),
      },
      {
        path: 'autre-action',
        data: { pageTitle: 'newbetterflyApp.autreAction.home.title' },
        loadChildren: () => import('./autre-action/autre-action.module').then(m => m.AutreActionModule),
      },
      {
        path: 'besoin-pi',
        data: { pageTitle: 'newbetterflyApp.besoinPI.home.title' },
        loadChildren: () => import('./besoin-pi/besoin-pi.module').then(m => m.BesoinPIModule),
      },
      {
        path: 'constat-audit',
        data: { pageTitle: 'newbetterflyApp.constatAudit.home.title' },
        loadChildren: () => import('./constat-audit/constat-audit.module').then(m => m.ConstatAuditModule),
      },
      {
        path: 'document',
        data: { pageTitle: 'newbetterflyApp.document.home.title' },
        loadChildren: () => import('./document/document.module').then(m => m.DocumentModule),
      },
      {
        path: 'indicateur-smi',
        data: { pageTitle: 'newbetterflyApp.indicateurSMI.home.title' },
        loadChildren: () => import('./indicateur-smi/indicateur-smi.module').then(m => m.IndicateurSMIModule),
      },
      {
        path: 'non-conformite',
        data: { pageTitle: 'newbetterflyApp.nonConformite.home.title' },
        loadChildren: () => import('./non-conformite/non-conformite.module').then(m => m.NonConformiteModule),
      },
      {
        path: 'objectif',
        data: { pageTitle: 'newbetterflyApp.objectif.home.title' },
        loadChildren: () => import('./objectif/objectif.module').then(m => m.ObjectifModule),
      },
      {
        path: 'obligation-conformite',
        data: { pageTitle: 'newbetterflyApp.obligationConformite.home.title' },
        loadChildren: () => import('./obligation-conformite/obligation-conformite.module').then(m => m.ObligationConformiteModule),
      },
      {
        path: 'planification-rdd',
        data: { pageTitle: 'newbetterflyApp.planificationRDD.home.title' },
        loadChildren: () => import('./planification-rdd/planification-rdd.module').then(m => m.PlanificationRDDModule),
      },
      {
        path: 'politique-qse',
        data: { pageTitle: 'newbetterflyApp.politiqueQSE.home.title' },
        loadChildren: () => import('./politique-qse/politique-qse.module').then(m => m.PolitiqueQSEModule),
      },
      {
        path: 'processus-smi',
        data: { pageTitle: 'newbetterflyApp.processusSMI.home.title' },
        loadChildren: () => import('./processus-smi/processus-smi.module').then(m => m.ProcessusSMIModule),
      },
      {
        path: 'reclamation',
        data: { pageTitle: 'newbetterflyApp.reclamation.home.title' },
        loadChildren: () => import('./reclamation/reclamation.module').then(m => m.ReclamationModule),
      },
      {
        path: 'resultat-indicateur',
        data: { pageTitle: 'newbetterflyApp.resultatIndicateur.home.title' },
        loadChildren: () => import('./resultat-indicateur/resultat-indicateur.module').then(m => m.ResultatIndicateurModule),
      },
      {
        path: 'result-indicateurs',
        data: { pageTitle: 'newbetterflyApp.resultIndicateurs.home.title' },
        loadChildren: () => import('./result-indicateurs/result-indicateurs.module').then(m => m.ResultIndicateursModule),
      },
      {
        path: 'risque',
        data: { pageTitle: 'newbetterflyApp.risque.home.title' },
        loadChildren: () => import('./risque/risque.module').then(m => m.RisqueModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
