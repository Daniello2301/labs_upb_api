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

    @ManyToMany( fetch = FetchType.EAGER)
    @JoinTable(name = "roles_usuarios",
            joinColumns = {@JoinColumn(name = "id_usuarios")},
            inverseJoinColumns = {@JoinColumn(name = "id_roles")})
    Set<Rol> roles;


//    @OneToMany( fetch = FetchType.LAZY, mappedBy = "usuario")
//    Set<Activo> activos;


    @Column(name = "fecha_creacion", nullable = false)
    LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    LocalDateTime fechaActualizacion;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(rol -> {
            authorities.add(new SimpleGrantedAuthority(rol.getNombre()));
        });
        return authorities;
    }

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
