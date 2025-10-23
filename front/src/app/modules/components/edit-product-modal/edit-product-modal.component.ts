import { Component, Inject, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import type { Product } from '../../../models/product.model';

@Component({
  selector: 'app-edit-product-modal',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './edit-product-modal.component.html',
  styleUrl: './edit-product-modal.component.scss'
})
export class EditProductModalComponent {
  private fb = inject(FormBuilder);
  
  editForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<EditProductModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { product: Product }
  ) {
    this.editForm = this.fb.group({
      referencia: [data.product.referencia, [Validators.required, Validators.minLength(3)]],
      nombre: [data.product.nombre, [Validators.required, Validators.minLength(2)]],
      marca: [data.product.marca, [Validators.required, Validators.minLength(2)]],
      descripcion: [data.product.descripcion, [Validators.required, Validators.minLength(5)]],
      precio: [data.product.precio, [Validators.required, Validators.pattern(/^\d+([.,]\d{1,2})?$/)]],
      numeroDisponible: [data.product.numeroDisponible, [Validators.required, Validators.min(0)]],
      departamento: [data.product.departamento, [Validators.required, Validators.minLength(2)]]
    });
  }

  onSave(): void {
    if (this.editForm.valid) {
      const updatedProduct: Product = {
        ...this.data.product,
        ...this.editForm.value
      };
      this.dialogRef.close(updatedProduct);
    } else {
      // Mark all fields as touched to show validation errors
      Object.keys(this.editForm.controls).forEach(key => {
        this.editForm.get(key)?.markAsTouched();
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  getFieldError(fieldName: string): string {
    const field = this.editForm.get(fieldName);
    if (field?.errors && field.touched) {
      if (field.errors['required']) {
        return `El campo ${fieldName} es obligatorio`;
      }
      if (field.errors['minlength']) {
        return `El campo ${fieldName} debe tener al menos ${field.errors['minlength'].requiredLength} caracteres`;
      }
      if (field.errors['min']) {
        return `El valor debe ser mayor o igual a ${field.errors['min'].min}`;
      }
      if (field.errors['pattern']) {
        return 'El formato del precio no es válido (ej: 12,50 o 12.50)';
      }
    }
    return '';
  }
}
