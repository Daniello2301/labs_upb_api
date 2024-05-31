package co.edu.upb.labs_upb.controller;

import co.edu.upb.labs_upb.service.iface.IImagenActivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/imagen-activo")
public class ImagenActivoController {

    @Autowired
    private IImagenActivoService imagenActivoService;

    @PostMapping("/{activoId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> createImage(@PathVariable Long activoId, @RequestParam("image") MultipartFile file) {
        return ResponseEntity.ok(imagenActivoService.createImage(activoId, file));
    }

    @GetMapping("/{activoId}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Map<String, Object>> readImage(@PathVariable Long activoId) {
        Map<String, Object> response = imagenActivoService.readImage(activoId);
        return new ResponseEntity<>(response, (HttpStatus) response.get("status"));
    }

    @PutMapping("/{activoId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> updateImage(@PathVariable Long activoId, @RequestParam("image") MultipartFile file) {
        return ResponseEntity.ok(imagenActivoService.createImage(activoId, file));
    }


    @DeleteMapping("/{activoId}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> deleteImage(@PathVariable Long activoId) {
        return ResponseEntity.ok(imagenActivoService.deleteImage(activoId));
    }

}
