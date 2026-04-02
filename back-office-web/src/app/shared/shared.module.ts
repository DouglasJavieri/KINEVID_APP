import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HasRoleDirective } from './directives/has-role.directive';

/**
 * Módulo compartido de la aplicación.
 * Importarlo en cualquier módulo de feature que necesite:
 *   - La directiva *knvHasRole para control de visibilidad por rol.
 *   - CommonModule (NgIf, NgFor, etc.) de forma centralizada.
 *
 * USO:
 *   imports: [SharedModule]
 */
@NgModule({
  declarations: [
    HasRoleDirective
  ],
  imports: [
    CommonModule
  ],
  exports: [
    CommonModule,
    HasRoleDirective
  ]
})
export class SharedModule {}

