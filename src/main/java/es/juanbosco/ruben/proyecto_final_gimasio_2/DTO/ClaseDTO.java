package es.juanbosco.ruben.proyecto_final_gimasio_2.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaseDTO {
    private Long idClase;
    private String nombre;
    private String descripcion;
    private LocalDate fechaClase;
    private Integer duracion; // Calculada automáticamente desde el horario
    private Boolean esPrivada;
    private Integer aforoMaximo;
    private HorarioDTO horario;
    private Long idEntrenador;
    private String nombreEntrenador;
    private Integer reservasConfirmadas;
}