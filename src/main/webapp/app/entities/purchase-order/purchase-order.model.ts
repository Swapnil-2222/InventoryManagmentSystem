import dayjs from 'dayjs/esm';
import { IPurchaseOrderDetails } from 'app/entities/purchase-order-details/purchase-order-details.model';
import { IGoodsRecived } from 'app/entities/goods-recived/goods-recived.model';
import { ISecurityUser } from 'app/entities/security-user/security-user.model';
import { IProductInventory } from 'app/entities/product-inventory/product-inventory.model';
import { OrderType } from 'app/entities/enumerations/order-type.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IPurchaseOrder {
  id?: number;
  totalPOAmount?: number | null;
  totalGSTAmount?: number | null;
  expectedDeliveryDate?: dayjs.Dayjs | null;
  poDate?: dayjs.Dayjs | null;
  orderType?: OrderType | null;
  orderStatus?: Status | null;
  clientName?: string | null;
  clientMobile?: string | null;
  clientEmail?: string | null;
  termsAndCondition?: string | null;
  notes?: string | null;
  lastModified?: dayjs.Dayjs;
  lastModifiedBy?: string;
  freeField1?: string | null;
  freeField2?: string | null;
  purchaseOrderDetails?: IPurchaseOrderDetails[] | null;
  goodReciveds?: IGoodsRecived[] | null;
  securityUser?: ISecurityUser | null;
  productInventories?: IProductInventory[] | null;
}

export class PurchaseOrder implements IPurchaseOrder {
  constructor(
    public id?: number,
    public totalPOAmount?: number | null,
    public totalGSTAmount?: number | null,
    public expectedDeliveryDate?: dayjs.Dayjs | null,
    public poDate?: dayjs.Dayjs | null,
    public orderType?: OrderType | null,
    public orderStatus?: Status | null,
    public clientName?: string | null,
    public clientMobile?: string | null,
    public clientEmail?: string | null,
    public termsAndCondition?: string | null,
    public notes?: string | null,
    public lastModified?: dayjs.Dayjs,
    public lastModifiedBy?: string,
    public freeField1?: string | null,
    public freeField2?: string | null,
    public purchaseOrderDetails?: IPurchaseOrderDetails[] | null,
    public goodReciveds?: IGoodsRecived[] | null,
    public securityUser?: ISecurityUser | null,
    public productInventories?: IProductInventory[] | null
  ) {}
}

export function getPurchaseOrderIdentifier(purchaseOrder: IPurchaseOrder): number | undefined {
  return purchaseOrder.id;
}
