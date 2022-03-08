import dayjs from 'dayjs/esm';
import { ISecurityUser } from 'app/entities/security-user/security-user.model';
import { IWareHouse } from 'app/entities/ware-house/ware-house.model';
import { NotificationType } from 'app/entities/enumerations/notification-type.model';

export interface INotification {
  id?: number;
  massage?: string;
  notificationType?: NotificationType | null;
  isActionRequired?: boolean | null;
  lastModified?: dayjs.Dayjs;
  lastModifiedBy?: string;
  ecurityUser?: ISecurityUser | null;
  wareHouse?: IWareHouse | null;
}

export class Notification implements INotification {
  constructor(
    public id?: number,
    public massage?: string,
    public notificationType?: NotificationType | null,
    public isActionRequired?: boolean | null,
    public lastModified?: dayjs.Dayjs,
    public lastModifiedBy?: string,
    public ecurityUser?: ISecurityUser | null,
    public wareHouse?: IWareHouse | null
  ) {
    this.isActionRequired = this.isActionRequired ?? false;
  }
}

export function getNotificationIdentifier(notification: INotification): number | undefined {
  return notification.id;
}
