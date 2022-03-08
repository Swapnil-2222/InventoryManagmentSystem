import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PurchaseOrderService } from '../service/purchase-order.service';
import { IPurchaseOrder, PurchaseOrder } from '../purchase-order.model';
import { ISecurityUser } from 'app/entities/security-user/security-user.model';
import { SecurityUserService } from 'app/entities/security-user/service/security-user.service';

import { PurchaseOrderUpdateComponent } from './purchase-order-update.component';

describe('PurchaseOrder Management Update Component', () => {
  let comp: PurchaseOrderUpdateComponent;
  let fixture: ComponentFixture<PurchaseOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let purchaseOrderService: PurchaseOrderService;
  let securityUserService: SecurityUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PurchaseOrderUpdateComponent],
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
      .overrideTemplate(PurchaseOrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PurchaseOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    purchaseOrderService = TestBed.inject(PurchaseOrderService);
    securityUserService = TestBed.inject(SecurityUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SecurityUser query and add missing value', () => {
      const purchaseOrder: IPurchaseOrder = { id: 456 };
      const securityUser: ISecurityUser = { id: 81316 };
      purchaseOrder.securityUser = securityUser;

      const securityUserCollection: ISecurityUser[] = [{ id: 69279 }];
      jest.spyOn(securityUserService, 'query').mockReturnValue(of(new HttpResponse({ body: securityUserCollection })));
      const additionalSecurityUsers = [securityUser];
      const expectedCollection: ISecurityUser[] = [...additionalSecurityUsers, ...securityUserCollection];
      jest.spyOn(securityUserService, 'addSecurityUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      expect(securityUserService.query).toHaveBeenCalled();
      expect(securityUserService.addSecurityUserToCollectionIfMissing).toHaveBeenCalledWith(
        securityUserCollection,
        ...additionalSecurityUsers
      );
      expect(comp.securityUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const purchaseOrder: IPurchaseOrder = { id: 456 };
      const securityUser: ISecurityUser = { id: 90967 };
      purchaseOrder.securityUser = securityUser;

      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(purchaseOrder));
      expect(comp.securityUsersSharedCollection).toContain(securityUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PurchaseOrder>>();
      const purchaseOrder = { id: 123 };
      jest.spyOn(purchaseOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchaseOrder }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(purchaseOrderService.update).toHaveBeenCalledWith(purchaseOrder);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PurchaseOrder>>();
      const purchaseOrder = new PurchaseOrder();
      jest.spyOn(purchaseOrderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchaseOrder }));
      saveSubject.complete();

      // THEN
      expect(purchaseOrderService.create).toHaveBeenCalledWith(purchaseOrder);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PurchaseOrder>>();
      const purchaseOrder = { id: 123 };
      jest.spyOn(purchaseOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(purchaseOrderService.update).toHaveBeenCalledWith(purchaseOrder);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSecurityUserById', () => {
      it('Should return tracked SecurityUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSecurityUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
