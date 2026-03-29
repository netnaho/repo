import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, of } from 'rxjs';
import { OrderService } from '../../../core/services/order.service';
import { OrderSummaryModel } from '../../../core/models/order.models';
import { OrderStatusBadgeComponent } from '../shared/order-status-badge.component';

@Component({
  selector: 'app-review-orders',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, OrderStatusBadgeComponent],
  templateUrl: './review-orders.component.html',
  styleUrl: './review-orders.component.scss'
})
export class ReviewOrdersComponent implements OnInit {
  orders: OrderSummaryModel[] = [];
  loadError = '';

  constructor(private readonly orderService: OrderService, private readonly snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.refresh();
  }

  approve(orderId: number): void {
    this.orderService.approve(orderId, { decision: 'APPROVED', comments: 'Reviewed and approved.' }).subscribe({
      next: () => this.refresh(),
      error: () => this.snackBar.open('Order approval failed. Please retry.', 'Dismiss', { duration: 2600 })
    });
  }

  private refresh(): void {
    this.loadError = '';
    this.orderService.listOrders().pipe(
      catchError(() => {
        this.orders = [];
        this.loadError = 'Review queue could not be loaded right now.';
        return of([] as OrderSummaryModel[]);
      })
    ).subscribe({ next: (orders) => (this.orders = orders.filter((order) => order.status === 'UNDER_REVIEW')) });
  }
}
