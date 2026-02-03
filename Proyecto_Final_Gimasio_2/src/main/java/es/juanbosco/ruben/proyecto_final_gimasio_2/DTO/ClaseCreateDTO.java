package es.juanbosco.ruben.proyecto_final_gimasio_2.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaseCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "La fecha de la clase es obligatoria")
    private LocalDate fechaClase;

    @Schema(defaultValue = "false")
    @NotNull(message = "Debe especificar si es privada o no")
    private Boolean esPrivada;

    private Integer aforoMaximo;

    @NotNull(message = "El horario es obligatorio")
    private Long idHorario;

    @NotNull(message = "El entrenador es obligatorio")
    private Long idEntrenador;
}