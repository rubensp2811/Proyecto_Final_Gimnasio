package es.juanbosco.ruben.proyecto_final_gimasio_2.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonoDTO {
    private Long idBono;
    private Long idSocio;
    private String nombreSocio;
    private Integer sesionesTotales;
    private Integer sesionesRestantes;
    private Long idEntrenador;
    private String nombreEntrenador;
    private LocalDate fechaCompra;
    private Boolean activo;
}