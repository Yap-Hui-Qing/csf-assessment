import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { RestaurantStore } from '../restaurant.store';
import { Item, Menu, Order, Receipt } from '../models';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { isValidDate } from 'rxjs/internal/util/isDate';
import { Router } from '@angular/router';
import { RestaurantService } from '../restaurant.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit {

  private restaurantStore = inject(RestaurantStore)
  private restaurantSvc = inject(RestaurantService)
  private fb = inject(FormBuilder)
  private router = inject(Router)

  protected order!: FormGroup
  protected cart!: Menu[]
  protected total: number = 0
  protected items: Item[] = []

  // TODO: Task 3
  ngOnInit(): void {

    // get selected items from view 1
    this.restaurantStore.getCart.subscribe(
      (result) => {
        this.cart = result
        this.total = this.cart.reduce((acc, c) => acc + (c.price * c.quantity), 0)
      }
    )

    for (let c of this.cart){
      const item: Item = {
        id: c.id,
        price: c.price,
        quantity: c.quantity
      }
      this.items = [...this.items, item]
    }
    
    this.order = this.createOrder();
  }

  protected processOrder(){
    const order: Order = {
      username: this.order.value.username,
      password: this.order.value.password,
      items: this.items
    }
    console.info('>>> order: ', order)
    this.restaurantSvc.postOrder(order).then(
      (result) => {
        console.info('>>> payment receipt: ', result)
        this.router.navigate(['/confirmation'])
      }
    )
    .catch(
      (err) => {
        const errorMessage = err.error?.message
        alert(errorMessage)
        return
      }
    )
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
