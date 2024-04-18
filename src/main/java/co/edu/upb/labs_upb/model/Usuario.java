package co.edu.upb.labs_upb.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults( level = AccessLevel.PRIVATE)
public class Usuario implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column( name = "id_usuario", nullable = false)
    Long id;

    @Column( name = "id_upb", nullable = false, unique = true)
    Long idUpb;

    @Column( name = "documento_identificacion", nullable = false, unique = true)
    Long documento;

    @Column( nullable = false, length = 100)
    String nombre;

    Boolean enable;

    @Column(length = 100)
    String apellido;

    @Column( nullable = false, unique = true)
    String email;

    @Column( name = "password")
    String password;

    /**
     * This annotation defines a many-to-many relationship between a user entity and its associated roles.
     * The `Set<Rol>` type ensures uniqueness of roles for a user and avoids duplicates.
     *
     * FetchType.EAGER is used to fetch the associated roles along with the user entity in a single database query.
     * This can improve performance in scenarios where roles are frequently accessed together,
     * but it might lead to larger result sets if roles are not always needed. Consider using FetchType.LAZY
     * for performance optimization if roles are accessed conditionally.
     *
     * @ManyToMany specifies a many-to-many relationship between the user entity and the Rol entity.
     * @fetch defines the fetching strategy for the associated roles.
     *   - FetchType.EAGER: Fetches roles along with the user in a single query (potentially impacting performance).
     *   - FetchType.LAZY (default): Fetches roles only when explicitly accessed, improving performance but requiring additional queries.
     * @JoinTable defines the join table (roles_usuarios in this case) that maps the many-to-many relationship.
     *   - name: The name of the join table.
     *   - joinColumns: Defines the column(s) in the join table that reference the user entity.
     *     - id_usuarios: In this example, it references the user's ID.
     *   - inverseJoinColumns: Defines the column(s) in the join table that reference the Rol entity.
     *     - id_roles: In this example, it references the role's ID.
     */
    @ManyToMany( fetch = FetchType.EAGER)
    @JoinTable(name = "roles_usuarios",
            joinColumns = {@JoinColumn(name = "id_usuarios")},
            inverseJoinColumns = {@JoinColumn(name = "id_roles")})
    Set<Rol> roles;


    @Column(name = "fecha_creacion", nullable = false)
    LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    LocalDateTime fechaActualizacion;



    /**
     * This method overrides the `getAuthorities()` method from the `UserDetails` interface.
     * It retrieves a collection of `GrantedAuthority` objects representing the user's authorities.
     *
     * @return A collection of `GrantedAuthority` objects that represent the user's authorities.
     *         These authorities are derived by converting each role name from the user's associated roles
     *         into a `SimpleGrantedAuthority` object.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(rol -> {
            authorities.add(new SimpleGrantedAuthority(rol.getNombre()));
        });
        return authorities;
    }

    /**
     * Returns the username used for authentication.
     * @return a string representing the username
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }
}
