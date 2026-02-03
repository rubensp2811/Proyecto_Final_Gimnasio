package es.juanbosco.ruben.proyecto_final_gimasio_2.DTO;

import es.juanbosco.ruben.proyecto_final_gimasio_2.enums.TipoPlan;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioUpdateDTO {
    private String nombre;
    private String apellidos;

    @Email(message = "El email debe ser válido")
    private String email;

    private String telefono;
    private TipoPlan tipoPlan;
    private Boolean esActivo;
}