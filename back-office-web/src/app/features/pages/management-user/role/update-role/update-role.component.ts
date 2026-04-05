import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import * as Notiflix from 'notiflix';

import { RoleService }            from '../../../../../core/services/roles/role.service';
import { RolePageResponse, RoleRequest } from '../../../../../core/models/roles/role.interface';
import { noWhitespaceValidator }  from '../../../../../shared/utils/validators.util';

export interface UpdateRoleDialogData {
  role: RolePageResponse;
}

@Component({
  selector: 'knv-update-role',
  templateUrl: './update-role.component.html',
  styleUrls: ['./update-role.component.scss'],
})
export class UpdateRoleComponent implements OnInit {

  form!: FormGroup;

  constructor(
    private dialogRef: MatDialogRef<UpdateRoleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: UpdateRoleDialogData,
    private roleService: RoleService,
  ) {}

  ngOnInit(): void {
    this.buildForm();
  }

  private buildForm(): void {
    const { name, description } = this.data.role;
    this.form = new FormGroup({
      name: new FormControl(name, [
        Validators.required,
        Validators.maxLength(60),
        noWhitespaceValidator(),
      ]),
      description: new FormControl(description, [
        Validators.required,
        Validators.maxLength(255),
        noWhitespaceValidator(),
      ]),
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const body: RoleRequest = {
      name: this.form.value.name.trim(),
      description: this.form.value.description.trim(),
    };

    Notiflix.Loading.pulse('Actualizando...');

    this.roleService.update(this.data.role.id, body).subscribe({
      next: () => {
        Notiflix.Loading.remove(300);
        Notiflix.Report.success(
          'Operación Exitosa',
          'El rol fue actualizado exitosamente.',
          'OK',
        );
        this.dialogRef.close(true);
      },
      error: (err) => {
        Notiflix.Loading.remove(300);
        Notiflix.Report.failure(
          'Error',
          err?.error?.message ?? 'Ocurrió un error al actualizar el rol.',
          'OK',
        );
      },
    });
  }

  close(): void {
    this.dialogRef.close(false);
  }
}

