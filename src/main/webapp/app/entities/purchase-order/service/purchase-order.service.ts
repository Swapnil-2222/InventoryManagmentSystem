import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPurchaseOrder, getPurchaseOrderIdentifier } from '../purchase-order.model';

export type EntityResponseType = HttpResponse<IPurchaseOrder>;
export type EntityArrayResponseType = HttpResponse<IPurchaseOrder[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseOrderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/purchase-orders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(purchaseOrder: IPurchaseOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseOrder);
    return this.http
      .post<IPurchaseOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(purchaseOrder: IPurchaseOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseOrder);
    return this.http
      .put<IPurchaseOrder>(`${this.resourceUrl}/${getPurchaseOrderIdentifier(purchaseOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(purchaseOrder: IPurchaseOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseOrder);
    return this.http
      .patch<IPurchaseOrder>(`${this.resourceUrl}/${getPurchaseOrderIdentifier(purchaseOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPurchaseOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPurchaseOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPurchaseOrderToCollectionIfMissing(
    purchaseOrderCollection: IPurchaseOrder[],
    ...purchaseOrdersToCheck: (IPurchaseOrder | null | undefined)[]
  ): IPurchaseOrder[] {
    const purchaseOrders: IPurchaseOrder[] = purchaseOrdersToCheck.filter(isPresent);
    if (purchaseOrders.length > 0) {
      const purchaseOrderCollectionIdentifiers = purchaseOrderCollection.map(
        purchaseOrderItem => getPurchaseOrderIdentifier(purchaseOrderItem)!
      );
      const purchaseOrdersToAdd = purchaseOrders.filter(purchaseOrderItem => {
        const purchaseOrderIdentifier = getPurchaseOrderIdentifier(purchaseOrderItem);
        if (purchaseOrderIdentifier == null || purchaseOrderCollectionIdentifiers.includes(purchaseOrderIdentifier)) {
          return false;
        }
        purchaseOrderCollectionIdentifiers.push(purchaseOrderIdentifier);
        return true;
      });
      return [...purchaseOrdersToAdd, ...purchaseOrderCollection];
    }
    return purchaseOrderCollection;
  }

  protected convertDateFromClient(purchaseOrder: IPurchaseOrder): IPurchaseOrder {
    return Object.assign({}, purchaseOrder, {
      expectedDeliveryDate: purchaseOrder.expectedDeliveryDate?.isValid() ? purchaseOrder.expectedDeliveryDate.toJSON() : undefined,
      poDate: purchaseOrder.poDate?.isValid() ? purchaseOrder.poDate.toJSON() : undefined,
      lastModified: purchaseOrder.lastModified?.isValid() ? purchaseOrder.lastModified.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.expectedDeliveryDate = res.body.expectedDeliveryDate ? dayjs(res.body.expectedDeliveryDate) : undefined;
      res.body.poDate = res.body.poDate ? dayjs(res.body.poDate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((purchaseOrder: IPurchaseOrder) => {
        purchaseOrder.expectedDeliveryDate = purchaseOrder.expectedDeliveryDate ? dayjs(purchaseOrder.expectedDeliveryDate) : undefined;
        purchaseOrder.poDate = purchaseOrder.poDate ? dayjs(purchaseOrder.poDate) : undefined;
        purchaseOrder.lastModified = purchaseOrder.lastModified ? dayjs(purchaseOrder.lastModified) : undefined;
      });
    }
    return res;
  }
}
