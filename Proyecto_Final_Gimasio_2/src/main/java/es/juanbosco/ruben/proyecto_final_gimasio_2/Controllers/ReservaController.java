package es.juanbosco.ruben.proyecto_final_gimasio_2.Controllers;

import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.ReservaCreateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.ReservaDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.enums.EstadoReserva;
import es.juanbosco.ruben.proyecto_final_gimasio_2.servicios.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/api/reservas")
@Tag(name = "Reservas", description = "API para la gestión de reservas de clases")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Operation(summary = "Obtener todas las reservas", description = "Retorna una lista paginada de todas las reservas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<Page<ReservaDTO>> getAllReservas(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reservaService.findAll(pageable));
    }

    @Operation(summary = "Buscar reservas", description = "Busca reservas por socio o estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    })
    @GetMapping("/buscar")
    public ResponseEntity<Page<ReservaDTO>> buscarReservas(
            @Parameter(description = "ID del socio") @RequestParam(required = false) Long idSocio,
            @Parameter(description = "Estado de la reserva (CONFIRMADA, CANCELADA, COMPLETADA)") @RequestParam(required = false) EstadoReserva estado,
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reservaService.findByFiltros(idSocio, estado, pageable));
    }

    @Operation(summary = "Obtener reserva por ID", description = "Retorna una reserva específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> getReservaById(
            @Parameter(description = "ID de la reserva") @PathVariable Long id) {
        return ResponseEntity.ok(reservaService.findById(id));
    }

    @Operation(summary = "Crear reserva", description = "Crea una nueva reserva en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o no hay plazas disponibles")
    })

    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @io.swagger.v3.oas.annotations.parameters.RequestBody es necesario usarlo para sobrescribe el comportamiento por defecto de Swagger
     * De esta forma puedo cambiar el json por defecto al crear una reserva y que use idBono: null
     * */
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la reserva a crear",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReservaCreateDTO.class),
                    examples = {
                            @ExampleObject(
                                    name = "Reserva clase grupal",
                                    value = "{\"idClase\": 1, \"idSocio\": 1, \"idBono\": null}",
                                    description = "Ejemplo para clase grupal (no requiere bono)"
                            ),
                            @ExampleObject(
                                    name = "Reserva clase privada",
                                    value = "{\"idClase\": 2, \"idSocio\": 1, \"idBono\": 1}",
                                    description = "Ejemplo para clase privada (requiere bono)"
                            )
                    }
            )
    )
    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PostMapping
    public ResponseEntity<ReservaDTO> createReserva(@Valid @RequestBody ReservaCreateDTO reservaDTO) {
        ReservaDTO created = reservaService.create(reservaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Cancelar reserva", description = "Cambia el estado de una reserva a CANCELADA")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva cancelada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ReservaDTO> cancelarReserva(
            @Parameter(description = "ID de la reserva") @PathVariable Long id) {
        return ResponseEntity.ok(reservaService.cancelar(id));
    }

    @Operation(summary = "Eliminar reserva", description = "Elimina una reserva del sistema permanentemente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(
            @Parameter(description = "ID de la reserva") @PathVariable Long id) {
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
