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

      // Ruta pública para todos los usuarios autenticados
      { path: 'home', component: HomeComponent },
      {
        path: 'permisos',
        canActivate: [RoleGuard],
        data: { roles: [AppRole.ADMIN] },
        loadChildren: () =>
          import('./management-user/permission/permission.module').then(m => m.PermissionModule),
      },

      // Roles del sistema (solo ADMIN) — pendiente de implementación
      // {
      //   path: 'roles',
      //   canActivate: [RoleGuard],
      //   data: { roles: [AppRole.ADMIN] },
      //   loadChildren: () => import('./management-usr/role/role.module').then(m => m.RoleModule),
      // },

      // Usuarios del sistema (solo ADMIN) — pendiente de implementación
      // {
      //   path: 'usuarios',
      //   canActivate: [RoleGuard],
      //   data: { roles: [AppRole.ADMIN] },
      //   loadChildren: () => import('./management-usr/user/user.module').then(m => m.UserModule),
      // },

      // ── Módulo de Citas (ADMIN + futuros roles) ───────────────────────
      // {
      //   path: 'citas',
      //   canActivate: [RoleGuard],
      //   data: { roles: [AppRole.ADMIN] },
      //   loadChildren: () => import('./citas/citas.module').then(m => m.CitasModule)
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
