package es.juanbosco.ruben.proyecto_final_gimasio_2.Controllers;

import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.BonoCreateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.BonoDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.servicios.BonoService;
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

import java.util.List;

@RestController
@RequestMapping("/api/bonos")
@Tag(name = "Bonos", description = "API para la gestión de bonos de socios")
public class BonoController {

    @Autowired
    private BonoService bonoService;

    @Operation(summary = "Obtener todos los bonos", description = "Retorna una lista paginada de todos los bonos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de bonos obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<Page<BonoDTO>> getAllBonos(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bonoService.findAll(pageable));
    }

    @Operation(summary = "Obtener bonos de un socio", description = "Retorna todos los bonos de un socio específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de bonos del socio obtenida exitosamente")
    })
    @GetMapping("/socio/{idSocio}")
    public ResponseEntity<List<BonoDTO>> getBonosBySocio(
            @Parameter(description = "ID del socio") @PathVariable Long idSocio) {
        return ResponseEntity.ok(bonoService.findBySocioId(idSocio));
    }

    @Operation(summary = "Obtener bono por ID", description = "Retorna un bono específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bono encontrado"),
            @ApiResponse(responseCode = "404", description = "Bono no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BonoDTO> getBonoById(
            @Parameter(description = "ID del bono") @PathVariable Long id) {
        return ResponseEntity.ok(bonoService.findById(id));
    }

    @Operation(summary = "Crear bono", description = "Crea un nuevo bono para un socio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bono creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<BonoDTO> createBono(
            @Parameter(description = "Datos del bono a crear") @Valid @RequestBody BonoCreateDTO bonoDTO) {
        BonoDTO created = bonoService.create(bonoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Eliminar bono", description = "Elimina un bono del sistema permanentemente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bono eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bono no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBono(
            @Parameter(description = "ID del bono") @PathVariable Long id) {
        bonoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
