import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWareHouse, getWareHouseIdentifier } from '../ware-house.model';

export type EntityResponseType = HttpResponse<IWareHouse>;
export type EntityArrayResponseType = HttpResponse<IWareHouse[]>;

@Injectable({ providedIn: 'root' })
export class WareHouseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ware-houses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(wareHouse: IWareHouse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wareHouse);
    return this.http
      .post<IWareHouse>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(wareHouse: IWareHouse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wareHouse);
    return this.http
      .put<IWareHouse>(`${this.resourceUrl}/${getWareHouseIdentifier(wareHouse) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(wareHouse: IWareHouse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wareHouse);
    return this.http
      .patch<IWareHouse>(`${this.resourceUrl}/${getWareHouseIdentifier(wareHouse) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWareHouse>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWareHouse[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWareHouseToCollectionIfMissing(
    wareHouseCollection: IWareHouse[],
    ...wareHousesToCheck: (IWareHouse | null | undefined)[]
  ): IWareHouse[] {
    const wareHouses: IWareHouse[] = wareHousesToCheck.filter(isPresent);
    if (wareHouses.length > 0) {
      const wareHouseCollectionIdentifiers = wareHouseCollection.map(wareHouseItem => getWareHouseIdentifier(wareHouseItem)!);
      const wareHousesToAdd = wareHouses.filter(wareHouseItem => {
        const wareHouseIdentifier = getWareHouseIdentifier(wareHouseItem);
        if (wareHouseIdentifier == null || wareHouseCollectionIdentifiers.includes(wareHouseIdentifier)) {
          return false;
        }
        wareHouseCollectionIdentifiers.push(wareHouseIdentifier);
        return true;
      });
      return [...wareHousesToAdd, ...wareHouseCollection];
    }
    return wareHouseCollection;
  }

  protected convertDateFromClient(wareHouse: IWareHouse): IWareHouse {
    return Object.assign({}, wareHouse, {
      lastModified: wareHouse.lastModified?.isValid() ? wareHouse.lastModified.toJSON() : undefined,
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
      res.body.forEach((wareHouse: IWareHouse) => {
        wareHouse.lastModified = wareHouse.lastModified ? dayjs(wareHouse.lastModified) : undefined;
      });
    }
    return res;
  }
}
