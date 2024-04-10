package co.edu.upb.labs_upb.converter;

import co.edu.upb.labs_upb.model.Activo;
import co.edu.upb.labs_upb.model.EstadoActivo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

@Converter
public class EstadoActivoConverter implements AttributeConverter<EstadoActivo, Integer> {
    @Override
    public Integer convertToDatabaseColumn(EstadoActivo estado) {
        return estado.getEstadoId();
    }

    @Override
    public EstadoActivo convertToEntityAttribute(Integer integer) {
        return Arrays.stream(EstadoActivo.values())
                .filter(estadoActivo -> estadoActivo.getEstadoId() == integer)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
