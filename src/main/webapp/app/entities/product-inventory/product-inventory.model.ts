import dayjs from 'dayjs/esm';
import { IConsumptionDetails } from 'app/entities/consumption-details/consumption-details.model';
import { IProduct } from 'app/entities/product/product.model';
import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';
import { IProductTransaction } from 'app/entities/product-transaction/product-transaction.model';
import { IWareHouse } from 'app/entities/ware-house/ware-house.model';
import { ISecurityUser } from 'app/entities/security-user/security-user.model';

export interface IProductInventory {
  id?: number;
  inwardOutwardDate?: dayjs.Dayjs | null;
  inwardQty?: string | null;
  outwardQty?: string | null;
  totalQuanity?: string | null;
  pricePerUnit?: number | null;
  lotNo?: string | null;
  expiryDate?: dayjs.Dayjs | null;
  inventoryTypeId?: string | null;
  freeField1?: string | null;
  freeField2?: string | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean | null;
  isActive?: boolean | null;
  consumptionDetails?: IConsumptionDetails[] | null;
  product?: IProduct | null;
  purchaseOrder?: IPurchaseOrder | null;
  productTransaction?: IProductTransaction | null;
  wareHouses?: IWareHouse[] | null;
  securityUsers?: ISecurityUser[] | null;
}

export class ProductInventory implements IProductInventory {
  constructor(
    public id?: number,
    public inwardOutwardDate?: dayjs.Dayjs | null,
    public inwardQty?: string | null,
    public outwardQty?: string | null,
    public totalQuanity?: string | null,
    public pricePerUnit?: number | null,
    public lotNo?: string | null,
    public expiryDate?: dayjs.Dayjs | null,
    public inventoryTypeId?: string | null,
    public freeField1?: string | null,
    public freeField2?: string | null,
    public lastModified?: dayjs.Dayjs | null,
    public lastModifiedBy?: string | null,
    public isDeleted?: boolean | null,
    public isActive?: boolean | null,
    public consumptionDetails?: IConsumptionDetails[] | null,
    public product?: IProduct | null,
    public purchaseOrder?: IPurchaseOrder | null,
    public productTransaction?: IProductTransaction | null,
    public wareHouses?: IWareHouse[] | null,
    public securityUsers?: ISecurityUser[] | null
  ) {
    this.isDeleted = this.isDeleted ?? false;
    this.isActive = this.isActive ?? false;
  }
}

export function getProductInventoryIdentifier(productInventory: IProductInventory): number | undefined {
  return productInventory.id;
}
