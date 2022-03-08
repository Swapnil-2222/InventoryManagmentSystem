import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUnit, getUnitIdentifier } from '../unit.model';

export type EntityResponseType = HttpResponse<IUnit>;
export type EntityArrayResponseType = HttpResponse<IUnit[]>;

@Injectable({ providedIn: 'root' })
export class UnitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/units');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(unit: IUnit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(unit);
    return this.http
      .post<IUnit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(unit: IUnit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(unit);
    return this.http
      .put<IUnit>(`${this.resourceUrl}/${getUnitIdentifier(unit) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(unit: IUnit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(unit);
    return this.http
      .patch<IUnit>(`${this.resourceUrl}/${getUnitIdentifier(unit) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUnit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUnitToCollectionIfMissing(unitCollection: IUnit[], ...unitsToCheck: (IUnit | null | undefined)[]): IUnit[] {
    const units: IUnit[] = unitsToCheck.filter(isPresent);
    if (units.length > 0) {
      const unitCollectionIdentifiers = unitCollection.map(unitItem => getUnitIdentifier(unitItem)!);
      const unitsToAdd = units.filter(unitItem => {
        const unitIdentifier = getUnitIdentifier(unitItem);
        if (unitIdentifier == null || unitCollectionIdentifiers.includes(unitIdentifier)) {
          return false;
        }
        unitCollectionIdentifiers.push(unitIdentifier);
        return true;
      });
      return [...unitsToAdd, ...unitCollection];
    }
    return unitCollection;
  }

  protected convertDateFromClient(unit: IUnit): IUnit {
    return Object.assign({}, unit, {
      lastModified: unit.lastModified?.isValid() ? unit.lastModified.toJSON() : undefined,
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
      res.body.forEach((unit: IUnit) => {
        unit.lastModified = unit.lastModified ? dayjs(unit.lastModified) : undefined;
      });
    }
    return res;
  }
}
