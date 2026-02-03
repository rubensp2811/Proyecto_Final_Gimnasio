package es.juanbosco.ruben.proyecto_final_gimasio_2.Controllers;

import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.PlanDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.enums.TipoPlan;
import es.juanbosco.ruben.proyecto_final_gimasio_2.servicios.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
@Tag(name = "Planes", description = "API para la gestión de planes de membresía")
public class PlanController {

    @Autowired
    private PlanService planService;

    @Operation(summary = "Obtener todos los planes", description = "Retorna una lista de todos los planes disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de planes obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<PlanDTO>> getAllPlanes() {
        return ResponseEntity.ok(planService.findAll());
    }
}