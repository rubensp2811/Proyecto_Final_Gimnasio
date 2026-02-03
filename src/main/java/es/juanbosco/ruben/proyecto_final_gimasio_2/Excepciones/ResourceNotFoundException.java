package es.juanbosco.ruben.proyecto_final_gimasio_2.Excepciones;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}