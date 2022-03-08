import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';
import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';

export interface IPurchaseOrderDetails {
  id?: number;
  qtyordered?: number | null;
  gstTaxPercentage?: number | null;
  pricePerUnit?: number | null;
  totalPrice?: number | null;
  discount?: number | null;
  lastModified?: dayjs.Dayjs;
  lastModifiedBy?: string;
  freeField1?: string | null;
  freeField2?: string | null;
  products?: IProduct[] | null;
  purchaseOrder?: IPurchaseOrder | null;
}

export class PurchaseOrderDetails implements IPurchaseOrderDetails {
  constructor(
    public id?: number,
    public qtyordered?: number | null,
    public gstTaxPercentage?: number | null,
    public pricePerUnit?: number | null,
    public totalPrice?: number | null,
    public discount?: number | null,
    public lastModified?: dayjs.Dayjs,
    public lastModifiedBy?: string,
    public freeField1?: string | null,
    public freeField2?: string | null,
    public products?: IProduct[] | null,
    public purchaseOrder?: IPurchaseOrder | null
  ) {}
}

export function getPurchaseOrderDetailsIdentifier(purchaseOrderDetails: IPurchaseOrderDetails): number | undefined {
  return purchaseOrderDetails.id;
}
