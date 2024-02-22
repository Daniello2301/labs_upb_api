package co.edu.upb.labs_upb.converter;

import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.model.Rol;
import co.edu.upb.labs_upb.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioConverter {

    public Usuario usuarioDtoToUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioDTO.getId());
        usuario.setDocumento(usuarioDTO.getDocumento());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setCorreo(usuarioDTO.getEmail());
        usuario.setContrasena(usuarioDTO.getPassword());
        usuario.setFechaActualizacion(usuarioDTO.getFechaActualizacion());
        usuario.setFechaCreacion(usuarioDTO.getFechaCreacion());
        /*Arrays.stream(usuarioDTO.getRoles().toArray()).forEach(rol -> {
            Rol rol1 = new Rol();
            rol1.setNombre(rol.toString());
            usuario.getRoles().add(rol1);
        });*/
        usuario.setRoles(usuarioDTO.getRoles()
                .stream()
                .map(rol -> {
                    Rol rol1 = new Rol();
                    rol1.setNombre(rol);
                    return rol1;
                })
                .collect(Collectors.toList()));

        return usuario;
    }

    public static void setMapValuesClient(List<Usuario> usuarios, List<UsuarioDTO> usuariosDto){
        usuarios.stream().map(usuario -> {
            UsuarioDTO usDto = getMapValuesClient(usuario);
            return usDto;
        }).forEach(usuariosDto::add);
    }

    public static UsuarioDTO getMapValuesClient(Usuario usuario){
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setDocumento(usuario.getDocumento());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setEmail(usuario.getCorreo());
        usuarioDTO.setPassword(usuario.getContrasena());
        usuarioDTO.setFechaCreacion(usuario.getFechaCreacion());
        usuarioDTO.setFechaActualizacion(usuario.getFechaActualizacion());
        usuarioDTO.setRoles(usuario.getRoles()
                .stream()
                .map(Rol::getNombre)
                .collect(Collectors.toList()));
        return usuarioDTO;
    }
}
