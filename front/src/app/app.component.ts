import { Component, ViewChild, ElementRef, AfterViewInit, ViewContainerRef } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Injector } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { HeaderComponent } from './modules/components/header/header.component';
import { RouterModule } from '@angular/router';
import { ListItemComponent } from './modules/components/list-item/list-item.component';
import { AddProductComponent } from './modules/components/add-product/add-product.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HeaderComponent, MatButtonModule, RouterModule, ListItemComponent, AddProductComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {






}
