import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PurchaseOrderDetailsComponent } from '../list/purchase-order-details.component';
import { PurchaseOrderDetailsDetailComponent } from '../detail/purchase-order-details-detail.component';
import { PurchaseOrderDetailsUpdateComponent } from '../update/purchase-order-details-update.component';
import { PurchaseOrderDetailsRoutingResolveService } from './purchase-order-details-routing-resolve.service';

const purchaseOrderDetailsRoute: Routes = [
  {
    path: '',
    component: PurchaseOrderDetailsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PurchaseOrderDetailsDetailComponent,
    resolve: {
      purchaseOrderDetails: PurchaseOrderDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PurchaseOrderDetailsUpdateComponent,
    resolve: {
      purchaseOrderDetails: PurchaseOrderDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PurchaseOrderDetailsUpdateComponent,
    resolve: {
      purchaseOrderDetails: PurchaseOrderDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(purchaseOrderDetailsRoute)],
  exports: [RouterModule],
})
export class PurchaseOrderDetailsRoutingModule {}
