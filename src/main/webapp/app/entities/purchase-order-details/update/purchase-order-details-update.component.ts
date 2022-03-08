import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPurchaseOrderDetails, PurchaseOrderDetails } from '../purchase-order-details.model';
import { PurchaseOrderDetailsService } from '../service/purchase-order-details.service';
import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/purchase-order/service/purchase-order.service';

@Component({
  selector: 'jhi-purchase-order-details-update',
  templateUrl: './purchase-order-details-update.component.html',
})
export class PurchaseOrderDetailsUpdateComponent implements OnInit {
  isSaving = false;

  purchaseOrdersSharedCollection: IPurchaseOrder[] = [];

  editForm = this.fb.group({
    id: [],
    qtyordered: [],
    gstTaxPercentage: [],
    pricePerUnit: [],
    totalPrice: [],
    discount: [],
    lastModified: [null, [Validators.required]],
    lastModifiedBy: [null, [Validators.required]],
    freeField1: [],
    freeField2: [],
    purchaseOrder: [],
  });

  constructor(
    protected purchaseOrderDetailsService: PurchaseOrderDetailsService,
    protected purchaseOrderService: PurchaseOrderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseOrderDetails }) => {
      if (purchaseOrderDetails.id === undefined) {
        const today = dayjs().startOf('day');
        purchaseOrderDetails.lastModified = today;
      }

      this.updateForm(purchaseOrderDetails);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const purchaseOrderDetails = this.createFromForm();
    if (purchaseOrderDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.purchaseOrderDetailsService.update(purchaseOrderDetails));
    } else {
      this.subscribeToSaveResponse(this.purchaseOrderDetailsService.create(purchaseOrderDetails));
    }
  }

  trackPurchaseOrderById(index: number, item: IPurchaseOrder): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseOrderDetails>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(purchaseOrderDetails: IPurchaseOrderDetails): void {
    this.editForm.patchValue({
      id: purchaseOrderDetails.id,
      qtyordered: purchaseOrderDetails.qtyordered,
      gstTaxPercentage: purchaseOrderDetails.gstTaxPercentage,
      pricePerUnit: purchaseOrderDetails.pricePerUnit,
      totalPrice: purchaseOrderDetails.totalPrice,
      discount: purchaseOrderDetails.discount,
      lastModified: purchaseOrderDetails.lastModified ? purchaseOrderDetails.lastModified.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: purchaseOrderDetails.lastModifiedBy,
      freeField1: purchaseOrderDetails.freeField1,
      freeField2: purchaseOrderDetails.freeField2,
      purchaseOrder: purchaseOrderDetails.purchaseOrder,
    });

    this.purchaseOrdersSharedCollection = this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing(
      this.purchaseOrdersSharedCollection,
      purchaseOrderDetails.purchaseOrder
    );
  }

  protected loadRelationshipsOptions(): void {
    this.purchaseOrderService
      .query()
      .pipe(map((res: HttpResponse<IPurchaseOrder[]>) => res.body ?? []))
      .pipe(
        map((purchaseOrders: IPurchaseOrder[]) =>
          this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing(purchaseOrders, this.editForm.get('purchaseOrder')!.value)
        )
      )
      .subscribe((purchaseOrders: IPurchaseOrder[]) => (this.purchaseOrdersSharedCollection = purchaseOrders));
  }

  protected createFromForm(): IPurchaseOrderDetails {
    return {
      ...new PurchaseOrderDetails(),
      id: this.editForm.get(['id'])!.value,
      qtyordered: this.editForm.get(['qtyordered'])!.value,
      gstTaxPercentage: this.editForm.get(['gstTaxPercentage'])!.value,
      pricePerUnit: this.editForm.get(['pricePerUnit'])!.value,
      totalPrice: this.editForm.get(['totalPrice'])!.value,
      discount: this.editForm.get(['discount'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value
        ? dayjs(this.editForm.get(['lastModified'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      freeField1: this.editForm.get(['freeField1'])!.value,
      freeField2: this.editForm.get(['freeField2'])!.value,
      purchaseOrder: this.editForm.get(['purchaseOrder'])!.value,
    };
  }
}
