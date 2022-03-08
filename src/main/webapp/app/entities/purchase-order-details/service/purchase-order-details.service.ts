import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPurchaseOrderDetails, getPurchaseOrderDetailsIdentifier } from '../purchase-order-details.model';

export type EntityResponseType = HttpResponse<IPurchaseOrderDetails>;
export type EntityArrayResponseType = HttpResponse<IPurchaseOrderDetails[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseOrderDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/purchase-order-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(purchaseOrderDetails: IPurchaseOrderDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseOrderDetails);
    return this.http
      .post<IPurchaseOrderDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(purchaseOrderDetails: IPurchaseOrderDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseOrderDetails);
    return this.http
      .put<IPurchaseOrderDetails>(`${this.resourceUrl}/${getPurchaseOrderDetailsIdentifier(purchaseOrderDetails) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(purchaseOrderDetails: IPurchaseOrderDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseOrderDetails);
    return this.http
      .patch<IPurchaseOrderDetails>(`${this.resourceUrl}/${getPurchaseOrderDetailsIdentifier(purchaseOrderDetails) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPurchaseOrderDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPurchaseOrderDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPurchaseOrderDetailsToCollectionIfMissing(
    purchaseOrderDetailsCollection: IPurchaseOrderDetails[],
    ...purchaseOrderDetailsToCheck: (IPurchaseOrderDetails | null | undefined)[]
  ): IPurchaseOrderDetails[] {
    const purchaseOrderDetails: IPurchaseOrderDetails[] = purchaseOrderDetailsToCheck.filter(isPresent);
    if (purchaseOrderDetails.length > 0) {
      const purchaseOrderDetailsCollectionIdentifiers = purchaseOrderDetailsCollection.map(
        purchaseOrderDetailsItem => getPurchaseOrderDetailsIdentifier(purchaseOrderDetailsItem)!
      );
      const purchaseOrderDetailsToAdd = purchaseOrderDetails.filter(purchaseOrderDetailsItem => {
        const purchaseOrderDetailsIdentifier = getPurchaseOrderDetailsIdentifier(purchaseOrderDetailsItem);
        if (purchaseOrderDetailsIdentifier == null || purchaseOrderDetailsCollectionIdentifiers.includes(purchaseOrderDetailsIdentifier)) {
          return false;
        }
        purchaseOrderDetailsCollectionIdentifiers.push(purchaseOrderDetailsIdentifier);
        return true;
      });
      return [...purchaseOrderDetailsToAdd, ...purchaseOrderDetailsCollection];
    }
    return purchaseOrderDetailsCollection;
  }

  protected convertDateFromClient(purchaseOrderDetails: IPurchaseOrderDetails): IPurchaseOrderDetails {
    return Object.assign({}, purchaseOrderDetails, {
      lastModified: purchaseOrderDetails.lastModified?.isValid() ? purchaseOrderDetails.lastModified.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((purchaseOrderDetails: IPurchaseOrderDetails) => {
        purchaseOrderDetails.lastModified = purchaseOrderDetails.lastModified ? dayjs(purchaseOrderDetails.lastModified) : undefined;
      });
    }
    return res;
  }
}
