import dayjs from 'dayjs/esm';
import { IProductInventory } from 'app/entities/product-inventory/product-inventory.model';

export interface IWareHouse {
  id?: number;
  wareHouseName?: string | null;
  address?: string | null;
  pincode?: number | null;
  city?: string | null;
  state?: string | null;
  country?: string | null;
  gstDetails?: string | null;
  managerName?: string | null;
  managerEmail?: string | null;
  managerContact?: string | null;
  contact?: string | null;
  isDeleted?: boolean | null;
  isActive?: boolean | null;
  lastModified?: dayjs.Dayjs;
  lastModifiedBy?: string;
  productInventories?: IProductInventory[] | null;
}

export class WareHouse implements IWareHouse {
  constructor(
    public id?: number,
    public wareHouseName?: string | null,
    public address?: string | null,
    public pincode?: number | null,
    public city?: string | null,
    public state?: string | null,
    public country?: string | null,
    public gstDetails?: string | null,
    public managerName?: string | null,
    public managerEmail?: string | null,
    public managerContact?: string | null,
    public contact?: string | null,
    public isDeleted?: boolean | null,
    public isActive?: boolean | null,
    public lastModified?: dayjs.Dayjs,
    public lastModifiedBy?: string,
    public productInventories?: IProductInventory[] | null
  ) {
    this.isDeleted = this.isDeleted ?? false;
    this.isActive = this.isActive ?? false;
  }
}

export function getWareHouseIdentifier(wareHouse: IWareHouse): number | undefined {
  return wareHouse.id;
}
