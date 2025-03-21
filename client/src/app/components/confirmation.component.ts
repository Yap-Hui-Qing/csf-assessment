import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { RestaurantService } from '../restaurant.service';
import { Receipt } from '../models';
import { Router } from '@angular/router';
import { RestaurantStore } from '../restaurant.store';

@Component({
  selector: 'app-confirmation',
  standalone: false,
  templateUrl: './confirmation.component.html',
  styleUrl: './confirmation.component.css'
})
export class ConfirmationComponent implements OnInit {

  // TODO: Task 5

  private restaurantSvc = inject(RestaurantService)
  private router = inject(Router)
  private restaurantStore = inject(RestaurantStore)

  protected receipt!: Receipt

  ngOnInit(): void {
    this.receipt = this.restaurantSvc.getReceipt();
    console.info(this.receipt);
  }

  protected goBack(){
    this.restaurantStore.clearCart()
    this.router.navigate(['/'])
  }

}
