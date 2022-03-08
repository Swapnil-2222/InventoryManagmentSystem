import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPurchaseOrder, PurchaseOrder } from '../purchase-order.model';
import { PurchaseOrderService } from '../service/purchase-order.service';
import { ISecurityUser } from 'app/entities/security-user/security-user.model';
import { SecurityUserService } from 'app/entities/security-user/service/security-user.service';
import { OrderType } from 'app/entities/enumerations/order-type.model';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-purchase-order-update',
  templateUrl: './purchase-order-update.component.html',
})
export class PurchaseOrderUpdateComponent implements OnInit {
  isSaving = false;
  orderTypeValues = Object.keys(OrderType);
  statusValues = Object.keys(Status);

  securityUsersSharedCollection: ISecurityUser[] = [];

  editForm = this.fb.group({
    id: [],
    totalPOAmount: [],
    totalGSTAmount: [],
    expectedDeliveryDate: [],
    poDate: [],
    orderType: [],
    orderStatus: [],
    clientName: [],
    clientMobile: [],
    clientEmail: [],
    termsAndCondition: [],
    notes: [],
    lastModified: [null, [Validators.required]],
    lastModifiedBy: [null, [Validators.required]],
    freeField1: [],
    freeField2: [],
    securityUser: [],
  });

  constructor(
    protected purchaseOrderService: PurchaseOrderService,
    protected securityUserService: SecurityUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseOrder }) => {
      if (purchaseOrder.id === undefined) {
        const today = dayjs().startOf('day');
        purchaseOrder.expectedDeliveryDate = today;
        purchaseOrder.poDate = today;
        purchaseOrder.lastModified = today;
      }

      this.updateForm(purchaseOrder);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const purchaseOrder = this.createFromForm();
    if (purchaseOrder.id !== undefined) {
      this.subscribeToSaveResponse(this.purchaseOrderService.update(purchaseOrder));
    } else {
      this.subscribeToSaveResponse(this.purchaseOrderService.create(purchaseOrder));
    }
  }

  trackSecurityUserById(index: number, item: ISecurityUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseOrder>>): void {
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

  protected updateForm(purchaseOrder: IPurchaseOrder): void {
    this.editForm.patchValue({
      id: purchaseOrder.id,
      totalPOAmount: purchaseOrder.totalPOAmount,
      totalGSTAmount: purchaseOrder.totalGSTAmount,
      expectedDeliveryDate: purchaseOrder.expectedDeliveryDate ? purchaseOrder.expectedDeliveryDate.format(DATE_TIME_FORMAT) : null,
      poDate: purchaseOrder.poDate ? purchaseOrder.poDate.format(DATE_TIME_FORMAT) : null,
      orderType: purchaseOrder.orderType,
      orderStatus: purchaseOrder.orderStatus,
      clientName: purchaseOrder.clientName,
      clientMobile: purchaseOrder.clientMobile,
      clientEmail: purchaseOrder.clientEmail,
      termsAndCondition: purchaseOrder.termsAndCondition,
      notes: purchaseOrder.notes,
      lastModified: purchaseOrder.lastModified ? purchaseOrder.lastModified.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: purchaseOrder.lastModifiedBy,
      freeField1: purchaseOrder.freeField1,
      freeField2: purchaseOrder.freeField2,
      securityUser: purchaseOrder.securityUser,
    });

    this.securityUsersSharedCollection = this.securityUserService.addSecurityUserToCollectionIfMissing(
      this.securityUsersSharedCollection,
      purchaseOrder.securityUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.securityUserService
      .query()
      .pipe(map((res: HttpResponse<ISecurityUser[]>) => res.body ?? []))
      .pipe(
        map((securityUsers: ISecurityUser[]) =>
          this.securityUserService.addSecurityUserToCollectionIfMissing(securityUsers, this.editForm.get('securityUser')!.value)
        )
      )
      .subscribe((securityUsers: ISecurityUser[]) => (this.securityUsersSharedCollection = securityUsers));
  }

  protected createFromForm(): IPurchaseOrder {
    return {
      ...new PurchaseOrder(),
      id: this.editForm.get(['id'])!.value,
      totalPOAmount: this.editForm.get(['totalPOAmount'])!.value,
      totalGSTAmount: this.editForm.get(['totalGSTAmount'])!.value,
      expectedDeliveryDate: this.editForm.get(['expectedDeliveryDate'])!.value
        ? dayjs(this.editForm.get(['expectedDeliveryDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      poDate: this.editForm.get(['poDate'])!.value ? dayjs(this.editForm.get(['poDate'])!.value, DATE_TIME_FORMAT) : undefined,
      orderType: this.editForm.get(['orderType'])!.value,
      orderStatus: this.editForm.get(['orderStatus'])!.value,
      clientName: this.editForm.get(['clientName'])!.value,
      clientMobile: this.editForm.get(['clientMobile'])!.value,
      clientEmail: this.editForm.get(['clientEmail'])!.value,
      termsAndCondition: this.editForm.get(['termsAndCondition'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value
        ? dayjs(this.editForm.get(['lastModified'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      freeField1: this.editForm.get(['freeField1'])!.value,
      freeField2: this.editForm.get(['freeField2'])!.value,
      securityUser: this.editForm.get(['securityUser'])!.value,
    };
  }
}
