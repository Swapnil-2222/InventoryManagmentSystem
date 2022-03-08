import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { INotification, Notification } from '../notification.model';
import { NotificationService } from '../service/notification.service';
import { ISecurityUser } from 'app/entities/security-user/security-user.model';
import { SecurityUserService } from 'app/entities/security-user/service/security-user.service';
import { IWareHouse } from 'app/entities/ware-house/ware-house.model';
import { WareHouseService } from 'app/entities/ware-house/service/ware-house.service';
import { NotificationType } from 'app/entities/enumerations/notification-type.model';

@Component({
  selector: 'jhi-notification-update',
  templateUrl: './notification-update.component.html',
})
export class NotificationUpdateComponent implements OnInit {
  isSaving = false;
  notificationTypeValues = Object.keys(NotificationType);

  securityUsersSharedCollection: ISecurityUser[] = [];
  wareHousesSharedCollection: IWareHouse[] = [];

  editForm = this.fb.group({
    id: [],
    massage: [null, [Validators.required]],
    notificationType: [],
    isActionRequired: [],
    lastModified: [null, [Validators.required]],
    lastModifiedBy: [null, [Validators.required]],
    ecurityUser: [],
    wareHouse: [],
  });

  constructor(
    protected notificationService: NotificationService,
    protected securityUserService: SecurityUserService,
    protected wareHouseService: WareHouseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notification }) => {
      if (notification.id === undefined) {
        const today = dayjs().startOf('day');
        notification.lastModified = today;
      }

      this.updateForm(notification);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notification = this.createFromForm();
    if (notification.id !== undefined) {
      this.subscribeToSaveResponse(this.notificationService.update(notification));
    } else {
      this.subscribeToSaveResponse(this.notificationService.create(notification));
    }
  }

  trackSecurityUserById(index: number, item: ISecurityUser): number {
    return item.id!;
  }

  trackWareHouseById(index: number, item: IWareHouse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotification>>): void {
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

  protected updateForm(notification: INotification): void {
    this.editForm.patchValue({
      id: notification.id,
      massage: notification.massage,
      notificationType: notification.notificationType,
      isActionRequired: notification.isActionRequired,
      lastModified: notification.lastModified ? notification.lastModified.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: notification.lastModifiedBy,
      ecurityUser: notification.ecurityUser,
      wareHouse: notification.wareHouse,
    });

    this.securityUsersSharedCollection = this.securityUserService.addSecurityUserToCollectionIfMissing(
      this.securityUsersSharedCollection,
      notification.ecurityUser
    );
    this.wareHousesSharedCollection = this.wareHouseService.addWareHouseToCollectionIfMissing(
      this.wareHousesSharedCollection,
      notification.wareHouse
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

  protected createFromForm(): INotification {
    return {
      ...new Notification(),
      id: this.editForm.get(['id'])!.value,
      massage: this.editForm.get(['massage'])!.value,
      notificationType: this.editForm.get(['notificationType'])!.value,
      isActionRequired: this.editForm.get(['isActionRequired'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value
        ? dayjs(this.editForm.get(['lastModified'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      ecurityUser: this.editForm.get(['ecurityUser'])!.value,
      wareHouse: this.editForm.get(['wareHouse'])!.value,
    };
  }
}
