package co.edu.upb.labs_upb.service.impl;

import co.edu.upb.labs_upb.converter.UsuarioConverter;
import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.Usuario;
import co.edu.upb.labs_upb.repository.IUsuarioRepository;
import co.edu.upb.labs_upb.service.iface.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UsuarioImpl implements IUsuarioService {

    @Autowired
    private IUsuarioRepository  usuarioRepository;

    @Autowired
    private UsuarioConverter usuarioConverter;

    @Override
    public Page<Usuario> usersPagination(int numPage, int sizePage) throws RestException {
        return null;
    }

    @Override
    public Page<Usuario> usersPaginationSortBy(String sortBy) throws RestException {
        return null;
    }

    @Override
    public Page<Usuario> usersPaginationSortBy(int numPage, int sizePage, String sortBy) throws RestException {
        return null;
    }

    @Override
    public List<UsuarioDTO> findAllUsers() throws RestException {

        List<Usuario> usuarios = usuarioRepository.findAll();

        if(usuarios != null && !usuarios.isEmpty()){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No Se encontraron usuarios",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        List<UsuarioDTO> usuariosDTO = Collections.emptyList();

        for(Usuario usuario : usuarios){
            usuariosDTO.add(usuarioConverter.usuarioToUsuarioDto(usuario));
        }

        return usuariosDTO;
    }

    @Override
    public UsuarioDTO findUserById(Long id) throws RestException {
        return null;
    }

    @Override
    public UsuarioDTO saveUser(UsuarioDTO user) throws RestException {
        return null;
    }

    @Override
    public UsuarioDTO updateUser(Long id, UsuarioDTO user) throws RestException {
        return null;
    }

    @Override
    public void deleteUser(Long id) throws RestException {

    }
}
