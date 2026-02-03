package es.juanbosco.ruben.proyecto_final_gimasio_2.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import es.juanbosco.ruben.proyecto_final_gimasio_2.enums.TipoPlan;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO {
    // Eliminado el campo idPlan, solo se usará tipoPlan
    private TipoPlan tipo;
}
