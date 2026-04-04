import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MenuItem } from '../../../shared/models/menu.interface';

@Component({
  selector: 'knv-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['sidebar.component.scss']
})
export class SidebarComponent {
  @Input() isOpen = true;
  @Output() toggleSidebar = new EventEmitter<void>();

  expandedItems: Set<string> = new Set();

  menuItems: MenuItem[] = [
    {
      id: 'home',
      label: 'Home',
      icon: 'dashboard',
      route: '/home',
      active: true
    },
    {
      id: 'management-users',
      label: 'Gestión de Usuarios',
      icon: 'admin_panel_settings',
      route: null,
      children: [
        {
          id: 'users',
          label: 'Usuarios',
          icon: 'person',
          route: '/management-users/users'
        },
        {
          id: 'roles',
          label: 'Roles',
          icon: 'security',
          route: '/management-users/roles'
        },
        {
          id: 'permissions',
          label: 'Permisos',
          icon: 'lock',
          route: '/management-users/permissions'
        }
      ]
    },
    {
      id: 'citas',
      label: 'Citas',
      icon: 'assessment',
      route: '/citas'
    },
    {
      id: 'hce',
      label: 'Historia Clínica',
      icon: 'settings',
      route: '/historia-clinica'
    }
  ];

  onToggle(): void {
    this.toggleSidebar.emit();
  }

  onMenuClick(item: MenuItem): void {
    // Si el item tiene hijos, expandir/contraer en lugar de navegar
    if (item.children && item.children.length > 0) {
      this.toggleExpanded(item.id!);
    } else {
      // Si no tiene hijos, marcar como activo y navegar
      this.setActiveItem(item);
    }
  }

  onChildClick(parent: MenuItem, child: MenuItem): void {
    // Marcar como activo el hijo y el padre
    this.menuItems.forEach(m => this.deactivateRecursive(m));
    this.activateItem(parent);
    this.activateItem(child);
  }

  toggleExpanded(itemId: string): void {
    if (this.expandedItems.has(itemId)) {
      this.expandedItems.delete(itemId);
    } else {
      this.expandedItems.add(itemId);
    }
  }

  isExpanded(itemId: string): boolean {
    return this.expandedItems.has(itemId);
  }

  setActiveItem(item: MenuItem): void {
    this.menuItems.forEach(m => this.deactivateRecursive(m));
    this.activateItem(item);
  }

  private activateItem(item: MenuItem): void {
    item.active = true;
  }

  private deactivateRecursive(item: MenuItem): void {
    item.active = false;
    if (item.children) {
      item.children.forEach(child => this.deactivateRecursive(child));
    }
  }

  hasChildren(item: MenuItem): boolean {
    return item.children !== undefined && item.children.length > 0;
  }
}
