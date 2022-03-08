import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPurchaseOrderDetails, PurchaseOrderDetails } from '../purchase-order-details.model';

import { PurchaseOrderDetailsService } from './purchase-order-details.service';

describe('PurchaseOrderDetails Service', () => {
  let service: PurchaseOrderDetailsService;
  let httpMock: HttpTestingController;
  let elemDefault: IPurchaseOrderDetails;
  let expectedResult: IPurchaseOrderDetails | IPurchaseOrderDetails[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PurchaseOrderDetailsService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      qtyordered: 0,
      gstTaxPercentage: 0,
      pricePerUnit: 0,
      totalPrice: 0,
      discount: 0,
      lastModified: currentDate,
      lastModifiedBy: 'AAAAAAA',
      freeField1: 'AAAAAAA',
      freeField2: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PurchaseOrderDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.create(new PurchaseOrderDetails()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PurchaseOrderDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          qtyordered: 1,
          gstTaxPercentage: 1,
          pricePerUnit: 1,
          totalPrice: 1,
          discount: 1,
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
          freeField1: 'BBBBBB',
          freeField2: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PurchaseOrderDetails', () => {
      const patchObject = Object.assign(
        {
          qtyordered: 1,
          lastModifiedBy: 'BBBBBB',
          freeField2: 'BBBBBB',
        },
        new PurchaseOrderDetails()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PurchaseOrderDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          qtyordered: 1,
          gstTaxPercentage: 1,
          pricePerUnit: 1,
          totalPrice: 1,
          discount: 1,
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
          freeField1: 'BBBBBB',
          freeField2: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PurchaseOrderDetails', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPurchaseOrderDetailsToCollectionIfMissing', () => {
      it('should add a PurchaseOrderDetails to an empty array', () => {
        const purchaseOrderDetails: IPurchaseOrderDetails = { id: 123 };
        expectedResult = service.addPurchaseOrderDetailsToCollectionIfMissing([], purchaseOrderDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(purchaseOrderDetails);
      });

      it('should not add a PurchaseOrderDetails to an array that contains it', () => {
        const purchaseOrderDetails: IPurchaseOrderDetails = { id: 123 };
        const purchaseOrderDetailsCollection: IPurchaseOrderDetails[] = [
          {
            ...purchaseOrderDetails,
          },
          { id: 456 },
        ];
        expectedResult = service.addPurchaseOrderDetailsToCollectionIfMissing(purchaseOrderDetailsCollection, purchaseOrderDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PurchaseOrderDetails to an array that doesn't contain it", () => {
        const purchaseOrderDetails: IPurchaseOrderDetails = { id: 123 };
        const purchaseOrderDetailsCollection: IPurchaseOrderDetails[] = [{ id: 456 }];
        expectedResult = service.addPurchaseOrderDetailsToCollectionIfMissing(purchaseOrderDetailsCollection, purchaseOrderDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(purchaseOrderDetails);
      });

      it('should add only unique PurchaseOrderDetails to an array', () => {
        const purchaseOrderDetailsArray: IPurchaseOrderDetails[] = [{ id: 123 }, { id: 456 }, { id: 32391 }];
        const purchaseOrderDetailsCollection: IPurchaseOrderDetails[] = [{ id: 123 }];
        expectedResult = service.addPurchaseOrderDetailsToCollectionIfMissing(purchaseOrderDetailsCollection, ...purchaseOrderDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const purchaseOrderDetails: IPurchaseOrderDetails = { id: 123 };
        const purchaseOrderDetails2: IPurchaseOrderDetails = { id: 456 };
        expectedResult = service.addPurchaseOrderDetailsToCollectionIfMissing([], purchaseOrderDetails, purchaseOrderDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(purchaseOrderDetails);
        expect(expectedResult).toContain(purchaseOrderDetails2);
      });

      it('should accept null and undefined values', () => {
        const purchaseOrderDetails: IPurchaseOrderDetails = { id: 123 };
        expectedResult = service.addPurchaseOrderDetailsToCollectionIfMissing([], null, purchaseOrderDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(purchaseOrderDetails);
      });

      it('should return initial array if no PurchaseOrderDetails is added', () => {
        const purchaseOrderDetailsCollection: IPurchaseOrderDetails[] = [{ id: 123 }];
        expectedResult = service.addPurchaseOrderDetailsToCollectionIfMissing(purchaseOrderDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(purchaseOrderDetailsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
