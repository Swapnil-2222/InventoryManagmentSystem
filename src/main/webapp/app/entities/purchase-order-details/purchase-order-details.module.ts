import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PurchaseOrderDetailsComponent } from './list/purchase-order-details.component';
import { PurchaseOrderDetailsDetailComponent } from './detail/purchase-order-details-detail.component';
import { PurchaseOrderDetailsUpdateComponent } from './update/purchase-order-details-update.component';
import { PurchaseOrderDetailsDeleteDialogComponent } from './delete/purchase-order-details-delete-dialog.component';
import { PurchaseOrderDetailsRoutingModule } from './route/purchase-order-details-routing.module';

@NgModule({
  imports: [SharedModule, PurchaseOrderDetailsRoutingModule],
  declarations: [
    PurchaseOrderDetailsComponent,
    PurchaseOrderDetailsDetailComponent,
    PurchaseOrderDetailsUpdateComponent,
    PurchaseOrderDetailsDeleteDialogComponent,
  ],
  entryComponents: [PurchaseOrderDetailsDeleteDialogComponent],
})
export class PurchaseOrderDetailsModule {}
