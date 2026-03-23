import {Component} from '@angular/core';

@Component({
  selector: 'knv-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.scss']
})
export class MainLayoutComponent {
  sidebarCollapsed = false;

  user = {
    id: 'demo',
    nombre: 'Usuario Demo',
    email: 'demo@kinevid.local'
  };

  onToggleSidebar(): void {
    this.sidebarCollapsed = !this.sidebarCollapsed;
  }

  onLogout(): void {
    // Demo por ahora
    console.log('Logout (demo)');
  }

}
