import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'profile',
        data: { pageTitle: 'churchserviceApp.profile.home.title' },
        loadChildren: () => import('./profile/profile.module').then(m => m.ProfileModule),
      },
      {
        path: 'address',
        data: { pageTitle: 'churchserviceApp.address.home.title' },
        loadChildren: () => import('./address/address.module').then(m => m.AddressModule),
      },
      {
        path: 'baptism-history',
        data: { pageTitle: 'churchserviceApp.baptismHistory.home.title' },
        loadChildren: () => import('./baptism-history/baptism-history.module').then(m => m.BaptismHistoryModule),
      },
      {
        path: 'league',
        data: { pageTitle: 'churchserviceApp.league.home.title' },
        loadChildren: () => import('./league/league.module').then(m => m.LeagueModule),
      },
      {
        path: 'ministry',
        data: { pageTitle: 'churchserviceApp.ministry.home.title' },
        loadChildren: () => import('./ministry/ministry.module').then(m => m.MinistryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
