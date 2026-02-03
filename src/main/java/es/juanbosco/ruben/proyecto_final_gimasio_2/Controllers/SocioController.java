package es.juanbosco.ruben.proyecto_final_gimasio_2.Controllers;

import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.SocioCreateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.SocioDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.SocioUpdateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.servicios.SocioService;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/socios")
@Tag(name = "Socios", description = "API para la gestión de socios del gimnasio")
public class SocioController {

    @Autowired
    private SocioService socioService;

    @Operation(summary = "Obtener todos los socios", description = "Retorna una lista paginada de todos los socios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de socios obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<Page<SocioDTO>> getAllSocios(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo por el cual ordenar") @RequestParam(defaultValue = "idSocio") String sortBy,
            @Parameter(description = "Dirección de ordenamiento (ASC o DESC)") @RequestParam(defaultValue = "ASC") String direction
    ) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return ResponseEntity.ok(socioService.findAll(pageable));
    }

    @Operation(summary = "Obtener socios activos", description = "Retorna una lista paginada de socios activos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de socios activos obtenida exitosamente")
    })
    @GetMapping("/activos")
    public ResponseEntity<Page<SocioDTO>> getSociosActivos(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(socioService.findAllActivos(pageable));
    }

    @Operation(summary = "Buscar socios", description = "Busca socios por nombre, email o estado activo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    })
    @GetMapping("/buscar")
    public ResponseEntity<Page<SocioDTO>> buscarSocios(
            @Parameter(description = "Nombre del socio") @RequestParam(required = false) String nombre,
            @Parameter(description = "Email del socio") @RequestParam(required = false) String email,
            @Parameter(description = "Estado activo del socio") @RequestParam(required = false) Boolean esActivo,
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(socioService.findByFiltros(nombre, email, esActivo, pageable));
    }

    @Operation(summary = "Obtener socio por ID", description = "Retorna un socio específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Socio encontrado"),
            @ApiResponse(responseCode = "404", description = "Socio no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SocioDTO> getSocioById(
            @Parameter(description = "ID del socio") @PathVariable Long id) {
        return ResponseEntity.ok(socioService.findById(id));
    }

    @Operation(summary = "Crear socio", description = "Crea un nuevo socio en el sistema. Usa tipoPlan=BASICO o tipoPlan=PREMIUM.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Socio creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<SocioDTO> createSocio(
            @Parameter(description = "Datos del socio a crear") @Valid @RequestBody SocioCreateDTO socioDTO) {
        SocioDTO created = socioService.create(socioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Actualizar socio", description = "Actualiza los datos de un socio existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Socio actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Socio no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SocioDTO> updateSocio(
            @Parameter(description = "ID del socio") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del socio") @Valid @RequestBody SocioUpdateDTO socioDTO
    ) {
        return ResponseEntity.ok(socioService.update(id, socioDTO));
    }

    @Operation(summary = "Dar de baja a un socio", description = "Marca un socio como inactivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Socio dado de baja exitosamente"),
            @ApiResponse(responseCode = "404", description = "Socio no encontrado")
    })
    @PutMapping("/{id}/baja")
    public ResponseEntity<SocioDTO> darDeBaja(
            @Parameter(description = "ID del socio") @PathVariable Long id) {
        return ResponseEntity.ok(socioService.darDeBaja(id));
    }

    @Operation(summary = "Eliminar socio", description = "Elimina un socio del sistema permanentemente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Socio eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Socio no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSocio(
            @Parameter(description = "ID del socio") @PathVariable Long id) {
        socioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}