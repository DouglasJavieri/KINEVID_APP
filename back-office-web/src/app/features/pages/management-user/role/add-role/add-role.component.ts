import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import * as Notiflix from 'notiflix';

import { RoleService }            from '../../../../../core/services/roles/role.service';
import { RoleRequest }            from '../../../../../core/models/roles/role.interface';
import { noWhitespaceValidator }  from '../../../../../shared/utils/validators.util';

@Component({
  selector: 'knv-add-role',
  templateUrl: './add-role.component.html',
  styleUrls: ['./add-role.component.scss'],
})
export class AddRoleComponent implements OnInit {

  form!: FormGroup;

  constructor(
    private dialogRef: MatDialogRef<AddRoleComponent>,
    private roleService: RoleService,
  ) {}

  ngOnInit(): void {
    this.buildForm();
  }

  private buildForm(): void {
    this.form = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.maxLength(60),
        noWhitespaceValidator(),
      ]),
      description: new FormControl('', [
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

    Notiflix.Loading.pulse('Guardando...');

    this.roleService.create(body).subscribe({
      next: () => {
        Notiflix.Loading.remove(300);
        Notiflix.Report.success(
          'Operación Exitosa',
          'El rol fue creado exitosamente.',
          'OK',
        );
        this.dialogRef.close(true);
      },
      error: (err) => {
        Notiflix.Loading.remove(300);
        Notiflix.Report.failure(
          'Error',
          err?.error?.message ?? 'Ocurrió un error al crear el rol.',
          'OK',
        );
      },
    });
  }

  close(): void {
    this.dialogRef.close(false);
  }
}

