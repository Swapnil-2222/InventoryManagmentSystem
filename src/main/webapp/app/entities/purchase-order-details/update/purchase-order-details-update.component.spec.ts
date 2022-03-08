import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PurchaseOrderDetailsService } from '../service/purchase-order-details.service';
import { IPurchaseOrderDetails, PurchaseOrderDetails } from '../purchase-order-details.model';
import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/purchase-order/service/purchase-order.service';

import { PurchaseOrderDetailsUpdateComponent } from './purchase-order-details-update.component';

describe('PurchaseOrderDetails Management Update Component', () => {
  let comp: PurchaseOrderDetailsUpdateComponent;
  let fixture: ComponentFixture<PurchaseOrderDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let purchaseOrderDetailsService: PurchaseOrderDetailsService;
  let purchaseOrderService: PurchaseOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PurchaseOrderDetailsUpdateComponent],
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
      .overrideTemplate(PurchaseOrderDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PurchaseOrderDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    purchaseOrderDetailsService = TestBed.inject(PurchaseOrderDetailsService);
    purchaseOrderService = TestBed.inject(PurchaseOrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PurchaseOrder query and add missing value', () => {
      const purchaseOrderDetails: IPurchaseOrderDetails = { id: 456 };
      const purchaseOrder: IPurchaseOrder = { id: 3510 };
      purchaseOrderDetails.purchaseOrder = purchaseOrder;

      const purchaseOrderCollection: IPurchaseOrder[] = [{ id: 90536 }];
      jest.spyOn(purchaseOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: purchaseOrderCollection })));
      const additionalPurchaseOrders = [purchaseOrder];
      const expectedCollection: IPurchaseOrder[] = [...additionalPurchaseOrders, ...purchaseOrderCollection];
      jest.spyOn(purchaseOrderService, 'addPurchaseOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchaseOrderDetails });
      comp.ngOnInit();

      expect(purchaseOrderService.query).toHaveBeenCalled();
      expect(purchaseOrderService.addPurchaseOrderToCollectionIfMissing).toHaveBeenCalledWith(
        purchaseOrderCollection,
        ...additionalPurchaseOrders
      );
      expect(comp.purchaseOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const purchaseOrderDetails: IPurchaseOrderDetails = { id: 456 };
      const purchaseOrder: IPurchaseOrder = { id: 55538 };
      purchaseOrderDetails.purchaseOrder = purchaseOrder;

      activatedRoute.data = of({ purchaseOrderDetails });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(purchaseOrderDetails));
      expect(comp.purchaseOrdersSharedCollection).toContain(purchaseOrder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PurchaseOrderDetails>>();
      const purchaseOrderDetails = { id: 123 };
      jest.spyOn(purchaseOrderDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseOrderDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchaseOrderDetails }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(purchaseOrderDetailsService.update).toHaveBeenCalledWith(purchaseOrderDetails);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PurchaseOrderDetails>>();
      const purchaseOrderDetails = new PurchaseOrderDetails();
      jest.spyOn(purchaseOrderDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseOrderDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchaseOrderDetails }));
      saveSubject.complete();

      // THEN
      expect(purchaseOrderDetailsService.create).toHaveBeenCalledWith(purchaseOrderDetails);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PurchaseOrderDetails>>();
      const purchaseOrderDetails = { id: 123 };
      jest.spyOn(purchaseOrderDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseOrderDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(purchaseOrderDetailsService.update).toHaveBeenCalledWith(purchaseOrderDetails);
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
