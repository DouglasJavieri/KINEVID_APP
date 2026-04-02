import {FormGroup, ValidatorFn} from '@angular/forms';
export type FieldValueChangeFn = (value: any, form: FormGroup, options?: any) => void;


export interface SelectMultipleSearchItem {
  type: any;
  name: string;
  label: string;
  default?: any;
  validators: ValidatorItem[];
  selectOptions: ISelectOptions;
  options?: any;
  flex?: number;
  disabled?: boolean;
  valueChangeFn?: FieldValueChangeFn;
  multiple?: boolean;
  id?: string;
}

export interface ValidatorItem {
  type: string;
  message: string;
  validator: ValidatorFn;
}

export interface ISelectOptions {
  dataList: any[];
  propValue: string;
  propShow: string;
  propMostrar?: string;
  dataListTmp?: any[];
}
