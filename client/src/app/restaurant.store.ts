import { Injectable } from "@angular/core";
import { ComponentStore } from "@ngrx/component-store";
import { Cart, Menu } from "./models";

const INIT: Cart = {
    menu: [],
    // total: 0
}

@Injectable()
export class RestaurantStore extends ComponentStore<Cart> {

    constructor() { super(INIT) }

    // updators/mutatators
    // addMenu(newMenu)
    readonly addMenu = this.updater<Menu>(
        (cart: Cart, newMenu: Menu) => {
            return {
                menu: [...cart.menu, newMenu]
            }
        }
    )    

    readonly clearCart = this.updater(
        (cart: Cart) => {
            return {
                ...cart,
                menu: []
            }
        }
    )

    // selectors
    readonly countItems = this.select<number>(
        (cart: Cart) => {
            const name = cart.menu.map(m => m.name)
            return name.length
        }
    )

    readonly getCart = this.select<Menu[]>(
        (cart: Cart) => cart.menu
    )


}