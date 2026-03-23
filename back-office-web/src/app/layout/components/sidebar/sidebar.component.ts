import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MenuItem} from "../../../shared/models/menu.interface";

@Component({
  selector: 'knv-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['sidebar.component.scss']
})
export class SidebarComponent {
  @Input() isOpen = true;
  @Output() toggleSidebar = new EventEmitter<void>();

  menuItems: MenuItem[] = [
    { id: 'home', label: 'Home', icon: 'dashboard', route: '/kinevid/home', active: true },
    { id: 'usuarios', label: 'Usuarios', icon: 'people', route: '/kinevid/usuarios' },
    { id: 'citas', label: 'Citas', icon: 'assessment', route: '/kinevid/citas' },
    { id: 'hce', label: 'Historia Clínica', icon: 'settings', route: '/kinevid/hitoria-clinica' },
  ];

  onToggle(): void {
    this.toggleSidebar.emit();
  }

  onMenuClick(item: MenuItem): void {
    this.menuItems.forEach(m => (m.active = m.id === item.id));
  }


}
