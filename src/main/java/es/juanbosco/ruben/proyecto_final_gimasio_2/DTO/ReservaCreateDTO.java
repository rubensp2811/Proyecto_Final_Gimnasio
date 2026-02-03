package es.juanbosco.ruben.proyecto_final_gimasio_2.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaCreateDTO {

    @NotNull(message = "El ID de la clase es obligatorio")
    private Long idClase;

    @NotNull(message = "El ID del socio es obligatorio")
    private Long idSocio;

    // Solo necesario para clases privadas
    @Schema(nullable = true, type = "integer", example = "null")
    @JsonProperty(defaultValue = "null")
    private Long idBono;
}