import { Component, inject, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { Cart, Menu } from '../models';
import { RestaurantStore } from '../restaurant.store';
import { Router } from '@angular/router';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit {
  // TODO: Task 2

  private restaurantSvc = inject(RestaurantService)
  private restaurantStore = inject(RestaurantStore)
  private router = inject(Router)
  
  protected menu!: Menu[]
  protected totalQuantity: number = 0
  protected totalAmount: number = 0
  protected cart!: Cart

  ngOnInit(): void {
    this.restaurantSvc.getMenuItems().then(
      (result) => {
        console.info('>>> returned menu items: ', result)
        this.menu = result
        // initialise the quantity of each menu
        for (let m of this.menu){
          m.quantity = 0
        }
        console.info('>>> initialised quantity: ', this.menu)
      }
    )
  }

  protected addItem($event: Menu){
    $event.quantity += 1
    this.totalAmount += $event.price
    this.totalQuantity += 1
    console.info('>>> quantity: ', $event.quantity)
    console.info('>>> total amt: ', this.totalAmount)
    console.info('>>> total qty: ', this.totalQuantity)
  }

  protected deleteItem($event: Menu){
    $event.quantity -= 1
    this.totalAmount -= $event.price
    this.totalQuantity -= 1
    console.info('>>> quantity: ', $event.quantity)
    console.info('>>> total amt: ', this.totalAmount)
    console.info('>>> total qty: ', this.totalQuantity)
  }

  protected disableDelete($event: Menu): boolean{
    if ($event.quantity <= 0)
      return true
    return false
  }

  protected process(){
    // add menus to cart
    for (let m of this.menu){
      if (m.quantity >= 1){
        this.restaurantStore.addMenu(m)
      }
    }
    console.info('>>> cart: ', this.restaurantStore.getCart)
    this.router.navigate(['/placeOrder'])
  }

}
