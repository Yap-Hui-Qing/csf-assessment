import { NumberSymbol } from "@angular/common"

// You may use this file to create any models
export interface Menu {
    id: string,
    name: string,
    description: string,
    price: number,
    quantity: number
}

export interface Cart {
    menu: Menu[],
    // total: number
}

export interface Item {
    id: string,
    price: number,
    quantity: number
}

export interface Order {
    username: string,
    password: string,
    items: Item[]
}