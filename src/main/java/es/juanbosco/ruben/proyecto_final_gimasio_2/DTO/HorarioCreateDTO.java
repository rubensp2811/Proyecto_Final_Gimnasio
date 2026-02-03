package es.juanbosco.ruben.proyecto_final_gimasio_2.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioCreateDTO {

    @Schema(type = "string", pattern = "HH:mm", example = "10:00")
    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @Schema(type = "string", pattern = "HH:mm", example = "12:00")
    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;
}