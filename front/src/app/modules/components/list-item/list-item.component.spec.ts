import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListItemComponent } from './list-item.component';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';

describe('ListItemComponent', () => {
  let component: ListItemComponent;
  let fixture: ComponentFixture<ListItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListItemComponent, MatIconModule, MatButtonModule, MatListModule]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});
