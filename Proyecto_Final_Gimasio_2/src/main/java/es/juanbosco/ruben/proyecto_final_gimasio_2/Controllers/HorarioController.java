package es.juanbosco.ruben.proyecto_final_gimasio_2.Controllers;

import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.HorarioCreateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.HorarioDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.servicios.HorarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@Tag(name = "Horarios", description = "API para la gestión de horarios de clases")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @Operation(summary = "Obtener todos los horarios", description = "Retorna una lista de todos los horarios sin paginación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de horarios obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<HorarioDTO>> getAllHorarios() {
        return ResponseEntity.ok(horarioService.findAll());
    }

    @Operation(summary = "Obtener horario por ID", description = "Retorna un horario específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horario encontrado"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HorarioDTO> getHorarioById(
            @Parameter(description = "ID del horario") @PathVariable Long id) {
        return ResponseEntity.ok(horarioService.findById(id));
    }

    @Operation(summary = "Crear horario", description = "Crea un nuevo horario para una clase")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Horario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<HorarioDTO> createHorario(
            @Parameter(description = "Datos del horario a crear") @Valid @RequestBody HorarioCreateDTO horarioDTO) {
        HorarioDTO created = horarioService.create(horarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Eliminar horario", description = "Elimina un horario del sistema permanentemente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Horario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHorario(
            @Parameter(description = "ID del horario") @PathVariable Long id) {
        horarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
