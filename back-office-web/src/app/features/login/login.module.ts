import {NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';

import {LoginRoutingModule} from "./login-routing.module";

import {LoginComponent} from "./login.component";
import {MatInputModule} from "@angular/material/input";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatCardModule} from "@angular/material/card";

@NgModule({
  declarations: [LoginComponent],
  imports: [
    LoginRoutingModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    CommonModule,
  ],
  exports: [LoginComponent],
  providers: [],
})
export class LoginModule {
}
