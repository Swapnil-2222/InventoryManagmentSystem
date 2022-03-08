import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProductTransaction, ProductTransaction } from '../product-transaction.model';

import { ProductTransactionService } from './product-transaction.service';

describe('ProductTransaction Service', () => {
  let service: ProductTransactionService;
  let httpMock: HttpTestingController;
  let elemDefault: IProductTransaction;
  let expectedResult: IProductTransaction | IProductTransaction[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductTransactionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      qtySold: 0,
      pricePerUnit: 0,
      lotNumber: 'AAAAAAA',
      expirydate: currentDate,
      totalAmount: 0,
      gstAmount: 0,
      description: 'AAAAAAA',
      lastModified: currentDate,
      lastModifiedBy: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          expirydate: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ProductTransaction', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          expirydate: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          expirydate: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.create(new ProductTransaction()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductTransaction', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          qtySold: 1,
          pricePerUnit: 1,
          lotNumber: 'BBBBBB',
          expirydate: currentDate.format(DATE_TIME_FORMAT),
          totalAmount: 1,
          gstAmount: 1,
          description: 'BBBBBB',
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          expirydate: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductTransaction', () => {
      const patchObject = Object.assign(
        {
          lotNumber: 'BBBBBB',
          expirydate: currentDate.format(DATE_TIME_FORMAT),
          totalAmount: 1,
          gstAmount: 1,
          description: 'BBBBBB',
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        new ProductTransaction()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          expirydate: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductTransaction', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          qtySold: 1,
          pricePerUnit: 1,
          lotNumber: 'BBBBBB',
          expirydate: currentDate.format(DATE_TIME_FORMAT),
          totalAmount: 1,
          gstAmount: 1,
          description: 'BBBBBB',
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          expirydate: currentDate,
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

    it('should delete a ProductTransaction', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProductTransactionToCollectionIfMissing', () => {
      it('should add a ProductTransaction to an empty array', () => {
        const productTransaction: IProductTransaction = { id: 123 };
        expectedResult = service.addProductTransactionToCollectionIfMissing([], productTransaction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productTransaction);
      });

      it('should not add a ProductTransaction to an array that contains it', () => {
        const productTransaction: IProductTransaction = { id: 123 };
        const productTransactionCollection: IProductTransaction[] = [
          {
            ...productTransaction,
          },
          { id: 456 },
        ];
        expectedResult = service.addProductTransactionToCollectionIfMissing(productTransactionCollection, productTransaction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductTransaction to an array that doesn't contain it", () => {
        const productTransaction: IProductTransaction = { id: 123 };
        const productTransactionCollection: IProductTransaction[] = [{ id: 456 }];
        expectedResult = service.addProductTransactionToCollectionIfMissing(productTransactionCollection, productTransaction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productTransaction);
      });

      it('should add only unique ProductTransaction to an array', () => {
        const productTransactionArray: IProductTransaction[] = [{ id: 123 }, { id: 456 }, { id: 29732 }];
        const productTransactionCollection: IProductTransaction[] = [{ id: 123 }];
        expectedResult = service.addProductTransactionToCollectionIfMissing(productTransactionCollection, ...productTransactionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productTransaction: IProductTransaction = { id: 123 };
        const productTransaction2: IProductTransaction = { id: 456 };
        expectedResult = service.addProductTransactionToCollectionIfMissing([], productTransaction, productTransaction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productTransaction);
        expect(expectedResult).toContain(productTransaction2);
      });

      it('should accept null and undefined values', () => {
        const productTransaction: IProductTransaction = { id: 123 };
        expectedResult = service.addProductTransactionToCollectionIfMissing([], null, productTransaction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productTransaction);
      });

      it('should return initial array if no ProductTransaction is added', () => {
        const productTransactionCollection: IProductTransaction[] = [{ id: 123 }];
        expectedResult = service.addProductTransactionToCollectionIfMissing(productTransactionCollection, undefined, null);
        expect(expectedResult).toEqual(productTransactionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
