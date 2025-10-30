import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject, of, throwError } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, retry } from 'rxjs/operators';
import type { Product } from '../models/product.model';
import { MOCK_PRODUCTS } from '../mocks/product.mock';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private productsSubject = new BehaviorSubject<Product[]>(MOCK_PRODUCTS);
  public products$ = this.productsSubject.asObservable();
  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

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

  /**
   * Analyzes an uploaded file by sending it to the analyze endpoint
   * Sends the file as multipart/form-data to the backend with proper error handling
   * @param file - The file to analyze
   * @returns Observable<any> - Response from the analyze endpoint
   */
  analyzeFile(file: File): Observable<any> {
    // Validate file before sending
    if (!file) {
      return throwError(() => new Error('No file provided for analysis'));
    }
    
    // Create FormData with proper multipart configuration
    const formData = new FormData();
    
    // Append the file with explicit parameters to ensure proper multipart handling
    // The key 'file' should match the backend endpoint parameter name
    formData.append('file', file, file.name);
    
    // Log FormData contents for debugging (optional, can be removed in production)
    console.log('Sending file for analysis:', {
      fileName: file.name,
      fileSize: file.size,
      fileType: file.type,
      endpoint: `${this.apiUrl}/analyze`
    });
    
    // Send request with explicit headers to ensure multipart/form-data
    // Note: Do NOT set Content-Type manually when using FormData
    // HttpClient automatically sets the correct boundary for multipart/form-data
    return this.http.post(`${this.apiUrl}/analyze`, formData, {
      // Let HttpClient handle Content-Type automatically for FormData
      // This ensures proper multipart/form-data with boundary
      reportProgress: true,
      observe: 'body'
    }).pipe(
      retry(1), // Retry once on failure
      catchError((error: HttpErrorResponse) => {
        console.error('Error analyzing file:', error);
        
        // Handle specific error types
        if (error.status === 0) {
          // Connection refused or CORS issue
          console.warn('Backend server is not available at', this.apiUrl);
          return throwError(() => new Error('Backend server is not available. Please check if the server is running.'));
        } else if (error.status >= 400 && error.status < 500) {
          // Client error - could be wrong endpoint or file format
          return throwError(() => new Error(`Client error: ${error.message || 'Invalid request or file format'}`));
        } else if (error.status >= 500) {
          // Server error
          return throwError(() => new Error(`Server error: ${error.message || 'Internal server error'}`));
        }
        
        // Generic error
        return throwError(() => error);
      })
    );
  }
}
