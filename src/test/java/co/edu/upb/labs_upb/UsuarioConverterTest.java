package co.edu.upb.labs_upb;

import co.edu.upb.labs_upb.converter.UsuarioConverter;
import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.model.Rol;
import co.edu.upb.labs_upb.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class UsuarioConverterTest {

    private UsuarioConverter usuarioConverter;

    @Test
    public void testUsuarioToUsuarioDTO() {
        // Given

        List<Rol> rols = new ArrayList<>();

        Rol rol1 = new Rol();
        rol1.setNombre("ADMIN");
        rols.add(rol1);
        Rol rol2 = new Rol();
        rol2.setNombre("USER");
        rols.add(rol2);

        rols.add(rol1);
        rols.add(rol2);

        Usuario usuario1 = new Usuario();

        usuario1.setId(1L);
        usuario1.setDocumento(123456789L);
        usuario1.setNombre("Juan");
        usuario1.setApellido("Perez");
        usuario1.setCorreo("usuario@correo.com");


        // When
        UsuarioDTO usuarioDTO = UsuarioConverter.usuarioToUsurioDTO(usuario1);

        // Then
        assertEquals(usuario1.getId(), usuarioDTO.getId());
        assertEquals(usuario1.getDocumento(), usuarioDTO.getDocumento());
        assertEquals(usuario1.getNombre(), usuarioDTO.getNombre());
        assertEquals(usuario1.getApellido(), usuarioDTO.getApellido());
        assertEquals(usuario1.getCorreo(), usuarioDTO.getEmail());
    }

    @Test
    public void testUsuarioDTOToUsuario() {
        // Given

        List<String> rols = new ArrayList<>();

        String rol1 = "ADMIN";
        String rol2 = "USER";
        rols.add(rol1);
        rols.add(rol2);

        UsuarioDTO usuario1Dto = new UsuarioDTO();

        usuario1Dto.setId(1L);
        usuario1Dto.setDocumento(123456789L);
        usuario1Dto.setNombre("Juan");
        usuario1Dto.setApellido("Perez");
        usuario1Dto.setEmail("usuario@correo.com");


        // When
        Usuario usuario = UsuarioConverter.usuarioDtoToUsuario(usuario1Dto);


        // Then
        assertThat( rols ).containsExactlyInAnyOrderElementsOf(
                usuario.getRoles()
                        .stream()
                        .map(Rol::getNombre)
                        .toList()
        );
        assertEquals(usuario1Dto.getId(), usuario.getId());
        assertEquals(usuario1Dto.getDocumento(), usuario.getDocumento());
        assertEquals(usuario1Dto.getNombre(), usuario.getNombre());
        assertEquals(usuario1Dto.getApellido(), usuario.getApellido());
        assertEquals(usuario1Dto.getEmail(), usuario.getCorreo());
        assertThat(usuario.getRoles()).hasSize(2);
    }



    private UsuarioDTO createUsuarioDTO( Long id, Long documento, String nombre, String apellido, String email, List<String> roles) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId( id );
        usuarioDTO.setDocumento(documento);
        usuarioDTO.setNombre(nombre);
        usuarioDTO.setApellido(apellido);
        usuarioDTO.setEmail(email);
        return usuarioDTO;
    }

    private Usuario createUsuario( Long id, Long documento, String nombre, String apellido, String email, List<Rol> roles) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setDocumento(documento);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(email);
        return usuario;
    }
}
