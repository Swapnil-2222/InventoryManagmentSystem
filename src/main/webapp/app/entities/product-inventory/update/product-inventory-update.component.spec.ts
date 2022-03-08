import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductInventoryService } from '../service/product-inventory.service';
import { IProductInventory, ProductInventory } from '../product-inventory.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/purchase-order/service/purchase-order.service';
import { IProductTransaction } from 'app/entities/product-transaction/product-transaction.model';
import { ProductTransactionService } from 'app/entities/product-transaction/service/product-transaction.service';
import { IWareHouse } from 'app/entities/ware-house/ware-house.model';
import { WareHouseService } from 'app/entities/ware-house/service/ware-house.service';
import { ISecurityUser } from 'app/entities/security-user/security-user.model';
import { SecurityUserService } from 'app/entities/security-user/service/security-user.service';

import { ProductInventoryUpdateComponent } from './product-inventory-update.component';

describe('ProductInventory Management Update Component', () => {
  let comp: ProductInventoryUpdateComponent;
  let fixture: ComponentFixture<ProductInventoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productInventoryService: ProductInventoryService;
  let productService: ProductService;
  let purchaseOrderService: PurchaseOrderService;
  let productTransactionService: ProductTransactionService;
  let wareHouseService: WareHouseService;
  let securityUserService: SecurityUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductInventoryUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProductInventoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductInventoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productInventoryService = TestBed.inject(ProductInventoryService);
    productService = TestBed.inject(ProductService);
    purchaseOrderService = TestBed.inject(PurchaseOrderService);
    productTransactionService = TestBed.inject(ProductTransactionService);
    wareHouseService = TestBed.inject(WareHouseService);
    securityUserService = TestBed.inject(SecurityUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const productInventory: IProductInventory = { id: 456 };
      const product: IProduct = { id: 25300 };
      productInventory.product = product;

      const productCollection: IProduct[] = [{ id: 9524 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productInventory });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PurchaseOrder query and add missing value', () => {
      const productInventory: IProductInventory = { id: 456 };
      const purchaseOrder: IPurchaseOrder = { id: 69854 };
      productInventory.purchaseOrder = purchaseOrder;

      const purchaseOrderCollection: IPurchaseOrder[] = [{ id: 42572 }];
      jest.spyOn(purchaseOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: purchaseOrderCollection })));
      const additionalPurchaseOrders = [purchaseOrder];
      const expectedCollection: IPurchaseOrder[] = [...additionalPurchaseOrders, ...purchaseOrderCollection];
      jest.spyOn(purchaseOrderService, 'addPurchaseOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productInventory });
      comp.ngOnInit();

      expect(purchaseOrderService.query).toHaveBeenCalled();
      expect(purchaseOrderService.addPurchaseOrderToCollectionIfMissing).toHaveBeenCalledWith(
        purchaseOrderCollection,
        ...additionalPurchaseOrders
      );
      expect(comp.purchaseOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ProductTransaction query and add missing value', () => {
      const productInventory: IProductInventory = { id: 456 };
      const productTransaction: IProductTransaction = { id: 72208 };
      productInventory.productTransaction = productTransaction;

      const productTransactionCollection: IProductTransaction[] = [{ id: 72035 }];
      jest.spyOn(productTransactionService, 'query').mockReturnValue(of(new HttpResponse({ body: productTransactionCollection })));
      const additionalProductTransactions = [productTransaction];
      const expectedCollection: IProductTransaction[] = [...additionalProductTransactions, ...productTransactionCollection];
      jest.spyOn(productTransactionService, 'addProductTransactionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productInventory });
      comp.ngOnInit();

      expect(productTransactionService.query).toHaveBeenCalled();
      expect(productTransactionService.addProductTransactionToCollectionIfMissing).toHaveBeenCalledWith(
        productTransactionCollection,
        ...additionalProductTransactions
      );
      expect(comp.productTransactionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call WareHouse query and add missing value', () => {
      const productInventory: IProductInventory = { id: 456 };
      const wareHouses: IWareHouse[] = [{ id: 91935 }];
      productInventory.wareHouses = wareHouses;

      const wareHouseCollection: IWareHouse[] = [{ id: 1014 }];
      jest.spyOn(wareHouseService, 'query').mockReturnValue(of(new HttpResponse({ body: wareHouseCollection })));
      const additionalWareHouses = [...wareHouses];
      const expectedCollection: IWareHouse[] = [...additionalWareHouses, ...wareHouseCollection];
      jest.spyOn(wareHouseService, 'addWareHouseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productInventory });
      comp.ngOnInit();

      expect(wareHouseService.query).toHaveBeenCalled();
      expect(wareHouseService.addWareHouseToCollectionIfMissing).toHaveBeenCalledWith(wareHouseCollection, ...additionalWareHouses);
      expect(comp.wareHousesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SecurityUser query and add missing value', () => {
      const productInventory: IProductInventory = { id: 456 };
      const securityUsers: ISecurityUser[] = [{ id: 35139 }];
      productInventory.securityUsers = securityUsers;

      const securityUserCollection: ISecurityUser[] = [{ id: 61561 }];
      jest.spyOn(securityUserService, 'query').mockReturnValue(of(new HttpResponse({ body: securityUserCollection })));
      const additionalSecurityUsers = [...securityUsers];
      const expectedCollection: ISecurityUser[] = [...additionalSecurityUsers, ...securityUserCollection];
      jest.spyOn(securityUserService, 'addSecurityUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productInventory });
      comp.ngOnInit();

      expect(securityUserService.query).toHaveBeenCalled();
      expect(securityUserService.addSecurityUserToCollectionIfMissing).toHaveBeenCalledWith(
        securityUserCollection,
        ...additionalSecurityUsers
      );
      expect(comp.securityUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productInventory: IProductInventory = { id: 456 };
      const product: IProduct = { id: 5809 };
      productInventory.product = product;
      const purchaseOrder: IPurchaseOrder = { id: 56675 };
      productInventory.purchaseOrder = purchaseOrder;
      const productTransaction: IProductTransaction = { id: 26716 };
      productInventory.productTransaction = productTransaction;
      const wareHouses: IWareHouse = { id: 51611 };
      productInventory.wareHouses = [wareHouses];
      const securityUsers: ISecurityUser = { id: 64290 };
      productInventory.securityUsers = [securityUsers];

      activatedRoute.data = of({ productInventory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(productInventory));
      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.purchaseOrdersSharedCollection).toContain(purchaseOrder);
      expect(comp.productTransactionsSharedCollection).toContain(productTransaction);
      expect(comp.wareHousesSharedCollection).toContain(wareHouses);
      expect(comp.securityUsersSharedCollection).toContain(securityUsers);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProductInventory>>();
      const productInventory = { id: 123 };
      jest.spyOn(productInventoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productInventory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productInventory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(productInventoryService.update).toHaveBeenCalledWith(productInventory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProductInventory>>();
      const productInventory = new ProductInventory();
      jest.spyOn(productInventoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productInventory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productInventory }));
      saveSubject.complete();

      // THEN
      expect(productInventoryService.create).toHaveBeenCalledWith(productInventory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProductInventory>>();
      const productInventory = { id: 123 };
      jest.spyOn(productInventoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productInventory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productInventoryService.update).toHaveBeenCalledWith(productInventory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackProductById', () => {
      it('Should return tracked Product primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProductById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPurchaseOrderById', () => {
      it('Should return tracked PurchaseOrder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPurchaseOrderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackProductTransactionById', () => {
      it('Should return tracked ProductTransaction primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProductTransactionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackWareHouseById', () => {
      it('Should return tracked WareHouse primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackWareHouseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSecurityUserById', () => {
      it('Should return tracked SecurityUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSecurityUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedWareHouse', () => {
      it('Should return option if no WareHouse is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedWareHouse(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected WareHouse for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedWareHouse(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this WareHouse is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedWareHouse(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedSecurityUser', () => {
      it('Should return option if no SecurityUser is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedSecurityUser(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected SecurityUser for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedSecurityUser(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this SecurityUser is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedSecurityUser(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
