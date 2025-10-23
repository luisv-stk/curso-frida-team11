import { Component, ViewEncapsulation, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { Subscription, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import type { Product } from '../../../models/product.model';
import { ProductService } from '../../../services/product.service';
import { EditProductModalComponent } from '../edit-product-modal/edit-product-modal.component';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    FormsModule,
    MatButtonModule,
    MatCardModule,
    MatTooltipModule,
    MatDialogModule,
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss',
  encapsulation: ViewEncapsulation.None,
})
export class ProductListComponent implements OnInit, OnDestroy {
  private productService = inject(ProductService);
  private dialog = inject(MatDialog);
  private subscription?: Subscription;
  private searchSubscription?: Subscription;
  private searchSubject = new Subject<string>();

  displayedColumns: string[] = [
    'referencia',
    'nombre',
    'marca',
    'descripcion',
    'precio',
    'numeroDisponible',
    'departamento',
    'actions',
  ];
  dataSource: Product[] = [];
  allProducts: Product[] = [];
  totalProducts = '0';
  private _searchText = '';

  get searchText(): string {
    return this._searchText;
  }

  set searchText(value: string) {
    this._searchText = value;
    this.searchSubject.next(value);
  }

  ngOnInit(): void {
    this.loadProducts();
    this.setupSearch();
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
    this.searchSubscription?.unsubscribe();
  }

  private setupSearch(): void {
    // Set up debounced search with 300ms delay
    this.searchSubscription = this.searchSubject
      .pipe(
        debounceTime(300),
        distinctUntilChanged()
      )
      .subscribe(searchTerm => {
        this.performSearch(searchTerm);
      });
  }

  private performSearch(searchTerm: string): void {
    if (!searchTerm.trim()) {
      // If search is empty, show all products
      this.dataSource = this.allProducts;
    } else {
      // Normalize search term: remove extra spaces, convert to lowercase
      const searchLower = searchTerm.toLowerCase().trim().replace(/\s+/g, ' ');
      
      this.dataSource = this.allProducts.filter(product => {
        // Create searchable text from all relevant fields
        const searchableText = [
          product.referencia,
          product.nombre,
          product.marca,
          product.descripcion,
          product.departamento,
          product.precio.toString(),
          product.numeroDisponible.toString()
        ].join(' ').toLowerCase();

        // Support partial matching and multiple words
        return searchLower.split(' ').every(term => 
          searchableText.includes(term)
        );
      });
    }
    
    // Update the total products counter with filtered results
    this.totalProducts = this.dataSource.length.toLocaleString('es-ES');
  }

  refreshProducts(): void {
    // Since we're using reactive state, no need to manually reload
    // The subscription will automatically receive updates
    console.log('Products refreshed automatically via reactive state');
  }

  clearSearch(): void {
    this.searchText = '';
  }

  getProductCountText(): string {
    if (!this.searchText.trim()) {
      // No search active - show total products
      return `${this.totalProducts} productos cargados`;
    } else {
      // Search active - show filtered vs total
      const filteredCount = this.dataSource.length;
      const totalCount = this.allProducts.length;
      
      if (filteredCount === 0) {
        return `0 de ${totalCount.toLocaleString('es-ES')} productos`;
      } else if (filteredCount === totalCount) {
        return `${filteredCount.toLocaleString('es-ES')} productos (todos coinciden)`;
      } else {
        return `${filteredCount.toLocaleString('es-ES')} de ${totalCount.toLocaleString('es-ES')} productos`;
      }
    }
  }

  editProduct(product: Product): void {
    const dialogRef = this.dialog.open(EditProductModalComponent, {
      width: '600px',
      data: { product: { ...product } }, // Create a copy to avoid direct mutation
      disableClose: true, // Prevent closing by clicking outside
      autoFocus: true
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // User clicked Save - update the product
        this.productService.updateProduct(result);
        console.log(`Product updated: ${result.referencia} - ${result.nombre}`);
      } else {
        // User clicked Cancel or closed modal - no action needed
        console.log('Product edit cancelled');
      }
    });
  }

  deleteProduct(product: Product): void {
    this.productService.deleteProduct(product);
    console.log(`Product deleted: ${product.referencia} - ${product.nombre}`);
  }

  private loadProducts(): void {
    // Subscribe to the reactive product stream
    this.subscription = this.productService.getProducts().subscribe({
      next: (products) => {
        this.allProducts = products;
        this.performSearch(this.searchText); // Apply current search filter
        console.log(`Product list updated: ${products.length} total products, ${this.dataSource.length} visible`);
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.allProducts = [];
        this.dataSource = [];
        this.totalProducts = '0';
      }
    });
  }
}
