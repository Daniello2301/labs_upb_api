package co.edu.upb.labs_upb.service.iface;

import co.edu.upb.labs_upb.dto.ChangePasswordReset;
import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    /* ******************** User Pagination And Sort ******************************************* */

    Page<Usuario> usersPagination(int numPage, int sizePage) throws RestException;

    //Sort by
    Page<Usuario> usersPaginationSortBy(String sortBy) throws RestException;

    //Sort by and pagination
    Page<Usuario> usersPaginationSortBy(int numPage, int sizePage, String sortBy) throws RestException;


    // CRUD methods

    Optional<Usuario> findByUsername(String username);


    List<UsuarioDTO> findAllUsers() throws RestException;


    UsuarioDTO findByCorreo(String correo) throws RestException;

    UsuarioDTO findUserById(Long id) throws RestException;

    UsuarioDTO saveUser(UsuarioDTO user) throws RestException;

    void changePassword(ChangePasswordReset request, Authentication connectedUser) throws RestException;

    void deleteUser(Long id) throws RestException;


}

