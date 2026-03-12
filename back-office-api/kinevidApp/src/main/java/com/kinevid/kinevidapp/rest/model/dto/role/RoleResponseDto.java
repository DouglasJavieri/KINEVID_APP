package com.kinevid.kinevidapp.rest.model.dto.role;

import com.kinevid.kinevidapp.rest.model.entity.role.Role;
import lombok.*;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 02/03/2026
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDto {
    private String name;
    private String description;
    private String status;

    public RoleResponseDto(Role role) {
        this.name = role.getName();
        this.description = role.getDescription();
        this.status = role.getStatus().getValue();
    }
}
