import { Component, ViewEncapsulation, ElementRef, ViewChild, inject } from '@angular/core';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import type { Product } from '../../../models/product.model';
import { ProductService } from '../../../services/product.service';

@Component({
  selector: 'app-add-product',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule],
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AddProductComponent {
  @ViewChild('fileInput', { static: false }) fileInput!: ElementRef<HTMLInputElement>;

  // Service injection
  private productService = inject(ProductService);

  // File state properties
  selectedFile: File | null = null;
  isFileValid = false;
  errorMessage = '';
  isDragOver = false;

  // File validation constants
  private readonly maxFileSize = 2 * 1024 * 1024; // 2MB in bytes
  private readonly allowedTypes = ['image/jpeg', 'image/jpg', 'image/png'];
  private readonly allowedExtensions = ['.jpg', '.jpeg', '.png'];

  /**
   * Validates if the file meets the requirements (size and format)
   */
  private validateFile(file: File): { valid: boolean; message: string } {
    // Check file type
    if (!this.allowedTypes.includes(file.type)) {
      return { valid: false, message: 'Solo se permiten archivos .JPG o .PNG' };
    }

    // Check file extension as additional validation
    const fileExtension = file.name.toLowerCase().substring(file.name.lastIndexOf('.'));
    if (!this.allowedExtensions.includes(fileExtension)) {
      return { valid: false, message: 'Solo se permiten archivos .JPG o .PNG' };
    }

    // Check file size
    if (file.size > this.maxFileSize) {
      return { valid: false, message: 'El archivo no puede superar los 2 MB' };
    }

    return { valid: true, message: '' };
  }

  /**
   * Processes and validates the selected file
   */
  private processFile(file: File): void {
    const validation = this.validateFile(file);
    
    if (validation.valid) {
      this.selectedFile = file;
      this.isFileValid = true;
      this.errorMessage = '';
    } else {
      this.selectedFile = null;
      this.isFileValid = false;
      this.errorMessage = validation.message;
    }
  }

  /**
   * Creates a Product object from the uploaded file
   * Extracts available data and sets unavailable fields to "--"
   */
  private createProductFromFile(file: File): Product {
    // Generate a unique reference based on timestamp and file info
    const timestamp = new Date().getTime();
    const fileBaseName = file.name.substring(0, file.name.lastIndexOf('.'));
    
    // Extract what we can from the file information
    const product: Product = {
      referencia: `IMG-${timestamp}`,
      nombre: fileBaseName.replace(/[-_]/g, ' ').trim() || 'Producto sin nombre',
      marca: '--', // Not available from file
      descripcion: `Producto creado desde imagen: ${file.name}`,
      precio: '--', // Not available from file
      numeroDisponible: 0, // Default to 0 for new products
      departamento: '--' // Not available from file
    };

    return product;
  }

  /**
   * Triggers the hidden file input for manual file selection
   */
  onUploadAreaClick(): void {
    this.fileInput.nativeElement.click();
  }

  /**
   * Handles file selection from the input element
   */
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.processFile(input.files[0]);
    }
  }

  constructor(private router: Router) {}

  /**
   * Handles the save action - processes file and creates new product
   */
  onSave(): void {
    if (this.isFileValid && this.selectedFile) {
      try {
        // Create product from uploaded file
        const newProduct = this.createProductFromFile(this.selectedFile);
        
        // Add product to service (triggers reactive updates)
        this.productService.addProduct(newProduct);
        
        console.log('Product saved successfully:', newProduct);

        // Try to analyze the uploaded file (but continue regardless of result)
        this.productService.analyzeFile(this.selectedFile).subscribe({
          next: (analysisResult) => {
            console.log('File analysis completed:', analysisResult);
          },
          error: (error) => {
            console.error('Error analyzing file:', error);
            
            // Provide specific error messaging based on error type
            if (error.status === 0) {
              console.warn('Backend connection failed - continuing without analysis');
            } else if (error.status >= 400 && error.status < 500) {
              console.warn('Client error during analysis - continuing without analysis');
            } else if (error.status >= 500) {
              console.warn('Server error during analysis - continuing without analysis');
            } else {
              console.warn('Unknown error during analysis - continuing without analysis');
            }
            
            // Product was saved successfully despite analysis failure
            console.log('Product saved successfully despite analysis error:', newProduct);
          }
        });
        
        // Reset form state
        this.resetForm();
        
        // Navigate to product list to show updated data (always navigate)
        this.router.navigate(['/product-list']);
      } catch (error) {
        console.error('Error saving product:', error);
        this.errorMessage = 'Error al guardar el producto. Inténtalo de nuevo.';
        this.isFileValid = false;
      }
    }
  }

  /**
   * Resets the form to initial state
   */
  private resetForm(): void {
    this.selectedFile = null;
    this.isFileValid = false;
    this.errorMessage = '';
    this.isDragOver = false;
    
    // Reset file input
    if (this.fileInput) {
      this.fileInput.nativeElement.value = '';
    }
  }

  /**
   * Handles the delete action - clears the selected file
   */
  onDelete(): void {
    this.selectedFile = null;
    this.isFileValid = false;
    this.errorMessage = '';
    this.isDragOver = false;
    
    // Reset the file input
    if (this.fileInput) {
      this.fileInput.nativeElement.value = '';
    }
  }

  // Drag & Drop Event Handlers

  /**
   * Handles dragover event - prevents default behavior and sets drag effect
   */
  onDragOver(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    if (event.dataTransfer) {
      event.dataTransfer.dropEffect = 'copy';
    }
  }

  /**
   * Handles dragenter event - sets drag over state for visual feedback
   */
  onDragEnter(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragOver = true;
  }

  /**
   * Handles dragleave event - removes drag over state when leaving the drop zone
   */
  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    
    // Only remove drag over state if we're actually leaving the drop zone
    // Check if the related target is outside the drop zone
    const target = event.currentTarget as HTMLElement;
    const relatedTarget = event.relatedTarget as HTMLElement;
    
    if (!target.contains(relatedTarget)) {
      this.isDragOver = false;
    }
  }

  /**
   * Handles drop event - processes the dropped files
   */
  onDrop(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragOver = false;

    if (event.dataTransfer && event.dataTransfer.files.length > 0) {
      const file = event.dataTransfer.files[0];
      this.processFile(file);
      
      // Clear the dataTransfer
      event.dataTransfer.clearData();
    }
  }
}
