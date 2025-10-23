import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import type { Product } from '../models/product.model';
import { MOCK_PRODUCTS } from '../mocks/product.mock';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private productsSubject = new BehaviorSubject<Product[]>(MOCK_PRODUCTS);
  public products$ = this.productsSubject.asObservable();

  constructor() { }

  /**
   * Returns an observable with the list of available products
   * @returns Observable<Product[]> - Stream of products with reactive updates
   */
  getProducts(): Observable<Product[]> {
    return this.products$;
  }

  /**
   * Adds a new product to the list and triggers reactive updates
   * @param product - The product to add to the list
   */
  addProduct(product: Product): void {
    const currentProducts = this.productsSubject.getValue();
    const updatedProducts = [...currentProducts, product];
    this.productsSubject.next(updatedProducts);
  }

  /**
   * Returns the current number of products
   * @returns number - Current product count
   */
  getProductCount(): number {
    return this.productsSubject.getValue().length;
  }

  /**
   * Updates an existing product in the list and triggers reactive updates
   * @param updatedProduct - The product with updated information
   */
  updateProduct(updatedProduct: Product): void {
    const currentProducts = this.productsSubject.getValue();
    const updatedProducts = currentProducts.map(p => 
      p.referencia === updatedProduct.referencia ? updatedProduct : p
    );
    this.productsSubject.next(updatedProducts);
  }

  /**
   * Removes a product from the list by its reference and triggers reactive updates
   * @param product - The product to remove from the list
   */
  deleteProduct(product: Product): void {
    const currentProducts = this.productsSubject.getValue();
    const updatedProducts = currentProducts.filter(p => p.referencia !== product.referencia);
    this.productsSubject.next(updatedProducts);
  }
}
