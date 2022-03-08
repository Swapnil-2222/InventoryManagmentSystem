import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PurchaseOrderDetailsDetailComponent } from './purchase-order-details-detail.component';

describe('PurchaseOrderDetails Management Detail Component', () => {
  let comp: PurchaseOrderDetailsDetailComponent;
  let fixture: ComponentFixture<PurchaseOrderDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PurchaseOrderDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ purchaseOrderDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PurchaseOrderDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PurchaseOrderDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load purchaseOrderDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.purchaseOrderDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
