package co.edu.upb.labs_upb.converter;

import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.model.Rol;
import co.edu.upb.labs_upb.model.Usuario;
import co.edu.upb.labs_upb.repository.IRolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioConverter {


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

    public  UsuarioDTO usuarioToUsurioDTO(Usuario usuario){
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
