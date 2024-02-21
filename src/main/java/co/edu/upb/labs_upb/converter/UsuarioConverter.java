package co.edu.upb.labs_upb.converter;

import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.model.Usuario;
import org.springframework.stereotype.Component;

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
        return usuario;
    }

    public UsuarioDTO usuarioToUsuarioDto(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setDocumento(usuario.getDocumento());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setEmail(usuario.getCorreo());
        usuarioDTO.setPassword(usuario.getContrasena());
        return usuarioDTO;
    }
}
