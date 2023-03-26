import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MinistryComponent } from '../list/ministry.component';
import { MinistryDetailComponent } from '../detail/ministry-detail.component';
import { MinistryUpdateComponent } from '../update/ministry-update.component';
import { MinistryRoutingResolveService } from './ministry-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const ministryRoute: Routes = [
  {
    path: '',
    component: MinistryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MinistryDetailComponent,
    resolve: {
      ministry: MinistryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MinistryUpdateComponent,
    resolve: {
      ministry: MinistryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MinistryUpdateComponent,
    resolve: {
      ministry: MinistryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ministryRoute)],
  exports: [RouterModule],
})
export class MinistryRoutingModule {}
