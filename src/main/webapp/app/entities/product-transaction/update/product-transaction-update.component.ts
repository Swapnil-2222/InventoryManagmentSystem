import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProductTransaction, ProductTransaction } from '../product-transaction.model';
import { ProductTransactionService } from '../service/product-transaction.service';
import { ISecurityUser } from 'app/entities/security-user/security-user.model';
import { SecurityUserService } from 'app/entities/security-user/service/security-user.service';
import { IWareHouse } from 'app/entities/ware-house/ware-house.model';
import { WareHouseService } from 'app/entities/ware-house/service/ware-house.service';

@Component({
  selector: 'jhi-product-transaction-update',
  templateUrl: './product-transaction-update.component.html',
})
export class ProductTransactionUpdateComponent implements OnInit {
  isSaving = false;

  securityUsersSharedCollection: ISecurityUser[] = [];
  wareHousesSharedCollection: IWareHouse[] = [];

  editForm = this.fb.group({
    id: [],
    qtySold: [],
    pricePerUnit: [],
    lotNumber: [],
    expirydate: [],
    totalAmount: [],
    gstAmount: [],
    description: [],
    lastModified: [],
    lastModifiedBy: [],
    ecurityUser: [],
    wareHouse: [],
  });

  constructor(
    protected productTransactionService: ProductTransactionService,
    protected securityUserService: SecurityUserService,
    protected wareHouseService: WareHouseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productTransaction }) => {
      if (productTransaction.id === undefined) {
        const today = dayjs().startOf('day');
        productTransaction.expirydate = today;
        productTransaction.lastModified = today;
      }

      this.updateForm(productTransaction);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productTransaction = this.createFromForm();
    if (productTransaction.id !== undefined) {
      this.subscribeToSaveResponse(this.productTransactionService.update(productTransaction));
    } else {
      this.subscribeToSaveResponse(this.productTransactionService.create(productTransaction));
    }
  }

  trackSecurityUserById(index: number, item: ISecurityUser): number {
    return item.id!;
  }

  trackWareHouseById(index: number, item: IWareHouse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductTransaction>>): void {
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

  protected updateForm(productTransaction: IProductTransaction): void {
    this.editForm.patchValue({
      id: productTransaction.id,
      qtySold: productTransaction.qtySold,
      pricePerUnit: productTransaction.pricePerUnit,
      lotNumber: productTransaction.lotNumber,
      expirydate: productTransaction.expirydate ? productTransaction.expirydate.format(DATE_TIME_FORMAT) : null,
      totalAmount: productTransaction.totalAmount,
      gstAmount: productTransaction.gstAmount,
      description: productTransaction.description,
      lastModified: productTransaction.lastModified ? productTransaction.lastModified.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: productTransaction.lastModifiedBy,
      ecurityUser: productTransaction.ecurityUser,
      wareHouse: productTransaction.wareHouse,
    });

    this.securityUsersSharedCollection = this.securityUserService.addSecurityUserToCollectionIfMissing(
      this.securityUsersSharedCollection,
      productTransaction.ecurityUser
    );
    this.wareHousesSharedCollection = this.wareHouseService.addWareHouseToCollectionIfMissing(
      this.wareHousesSharedCollection,
      productTransaction.wareHouse
    );
  }

  protected loadRelationshipsOptions(): void {
    this.securityUserService
      .query()
      .pipe(map((res: HttpResponse<ISecurityUser[]>) => res.body ?? []))
      .pipe(
        map((securityUsers: ISecurityUser[]) =>
          this.securityUserService.addSecurityUserToCollectionIfMissing(securityUsers, this.editForm.get('ecurityUser')!.value)
        )
      )
      .subscribe((securityUsers: ISecurityUser[]) => (this.securityUsersSharedCollection = securityUsers));

    this.wareHouseService
      .query()
      .pipe(map((res: HttpResponse<IWareHouse[]>) => res.body ?? []))
      .pipe(
        map((wareHouses: IWareHouse[]) =>
          this.wareHouseService.addWareHouseToCollectionIfMissing(wareHouses, this.editForm.get('wareHouse')!.value)
        )
      )
      .subscribe((wareHouses: IWareHouse[]) => (this.wareHousesSharedCollection = wareHouses));
  }

  protected createFromForm(): IProductTransaction {
    return {
      ...new ProductTransaction(),
      id: this.editForm.get(['id'])!.value,
      qtySold: this.editForm.get(['qtySold'])!.value,
      pricePerUnit: this.editForm.get(['pricePerUnit'])!.value,
      lotNumber: this.editForm.get(['lotNumber'])!.value,
      expirydate: this.editForm.get(['expirydate'])!.value ? dayjs(this.editForm.get(['expirydate'])!.value, DATE_TIME_FORMAT) : undefined,
      totalAmount: this.editForm.get(['totalAmount'])!.value,
      gstAmount: this.editForm.get(['gstAmount'])!.value,
      description: this.editForm.get(['description'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value
        ? dayjs(this.editForm.get(['lastModified'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      ecurityUser: this.editForm.get(['ecurityUser'])!.value,
      wareHouse: this.editForm.get(['wareHouse'])!.value,
    };
  }
}
