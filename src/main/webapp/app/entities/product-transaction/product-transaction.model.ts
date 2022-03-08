import dayjs from 'dayjs/esm';
import { ISecurityUser } from 'app/entities/security-user/security-user.model';
import { IWareHouse } from 'app/entities/ware-house/ware-house.model';

export interface IProductTransaction {
  id?: number;
  qtySold?: number | null;
  pricePerUnit?: number | null;
  lotNumber?: string | null;
  expirydate?: dayjs.Dayjs | null;
  totalAmount?: number | null;
  gstAmount?: number | null;
  description?: string | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  ecurityUser?: ISecurityUser | null;
  wareHouse?: IWareHouse | null;
}

export class ProductTransaction implements IProductTransaction {
  constructor(
    public id?: number,
    public qtySold?: number | null,
    public pricePerUnit?: number | null,
    public lotNumber?: string | null,
    public expirydate?: dayjs.Dayjs | null,
    public totalAmount?: number | null,
    public gstAmount?: number | null,
    public description?: string | null,
    public lastModified?: dayjs.Dayjs | null,
    public lastModifiedBy?: string | null,
    public ecurityUser?: ISecurityUser | null,
    public wareHouse?: IWareHouse | null
  ) {}
}

export function getProductTransactionIdentifier(productTransaction: IProductTransaction): number | undefined {
  return productTransaction.id;
}
