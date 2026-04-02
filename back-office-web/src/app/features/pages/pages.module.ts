import { NgModule } from '@angular/core';

import { PagesRoutingModule } from './pages-routing.module';
import { HomeModule } from './home/home.module';
import { LayoutModule } from '../../layout/layout.module';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [],
  imports: [
    SharedModule,
    PagesRoutingModule,
    HomeModule,
  ],
  exports: [],
  providers: [],
})
export class PagesModule {}
