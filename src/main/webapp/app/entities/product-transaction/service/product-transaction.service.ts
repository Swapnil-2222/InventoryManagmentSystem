import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductTransaction, getProductTransactionIdentifier } from '../product-transaction.model';

export type EntityResponseType = HttpResponse<IProductTransaction>;
export type EntityArrayResponseType = HttpResponse<IProductTransaction[]>;

@Injectable({ providedIn: 'root' })
export class ProductTransactionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-transactions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productTransaction: IProductTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productTransaction);
    return this.http
      .post<IProductTransaction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(productTransaction: IProductTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productTransaction);
    return this.http
      .put<IProductTransaction>(`${this.resourceUrl}/${getProductTransactionIdentifier(productTransaction) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(productTransaction: IProductTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productTransaction);
    return this.http
      .patch<IProductTransaction>(`${this.resourceUrl}/${getProductTransactionIdentifier(productTransaction) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProductTransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProductTransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProductTransactionToCollectionIfMissing(
    productTransactionCollection: IProductTransaction[],
    ...productTransactionsToCheck: (IProductTransaction | null | undefined)[]
  ): IProductTransaction[] {
    const productTransactions: IProductTransaction[] = productTransactionsToCheck.filter(isPresent);
    if (productTransactions.length > 0) {
      const productTransactionCollectionIdentifiers = productTransactionCollection.map(
        productTransactionItem => getProductTransactionIdentifier(productTransactionItem)!
      );
      const productTransactionsToAdd = productTransactions.filter(productTransactionItem => {
        const productTransactionIdentifier = getProductTransactionIdentifier(productTransactionItem);
        if (productTransactionIdentifier == null || productTransactionCollectionIdentifiers.includes(productTransactionIdentifier)) {
          return false;
        }
        productTransactionCollectionIdentifiers.push(productTransactionIdentifier);
        return true;
      });
      return [...productTransactionsToAdd, ...productTransactionCollection];
    }
    return productTransactionCollection;
  }

  protected convertDateFromClient(productTransaction: IProductTransaction): IProductTransaction {
    return Object.assign({}, productTransaction, {
      expirydate: productTransaction.expirydate?.isValid() ? productTransaction.expirydate.toJSON() : undefined,
      lastModified: productTransaction.lastModified?.isValid() ? productTransaction.lastModified.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.expirydate = res.body.expirydate ? dayjs(res.body.expirydate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((productTransaction: IProductTransaction) => {
        productTransaction.expirydate = productTransaction.expirydate ? dayjs(productTransaction.expirydate) : undefined;
        productTransaction.lastModified = productTransaction.lastModified ? dayjs(productTransaction.lastModified) : undefined;
      });
    }
    return res;
  }
}
