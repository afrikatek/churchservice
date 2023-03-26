import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MinistryComponent } from './list/ministry.component';
import { MinistryDetailComponent } from './detail/ministry-detail.component';
import { MinistryUpdateComponent } from './update/ministry-update.component';
import { MinistryDeleteDialogComponent } from './delete/ministry-delete-dialog.component';
import { MinistryRoutingModule } from './route/ministry-routing.module';

@NgModule({
  imports: [SharedModule, MinistryRoutingModule],
  declarations: [MinistryComponent, MinistryDetailComponent, MinistryUpdateComponent, MinistryDeleteDialogComponent],
})
export class MinistryModule {}
