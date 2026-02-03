package es.juanbosco.ruben.proyecto_final_gimasio_2.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrenadorUpdateDTO {
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private Boolean esActivo;
    private String especialidad;
}
