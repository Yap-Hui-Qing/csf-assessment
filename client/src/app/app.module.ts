import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { provideHttpClient } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { MenuComponent } from './components/menu.component';
import { PlaceOrderComponent } from './components/place-order.component';

import { ConfirmationComponent } from './components/confirmation.component';
import { RouterModule, Routes } from '@angular/router';
import { RestaurantService } from './restaurant.service';
import { RestaurantStore } from './restaurant.store';

const appRoutes: Routes = [
  {path: '', component:MenuComponent},
  {path: 'placeOrder', component: PlaceOrderComponent},
  {path: 'confirmation', component: ConfirmationComponent},
  {path: '**', redirectTo: '/', pathMatch: 'full'}
]

@NgModule({
  declarations: [
    AppComponent, MenuComponent, PlaceOrderComponent, ConfirmationComponent
  ],
  imports: [
    BrowserModule, ReactiveFormsModule, RouterModule.forRoot(appRoutes)
  ],
  providers: [ provideHttpClient(), RestaurantService, RestaurantStore ],
  bootstrap: [AppComponent]
})
export class AppModule { }
