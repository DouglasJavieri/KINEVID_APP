import {NgModule} from '@angular/core';
import {CommonModule} from "@angular/common";

import {PagesRoutingModule} from "./pages-routing.module";

import {HomeModule} from "./home/home.module";
import {LayoutModule} from "../../layout/layout.module";



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    LayoutModule,
    PagesRoutingModule,
    HomeModule,
  ],
  exports: [],
  providers: [],
})
export class PagesModule {
}
