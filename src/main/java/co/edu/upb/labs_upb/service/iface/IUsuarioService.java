package co.edu.upb.labs_upb.service.iface;

import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.Usuario;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUsuarioService {

    /* ******************** User Pagintacion And Sort ******************************************* */
    Page<Usuario> usersPagination(int numPage, int sizePage) throws RestException;

    //Sort by
    Page<Usuario> usersPaginationSortBy(String sortBy) throws RestException;

    //Sort by and pagination
    Page<Usuario> usersPaginationSortBy(int numPage, int sizePage, String sortBy) throws RestException;


    // CRUD methods

    public List<UsuarioDTO> findAllUsers() throws RestException;
    public UsuarioDTO findUserById(Long id) throws RestException;

    public UsuarioDTO saveUser(UsuarioDTO user) throws RestException;

    public UsuarioDTO updateUser(Long id, UsuarioDTO user) throws RestException;

    public void deleteUser(Long id) throws RestException;


}
