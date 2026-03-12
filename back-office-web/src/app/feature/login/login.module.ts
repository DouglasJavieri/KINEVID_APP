import {NgModule} from '@angular/core';

import {LoginComponent} from "./login.component";
import {MatInputModule} from "@angular/material/input";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatCardModule} from "@angular/material/card";

@NgModule({
  declarations: [LoginComponent],
  imports: [
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule
  ],
  exports: [LoginComponent],
  providers: [],
})
export class LoginModule {
}
