package com.kinevid.kinevidapp.rest.model.dto.rp;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionRequestDto {
    private Long roleId;
    private Long permissionId;
}