import { Component, inject, OnInit } from '@angular/core';
import { RestaurantStore } from '../restaurant.store';
import { Menu } from '../models';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { isValidDate } from 'rxjs/internal/util/isDate';
import { Router } from '@angular/router';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit{

  private restaurantStore = inject(RestaurantStore)
  private fb = inject(FormBuilder)
  private router = inject(Router)

  protected order!: FormGroup
  protected cart!: Menu[]
  protected total: number = 0

  // TODO: Task 3
  ngOnInit(): void {

    // get selected items from view 1
    this.restaurantStore.getCart.subscribe(
      (result) => {
        this.cart = result
        this.total = this.cart.reduce((acc, c) => acc + (c.price * c.quantity), 0)
      }
    )
    
    this.order = this.createOrder();
  }

  protected processOrder(){

  }

  protected clearCart(){
    this.restaurantStore.clearCart()
    this.router.navigate(['/'])
  }

  private createOrder(): FormGroup{
    return this.fb.group({
      username: this.fb.control<string>('', [ Validators.required ]),
      password: this.fb.control<string>('', [ Validators.required ])
    })
  }

}
