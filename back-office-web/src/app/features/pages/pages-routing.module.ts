import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import {MainLayoutComponent} from "../../layout/main-layout/main-layout.component";
import {HomeComponent} from "./home/home.component";


const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: 'home', component: HomeComponent },
      { path: '', redirectTo: 'home', pathMatch: 'full' },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule {}
