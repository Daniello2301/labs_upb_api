package co.edu.upb.labs_upb.converter;

import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.model.Usuario;
import org.springframework.stereotype.Component;


@Component
public class UsuarioConverter {


    /**
     * Method to convert a UsuarioDTO to a Usuario entity.
     *
     * @param usuarioDTO the UsuarioDTO to be converted.
     * @return the converted Usuario entity.
     */
    public  Usuario usuarioDtoToUsuario(UsuarioDTO usuarioDTO) {

        Usuario usuario = new Usuario();

        usuario.setId(usuarioDTO.getId());
        usuario.setIdUpb(usuarioDTO.getIdUpb());
        usuario.setDocumento(usuarioDTO.getDocumento());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setFechaActualizacion(usuarioDTO.getFechaActualizacion());
        usuario.setFechaCreacion(usuarioDTO.getFechaCreacion());

        return usuario;

    }

    /**
     * Method to convert a Usuario entity to a UsuarioDTO.
     *
     * @param usuario the Usuario entity to be converted.
     * @return the converted UsuarioDTO.
     */
    public  UsuarioDTO usuarioToUsurioDTO(Usuario usuario) {

        UsuarioDTO usuarioDTO = new UsuarioDTO();

        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setIdUpb(usuario.getIdUpb());
        usuarioDTO.setDocumento(usuario.getDocumento());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setPassword(usuario.getPassword());
        usuarioDTO.setFechaCreacion(usuario.getFechaCreacion());
        usuarioDTO.setFechaActualizacion(usuario.getFechaActualizacion());

        return usuarioDTO;

    }
}
