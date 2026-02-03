package es.juanbosco.ruben.proyecto_final_gimasio_2.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import es.juanbosco.ruben.proyecto_final_gimasio_2.enums.EstadoReserva;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private Long idReserva;
    private Long idClase;
    private String nombreClase;
    private Long idSocio;
    private String nombreSocio;
    private LocalDateTime fechaReservar;
    private Long idBono;
    private EstadoReserva estado;
    private HorarioDTO horario;
}