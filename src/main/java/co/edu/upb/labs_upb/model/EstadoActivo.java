package co.edu.upb.labs_upb.model;

public enum EstadoActivo {
    DISPONIBLE(1), PRESTADO(2), MANTENIMIENTO(3), DESUSO(4);

    int estadoId;

    private EstadoActivo(int estadoId) {
        this.estadoId = estadoId;
    }

    public int getEstadoId() {
        return estadoId;
    }

}
