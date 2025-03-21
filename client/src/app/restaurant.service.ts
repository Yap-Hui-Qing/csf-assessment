import { firstValueFrom } from "rxjs";
import { Cart, Menu, Order } from "./models";
import { inject, Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";

@Injectable()
export class RestaurantService {

  private httpClient = inject(HttpClient)

  private cart!: Cart

  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems(): Promise<any> {
    return firstValueFrom(this.httpClient.get('api/menu'))
  }

  // TODO: Task 3.2
  postOrder(o: Order): Promise<any>{
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json')
      .set('Accept', 'application/json')
    return firstValueFrom(
      this.httpClient.post<any>('api/food_order',o, { headers })
    )
  }
}
