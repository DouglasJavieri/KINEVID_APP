import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { MainLayoutComponent } from '../../layout/main-layout/main-layout.component';
import { HomeComponent }       from './home/home.component';
import { RoleGuard }           from '../../core/guards/role.guard';
import { AppRole }             from '../../core/models/auth.model';

const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [

      // ── Home (all authenticated users) ───────────────────────────────
      { path: 'home', component: HomeComponent },

      // ── Management Users (ADMIN only) ────────────────────────────────
      // Sub-routes handled in ManagementUserRoutingModule:
      //   /management-users/permissions
      //   /management-users/roles     (future)
      //   /management-users/users     (future)
      {
        path: 'management-users',
        canActivate: [RoleGuard],
        data: { roles: [AppRole.ADMIN] },
        loadChildren: () =>
          import('./management-user/management-user.module').then(m => m.ManagementUserModule),
      },

      // ── Appointments module (future) ──────────────────────────────────
      // {
      //   path: 'appointments',
      //   canActivate: [RoleGuard],
      //   data: { roles: [AppRole.ADMIN] },
      //   loadChildren: () => import('./appointments/appointments.module').then(m => m.AppointmentsModule),
      // },

      // ── Clinical Records module (future) ──────────────────────────────
      // {
      //   path: 'clinical-records',
      //   canActivate: [RoleGuard],
      //   data: { roles: [AppRole.ADMIN] },
      //   loadChildren: () => import('./clinical-records/clinical-records.module').then(m => m.ClinicalRecordsModule),
      // },

      // ── Image Analysis module (future) ────────────────────────────────
      // {
      //   path: 'image-analysis',
      //   canActivate: [RoleGuard],
      //   data: { roles: [AppRole.ADMIN] },
      //   loadChildren: () => import('./image-analysis/image-analysis.module').then(m => m.ImageAnalysisModule),
      // },

      { path: '', redirectTo: 'home', pathMatch: 'full' },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule {}
