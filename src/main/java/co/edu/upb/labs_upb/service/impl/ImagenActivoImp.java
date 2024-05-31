package co.edu.upb.labs_upb.service.impl;

import co.edu.upb.labs_upb.model.Activo;
import co.edu.upb.labs_upb.model.ImagenActivo;
import co.edu.upb.labs_upb.repository.IActivoRepository;
import co.edu.upb.labs_upb.repository.IImagenActivoRepository;
import co.edu.upb.labs_upb.service.iface.IImagenActivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImagenActivoImp implements IImagenActivoService {

    @Autowired
    private IImagenActivoRepository imagenRepository;

    @Autowired
    private IActivoRepository activoRepository;

    // Create Image
    public Map<String, Object> createImage(Long activoId, MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            ImagenActivo imagen = new ImagenActivo();
            Activo activoFounded = activoRepository.findById(activoId).orElse(null);
            if (activoFounded == null) {
                response.put("message", "Activo not found");
                response.put("status", HttpStatus.NOT_FOUND);
                return response;
            }
            imagen.setActivo(activoFounded);
            if (file.isEmpty()) {
                response.put("message", "Image is empty");
                response.put("status", HttpStatus.BAD_REQUEST);
                return response;
            }

            byte[] imageBytes = file.getBytes();
            Blob imageBlob = new javax.sql.rowset.serial.SerialBlob(imageBytes);

            ImagenActivo imageCreated = imagenRepository.getByActivoId(activoId);
            if (imageCreated != null) {
                imageCreated.setImagen(imageBlob);
                imagenRepository.save(imageCreated);
                response.put("message", "Image was updated successfully");
                response.put("status", HttpStatus.CREATED);
                return response;
            }

            imagen.setImagen(imageBlob);
            imagenRepository.save(imagen);
            response.put("message", "Image created successfully");
            response.put("activo", activoFounded.getNumeroInventario() + " - " + activoFounded.getDescripcion());
            response.put("status", HttpStatus.CREATED);
        } catch (IOException e) {
            response.put("message", "Error creating image");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    public Map<String, Object> readImage(Long activoId) {

        Map<String, Object> response = new HashMap<>();
        try {
            ImagenActivo imagenOpt = imagenRepository.getByActivoId(activoId);
            if (imagenOpt == null) {
                response.put("message", "Image not found");
                response.put("status", HttpStatus.NOT_FOUND);
                return response;
            }

            ImagenActivo imagen = imagenOpt;
            byte[] imageBytes = imagen.getImagen().getBytes(1, (int) imagen.getImagen().length());
            response.put("image", imageBytes);
            response.put("message", "Image retrieved successfully");
            response.put("Activo", imagen.getActivo().getNumeroInventario() + " - " + imagen.getActivo().getDescripcion());
            response.put("status", HttpStatus.OK);
        } catch (SQLException e) {
            response.put("message", "Error retrieving image");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Override
    public Map<String, Object> deleteImage(Long activoId) {

        Map<String, Object> response = new HashMap<>();

        ImagenActivo imagenOpt = imagenRepository.getByActivoId(activoId);
        if (imagenOpt == null) {
            response.put("message", "Image not found");
            response.put("status", HttpStatus.NOT_FOUND);
            return response;
        }

        imagenRepository.delete(imagenOpt);

        response.put("message", "Image deleted successfully");
        response.put("status", HttpStatus.OK);
        return response;
    }

}
