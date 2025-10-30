import { Routes } from '@angular/router';
import { AddProductComponent } from './modules/components/add-product/add-product.component';
import { ProductListComponent } from './modules/components/product-list/product-list.component';

export const routes: Routes = [
  { path: 'add-product', component: AddProductComponent },
  { path: 'product-list', component: ProductListComponent },
  { path: '', redirectTo: '/product-list', pathMatch: 'full' }
];
