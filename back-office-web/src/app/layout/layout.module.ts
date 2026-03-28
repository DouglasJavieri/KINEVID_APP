import {NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import {SidebarComponent} from "./components/sidebar/sidebar.component";
import {NavbarComponent} from "./components/navbar/navbar.component";
import {MainLayoutComponent} from "./main-layout/main-layout.component";
import {MatIconModule} from "@angular/material/icon";

@NgModule({
  declarations: [
    SidebarComponent,
    NavbarComponent,
    MainLayoutComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    MatIconModule
  ],
  exports: [
    SidebarComponent,
    NavbarComponent,
    MainLayoutComponent
  ],
  providers: [],
})
export class LayoutModule {
}
