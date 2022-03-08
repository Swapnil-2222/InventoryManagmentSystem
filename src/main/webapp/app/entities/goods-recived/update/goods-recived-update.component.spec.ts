import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GoodsRecivedService } from '../service/goods-recived.service';
import { IGoodsRecived, GoodsRecived } from '../goods-recived.model';
import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/purchase-order/service/purchase-order.service';

import { GoodsRecivedUpdateComponent } from './goods-recived-update.component';

describe('GoodsRecived Management Update Component', () => {
  let comp: GoodsRecivedUpdateComponent;
  let fixture: ComponentFixture<GoodsRecivedUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let goodsRecivedService: GoodsRecivedService;
  let purchaseOrderService: PurchaseOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GoodsRecivedUpdateComponent],
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
      .overrideTemplate(GoodsRecivedUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GoodsRecivedUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    goodsRecivedService = TestBed.inject(GoodsRecivedService);
    purchaseOrderService = TestBed.inject(PurchaseOrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PurchaseOrder query and add missing value', () => {
      const goodsRecived: IGoodsRecived = { id: 456 };
      const purchaseOrder: IPurchaseOrder = { id: 29625 };
      goodsRecived.purchaseOrder = purchaseOrder;

      const purchaseOrderCollection: IPurchaseOrder[] = [{ id: 957 }];
      jest.spyOn(purchaseOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: purchaseOrderCollection })));
      const additionalPurchaseOrders = [purchaseOrder];
      const expectedCollection: IPurchaseOrder[] = [...additionalPurchaseOrders, ...purchaseOrderCollection];
      jest.spyOn(purchaseOrderService, 'addPurchaseOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ goodsRecived });
      comp.ngOnInit();

      expect(purchaseOrderService.query).toHaveBeenCalled();
      expect(purchaseOrderService.addPurchaseOrderToCollectionIfMissing).toHaveBeenCalledWith(
        purchaseOrderCollection,
        ...additionalPurchaseOrders
      );
      expect(comp.purchaseOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const goodsRecived: IGoodsRecived = { id: 456 };
      const purchaseOrder: IPurchaseOrder = { id: 46030 };
      goodsRecived.purchaseOrder = purchaseOrder;

      activatedRoute.data = of({ goodsRecived });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(goodsRecived));
      expect(comp.purchaseOrdersSharedCollection).toContain(purchaseOrder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GoodsRecived>>();
      const goodsRecived = { id: 123 };
      jest.spyOn(goodsRecivedService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ goodsRecived });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: goodsRecived }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(goodsRecivedService.update).toHaveBeenCalledWith(goodsRecived);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GoodsRecived>>();
      const goodsRecived = new GoodsRecived();
      jest.spyOn(goodsRecivedService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ goodsRecived });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: goodsRecived }));
      saveSubject.complete();

      // THEN
      expect(goodsRecivedService.create).toHaveBeenCalledWith(goodsRecived);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GoodsRecived>>();
      const goodsRecived = { id: 123 };
      jest.spyOn(goodsRecivedService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ goodsRecived });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(goodsRecivedService.update).toHaveBeenCalledWith(goodsRecived);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPurchaseOrderById', () => {
      it('Should return tracked PurchaseOrder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPurchaseOrderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
