package co.edu.upb.labs_upb.service.impl;

import co.edu.upb.labs_upb.dto.PrestamoDTO;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.repository.IActivoRepository;
import co.edu.upb.labs_upb.repository.IPrestamoRepository;
import co.edu.upb.labs_upb.service.iface.IPrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestamoImplement implements IPrestamoService {


    @Autowired
    private IPrestamoRepository prestamoRepository;

    @Autowired
    private IActivoRepository activoRepository;

    @Override
    public List<PrestamoDTO> getAll() throws RestException {
        return null;
    }

    @Override
    public PrestamoDTO getById(Long id) throws RestException {
        return null;
    }

    @Override
    public PrestamoDTO create(PrestamoDTO prestamoDTO) throws RestException {
        return null;
    }

    @Override
    public void deleteById(Long id) throws RestException {

    }
}
