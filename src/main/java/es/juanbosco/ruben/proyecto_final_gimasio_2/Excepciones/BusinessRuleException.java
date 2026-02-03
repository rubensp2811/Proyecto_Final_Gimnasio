package es.juanbosco.ruben.proyecto_final_gimasio_2.Excepciones;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}