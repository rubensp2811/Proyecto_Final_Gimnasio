package es.juanbosco.ruben.proyecto_final_gimasio_2.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioDTO {
    private Long idSocio;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private LocalDate fechaAlta;
    private Boolean esActivo;
    private String tipoPlan;
}