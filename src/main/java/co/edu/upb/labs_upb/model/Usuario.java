package co.edu.upb.labs_upb.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Data
@FieldDefaults( level = AccessLevel.PRIVATE)
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "id_usuario", nullable = false)
    Long id;

    @Column( name = "documento_identificacion", nullable = false, unique = true)
    Long documento;

    @Column( nullable = false, length = 100)
    String nombre;

    @Column( nullable = true, length = 100)
    String apellido;

    @Column( nullable = false, unique = true)
    String correo;

    @Column( name = "password")
    String contrasena;

    @ManyToMany( fetch = FetchType.LAZY)
            @JoinTable(name = "roles_usuarios",
            joinColumns = {@JoinColumn(name = "id_usuarios")},
            inverseJoinColumns = {@JoinColumn(name = "id_roles")})
    Set<Rol> roles;


    @Column(name = "fecha_creacion", nullable = false)
    LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    LocalDateTime fechaActualizacion;

}
