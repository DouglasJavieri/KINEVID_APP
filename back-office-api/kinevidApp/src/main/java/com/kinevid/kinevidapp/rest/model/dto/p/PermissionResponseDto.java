package com.kinevid.kinevidapp.rest.model.dto.p;

import com.kinevid.kinevidapp.rest.model.entity.p.Permission;
import lombok.*;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 03/03/2026
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponseDto {
    private String name;
    private String description;

    public PermissionResponseDto(Permission permission) {
        this.name = permission.getName();
        this.description = permission.getDescription();
    }
}
