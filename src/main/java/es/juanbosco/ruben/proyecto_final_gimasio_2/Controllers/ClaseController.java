package es.juanbosco.ruben.proyecto_final_gimasio_2.Controllers;

import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.ClaseCreateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.ClaseDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.servicios.ClaseService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/clases")
@Tag(name = "Clases", description = "API para la gestión de clases del gimnasio")
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    @Operation(summary = "Obtener todas las clases", description = "Retorna una lista paginada de todas las clases")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clases obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<Page<ClaseDTO>> getAllClases(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(claseService.findAll(pageable));
    }

    @Operation(summary = "Buscar clases", description = "Busca clases por nombre, fecha o tipo (privada/grupal)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    })
    @GetMapping("/buscar")
    public ResponseEntity<Page<ClaseDTO>> buscarClases(
            @Parameter(description = "Nombre de la clase") @RequestParam(required = false) String nombre,
            @Parameter(description = "Fecha de la clase (formato ISO: YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaClase,
            @Parameter(description = "Indica si es una clase privada") @RequestParam(required = false) Boolean esPrivada,
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(claseService.findByFiltros(nombre, fechaClase, esPrivada, pageable));
    }

    @Operation(summary = "Obtener clases populares", description = "Retorna las clases más populares según el número de reservas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clases populares obtenida exitosamente")
    })
    @GetMapping("/populares")
    public ResponseEntity<List<ClaseDTO>> getClasesPopulares(
            @Parameter(description = "Límite de resultados") @RequestParam(defaultValue = "5") int limit
    ) {
        return ResponseEntity.ok(claseService.findClasesPopulares(limit));
    }

    @Operation(summary = "Obtener clase por ID", description = "Retorna una clase específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clase encontrada"),
            @ApiResponse(responseCode = "404", description = "Clase no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClaseDTO> getClaseById(
            @Parameter(description = "ID de la clase") @PathVariable Long id) {
        return ResponseEntity.ok(claseService.findById(id));
    }

    @Operation(summary = "Crear clase", description = "Crea una nueva clase en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Clase creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<ClaseDTO> createClase(
            @Parameter(description = "Datos de la clase a crear") @Valid @RequestBody ClaseCreateDTO claseDTO) {
        ClaseDTO created = claseService.create(claseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Eliminar clase", description = "Elimina una clase del sistema permanentemente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Clase eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Clase no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClase(
            @Parameter(description = "ID de la clase") @PathVariable Long id) {
        claseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}