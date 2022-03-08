import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPurchaseOrderDetails, PurchaseOrderDetails } from '../purchase-order-details.model';
import { PurchaseOrderDetailsService } from '../service/purchase-order-details.service';

@Injectable({ providedIn: 'root' })
export class PurchaseOrderDetailsRoutingResolveService implements Resolve<IPurchaseOrderDetails> {
  constructor(protected service: PurchaseOrderDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPurchaseOrderDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((purchaseOrderDetails: HttpResponse<PurchaseOrderDetails>) => {
          if (purchaseOrderDetails.body) {
            return of(purchaseOrderDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PurchaseOrderDetails());
  }
}
