package co.edu.upb.labs_upb.service.iface;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IImagenActivoService {

    // Create Image
    Map<String, Object> createImage(Long activoId, MultipartFile file);

    // Read Image by ActivoId
    Map<String, Object> readImage(Long activoId);

    // Delete Image
    Map<String, Object> deleteImage(Long activoId);

}
