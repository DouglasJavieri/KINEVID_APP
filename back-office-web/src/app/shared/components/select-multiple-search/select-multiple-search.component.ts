// import { Component, Input, OnInit } from "@angular/core";
// import { FormGroup, FormGroupDirective } from "@angular/forms";
// import { SelectMultipleSearchItem } from "./select-multiple-search.model";
//
// @Component({
//   selector: 'knv-select-multiple-search',
//   templateUrl: './select-multiple-search.component.html',
//   styleUrls: ['./select-multiple-search.component.scss']
// })
// export class SelectMultipleSearchComponent implements OnInit {
//
//   field: SelectMultipleSearchItem;
//   @Input() data: SelectMultipleSearchItem;
//   formGroup: FormGroup;
//
//   ngOnInit(): void {
//     this.field = this.data;
//     const propValueToMark = this.field.selectOptions.propValue;
//     const defaultValue = this.field.default;
//     this.field.selectOptions.dataList = this.field.selectOptions.dataList.map(objeto => {
//       if (defaultValue && defaultValue.includes(objeto[propValueToMark])){
//         return { ...objeto, marked: true, show: true };
//       }else {
//         return { ...objeto, marked: false, show: true };
//       }
//     });
//     // if (defaultValue){
//     //   this.ordenarLista(this.field);
//     // }
//     this.formGroup = this.rootFormGroup.control;
//   }
//
//   constructor(private rootFormGroup: FormGroupDirective) {
//
//   }
//
//
//   selectOpened(focusName: string) {
//     if (focusName) {
//       const searchInp = document.getElementById(focusName);
//       searchInp.focus();
//     }
//   }
//
//
//   //Cada opcion del select multiple al marcarse va al principio de la lista
//   marcarDesmarcarOpcion(option: any, _field?: SelectMultipleSearchItem) {
//     option.marked = !option.marked;
//     // this.ordenarLista(this.field);
//   }
//
//   selectSearch(value: any, _field?: SelectMultipleSearchItem) {
//
//     this.field.selectOptions.dataList.map(item => {
//       const itemNameLower = item.name.toLowerCase();
//       const valueLower = value.toLowerCase();
//       itemNameLower.includes(valueLower) ? item.show = true : item.show = false;
//     });
//   }
//
//
//   //Ordena un select multiple con las opciones marcadas arriba
//   ordenarLista(field: SelectMultipleSearchItem) {
//       setTimeout(() => {
//       field.selectOptions.dataList.sort((a, b) => {
//         if (a.marked && !b.marked) {
//           return -1;  // Colocar a antes que b
//         } else if (!a.marked && b.marked) {
//           return 1;   // Colocar b antes que a
//         } else {
//           return 0;   // No realizar cambios en el orden
//         }
//       });
//     }, 400);
//   }
// }
