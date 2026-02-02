package com.kinevid.kinevidapp.rest.model.entity.auth;

import com.kinevid.kinevidapp.rest.model.base.AuditableEntity;
import com.kinevid.kinevidapp.rest.model.enums.auth.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 01/02/2026
 */
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
public class User extends AuditableEntity implements Serializable {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "SEQ_USER_ID_GENERATOR", sequenceName = "SEQ_USER_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER_ID_GENERATOR")
    private Long id;

    @Basic
    @Column(name = "username", nullable = false, length = 30, unique = true)
    private String username;

    @Basic
    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @Basic
    @Column(name = "lastname", nullable = false, length = 60)
    private String lastname;

    @Basic
    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Basic
    @Column(name = "password", nullable = false, length = 30)
    private String password;

    @Basic
    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "state_user", nullable = false, length = 30)
    private UserStatus status;
}
