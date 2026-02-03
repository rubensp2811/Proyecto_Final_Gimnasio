package es.juanbosco.ruben.proyecto_final_gimasio_2.Controllers;

import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.EntrenadorCreateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.EntrenadorDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.EntrenadorUpdateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.servicios.EntrenadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entrenadores")
@Tag(name = "Entrenadores", description = "API para la gestión de entrenadores del gimnasio")
public class EntrenadorController {

    @Autowired
    private EntrenadorService entrenadorService;

    @Operation(summary = "Obtener todos los entrenadores", description = "Retorna una lista paginada de todos los entrenadores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de entrenadores obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<Page<EntrenadorDTO>> getAllEntrenadores(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(entrenadorService.findAll(pageable));
    }

    @Operation(summary = "Buscar entrenadores por especialidad", description = "Retorna entrenadores filtrados por especialidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    })
    @GetMapping("/especialidad")
    public ResponseEntity<Page<EntrenadorDTO>> getByEspecialidad(
            @Parameter(description = "Especialidad del entrenador") @RequestParam String especialidad,
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(entrenadorService.findByEspecialidad(especialidad, pageable));
    }

    @Operation(summary = "Obtener entrenador por ID", description = "Retorna un entrenador específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrenador encontrado"),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntrenadorDTO> getEntrenadorById(
            @Parameter(description = "ID del entrenador") @PathVariable Long id) {
        return ResponseEntity.ok(entrenadorService.findById(id));
    }

    @Operation(summary = "Crear entrenador", description = "Crea un nuevo entrenador en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entrenador creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<EntrenadorDTO> createEntrenador(
            @Parameter(description = "Datos del entrenador a crear") @Valid @RequestBody EntrenadorCreateDTO entrenadorDTO) {
        EntrenadorDTO created = entrenadorService.create(entrenadorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Eliminar entrenador", description = "Elimina un entrenador del sistema permanentemente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entrenador eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntrenador(
            @Parameter(description = "ID del entrenador") @PathVariable Long id) {
        entrenadorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar entrenador", description = "Actualiza todos los datos del entrenador excepto tipoPlan, idSocio y fechaAlta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrenador actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntrenadorDTO> updateEntrenador(
            @Parameter(description = "ID del entrenador") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del entrenador") @Valid @RequestBody EntrenadorUpdateDTO entrenadorDTO
    ) {
        return ResponseEntity.ok(entrenadorService.update(id, entrenadorDTO));
    }

    @Operation(summary = "Dar de baja a un entrenador", description = "Marca un entrenador como inactivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrenador dado de baja exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado")
    })
    @PutMapping("/{id}/baja")
    public ResponseEntity<EntrenadorDTO> darDeBajaEntrenador(
            @Parameter(description = "ID del entrenador") @PathVariable Long id) {
        return ResponseEntity.ok(entrenadorService.darDeBaja(id));
    }
}
