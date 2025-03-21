import { firstValueFrom, Subject } from "rxjs";
import { Cart, Menu, Order, Receipt } from "./models";
import { inject, Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";

@Injectable()
export class RestaurantService {

  private httpClient = inject(HttpClient)

  private cart!: Cart

  receipt = new Subject<any>

  r !: Receipt

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
      this.httpClient.post<any>('api/food_order', o, { headers })
    ).then(result => {
      console.info('>>> ', result)
      const r : Receipt = {
        timestamp: result.timestamp,
        orderId: result.orderId,
        paymentId: result.paymentId,
        total: result.total
      }
      this.setReceipt(r)
      console.info(result.timestamp)
      this.receipt.next(result)
      console.info(this.receipt)
    }
    )
  }

  setReceipt(receipt: Receipt){
    this.r = receipt
  }

  getReceipt(): Receipt{
    return this.r
  }
}
