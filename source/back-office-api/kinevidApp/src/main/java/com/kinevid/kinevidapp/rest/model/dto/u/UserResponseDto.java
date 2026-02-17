package com.kinevid.kinevidapp.rest.model.dto.u;

import com.kinevid.kinevidapp.rest.model.entity.auth.User;
import com.kinevid.kinevidapp.rest.model.enums.auth.UserStatus;
import lombok.*;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 16/02/2026
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private UserStatus status;

    public UserResponseDto (User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.status = user.getStatus();
    }
}
