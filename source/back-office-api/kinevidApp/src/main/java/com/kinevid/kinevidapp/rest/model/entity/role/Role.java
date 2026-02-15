package com.kinevid.kinevidapp.rest.model.entity.role;

import com.kinevid.kinevidapp.rest.model.base.AuditableEntity;
import com.kinevid.kinevidapp.rest.model.enums.role.RoleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 15/02/2026
 */

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Role")
public class Role extends AuditableEntity implements Serializable {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "SEQ_ROLE_ID_GENERATOR", sequenceName = "SEQ_ROLE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ROLE_ID_GENERATOR")
    private Long id;

    @Basic
    @Column(name = "name_role", length = 75)
    private String name;

    @Basic
    @Column(name = "description", length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_status")
    private RoleStatus status;
}
