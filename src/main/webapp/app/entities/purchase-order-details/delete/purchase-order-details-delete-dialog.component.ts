import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPurchaseOrderDetails } from '../purchase-order-details.model';
import { PurchaseOrderDetailsService } from '../service/purchase-order-details.service';

@Component({
  templateUrl: './purchase-order-details-delete-dialog.component.html',
})
export class PurchaseOrderDetailsDeleteDialogComponent {
  purchaseOrderDetails?: IPurchaseOrderDetails;

  constructor(protected purchaseOrderDetailsService: PurchaseOrderDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.purchaseOrderDetailsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
