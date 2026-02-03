package es.juanbosco.ruben.proyecto_final_gimasio_2.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonoCreateDTO {

    @NotNull(message = "El ID del socio es obligatorio")
    private Long idSocio;

    @NotNull(message = "El ID del entrenador es obligatorio")
    private Long idEntrenador;
}
