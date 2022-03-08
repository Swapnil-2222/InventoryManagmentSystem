import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'product',
        data: { pageTitle: 'inventoryManagmentSystemApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'categories',
        data: { pageTitle: 'inventoryManagmentSystemApp.categories.home.title' },
        loadChildren: () => import('./categories/categories.module').then(m => m.CategoriesModule),
      },
      {
        path: 'unit',
        data: { pageTitle: 'inventoryManagmentSystemApp.unit.home.title' },
        loadChildren: () => import('./unit/unit.module').then(m => m.UnitModule),
      },
      {
        path: 'ware-house',
        data: { pageTitle: 'inventoryManagmentSystemApp.wareHouse.home.title' },
        loadChildren: () => import('./ware-house/ware-house.module').then(m => m.WareHouseModule),
      },
      {
        path: 'product-inventory',
        data: { pageTitle: 'inventoryManagmentSystemApp.productInventory.home.title' },
        loadChildren: () => import('./product-inventory/product-inventory.module').then(m => m.ProductInventoryModule),
      },
      {
        path: 'project',
        data: { pageTitle: 'inventoryManagmentSystemApp.project.home.title' },
        loadChildren: () => import('./project/project.module').then(m => m.ProjectModule),
      },
      {
        path: 'consumption-details',
        data: { pageTitle: 'inventoryManagmentSystemApp.consumptionDetails.home.title' },
        loadChildren: () => import('./consumption-details/consumption-details.module').then(m => m.ConsumptionDetailsModule),
      },
      {
        path: 'product-transaction',
        data: { pageTitle: 'inventoryManagmentSystemApp.productTransaction.home.title' },
        loadChildren: () => import('./product-transaction/product-transaction.module').then(m => m.ProductTransactionModule),
      },
      {
        path: 'purchase-order',
        data: { pageTitle: 'inventoryManagmentSystemApp.purchaseOrder.home.title' },
        loadChildren: () => import('./purchase-order/purchase-order.module').then(m => m.PurchaseOrderModule),
      },
      {
        path: 'purchase-order-details',
        data: { pageTitle: 'inventoryManagmentSystemApp.purchaseOrderDetails.home.title' },
        loadChildren: () => import('./purchase-order-details/purchase-order-details.module').then(m => m.PurchaseOrderDetailsModule),
      },
      {
        path: 'goods-recived',
        data: { pageTitle: 'inventoryManagmentSystemApp.goodsRecived.home.title' },
        loadChildren: () => import('./goods-recived/goods-recived.module').then(m => m.GoodsRecivedModule),
      },
      {
        path: 'transfer',
        data: { pageTitle: 'inventoryManagmentSystemApp.transfer.home.title' },
        loadChildren: () => import('./transfer/transfer.module').then(m => m.TransferModule),
      },
      {
        path: 'transfer-details',
        data: { pageTitle: 'inventoryManagmentSystemApp.transferDetails.home.title' },
        loadChildren: () => import('./transfer-details/transfer-details.module').then(m => m.TransferDetailsModule),
      },
      {
        path: 'transfer-details-approvals',
        data: { pageTitle: 'inventoryManagmentSystemApp.transferDetailsApprovals.home.title' },
        loadChildren: () =>
          import('./transfer-details-approvals/transfer-details-approvals.module').then(m => m.TransferDetailsApprovalsModule),
      },
      {
        path: 'transfer-recieved',
        data: { pageTitle: 'inventoryManagmentSystemApp.transferRecieved.home.title' },
        loadChildren: () => import('./transfer-recieved/transfer-recieved.module').then(m => m.TransferRecievedModule),
      },
      {
        path: 'security-user',
        data: { pageTitle: 'inventoryManagmentSystemApp.securityUser.home.title' },
        loadChildren: () => import('./security-user/security-user.module').then(m => m.SecurityUserModule),
      },
      {
        path: 'security-role',
        data: { pageTitle: 'inventoryManagmentSystemApp.securityRole.home.title' },
        loadChildren: () => import('./security-role/security-role.module').then(m => m.SecurityRoleModule),
      },
      {
        path: 'security-permission',
        data: { pageTitle: 'inventoryManagmentSystemApp.securityPermission.home.title' },
        loadChildren: () => import('./security-permission/security-permission.module').then(m => m.SecurityPermissionModule),
      },
      {
        path: 'user-access',
        data: { pageTitle: 'inventoryManagmentSystemApp.userAccess.home.title' },
        loadChildren: () => import('./user-access/user-access.module').then(m => m.UserAccessModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
