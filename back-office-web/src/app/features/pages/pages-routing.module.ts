import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { MainLayoutComponent } from '../../layout/main-layout/main-layout.component';
import { HomeComponent }       from './home/home.component';
import { RoleGuard }           from '../../core/guards/role.guard';
import { AppRole }             from '../../core/models/auth.model';

/**
 * Rutas internas de la aplicación (bajo AuthGuard del AppRoutingModule).
 *
 * Patrón de uso de RoleGuard:
 * ──────────────────────────────────────────────────────────────────────
 * {
 *   path: 'ruta-restringida',
 *   canActivate: [RoleGuard],
 *   data: { roles: [AppRole.ADMIN] },
 *   component: MiComponente
 * }
 * ──────────────────────────────────────────────────────────────────────
 * AuthGuard (en AppRoutingModule) garantiza que el usuario está autenticado.
 * RoleGuard (aquí) garantiza que tiene el rol necesario.
 * Siempre van en ese orden.
 */
const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [

      // Ruta pública para todos los usuarios autenticados
      { path: 'home', component: HomeComponent },

      // ── Módulo de Usuarios (solo ADMIN) ──────────────────────────────
      // Descomentar cuando el UserModule esté disponible:
      // {
      //   path: 'usuarios',
      //   canActivate: [RoleGuard],
      //   data: { roles: [AppRole.ADMIN] },
      //   loadChildren: () => import('./user/user.module').then(m => m.UserModule)
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
