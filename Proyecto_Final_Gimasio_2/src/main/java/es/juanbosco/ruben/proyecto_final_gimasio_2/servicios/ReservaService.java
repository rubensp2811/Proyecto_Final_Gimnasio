package es.juanbosco.ruben.proyecto_final_gimasio_2.servicios;

import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.HorarioDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.ReservaCreateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.ReservaDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Excepciones.BusinessRuleException;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Excepciones.ResourceNotFoundException;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.*;
import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.*;
import es.juanbosco.ruben.proyecto_final_gimasio_2.enums.EstadoReserva;
import es.juanbosco.ruben.proyecto_final_gimasio_2.enums.TipoPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

// Servicio para la gestión de reservas de clases
// Incluye validaciones de negocio y conversión a DTO
@Service
@Transactional
public class ReservaService {
    // Repositorio de reservas
    @Autowired
    private ReservaRepository reservaRepository;
    // Repositorio de clases
    @Autowired
    private ClaseRepository claseRepository;
    // Repositorio de socios
    @Autowired
    private SocioRepository socioRepository;
    // Repositorio de bonos
    @Autowired
    private BonoRepository bonoRepository;
    // Repositorio de horarios
    @Autowired
    private HorarioRepository horarioRepository;

    // Devuelve todas las reservas paginadas
    public Page<ReservaDTO> findAll(Pageable pageable) {
        return reservaRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    // Busca reservas por filtros (socio, estado)
    public Page<ReservaDTO> findByFiltros(Long idSocio, EstadoReserva estado, Pageable pageable) {
        return reservaRepository.findByFiltros(idSocio, estado, pageable)
                .map(this::convertToDTO);
    }

    // Busca reserva por ID
    public ReservaDTO findById(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));
        return convertToDTO(reserva);
    }

    // Crea una nueva reserva con validaciones de negocio
    public ReservaDTO create(ReservaCreateDTO createDTO) {
        Clase clase = claseRepository.findById(createDTO.getIdClase())
                .orElseThrow(() -> new ResourceNotFoundException("Clase no encontrada con id: " + createDTO.getIdClase()));

        Socio socio = socioRepository.findById(createDTO.getIdSocio())
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + createDTO.getIdSocio()));

        // VALIDACIÓN CRÍTICA: Un socio no puede reservar la misma clase más de una vez
        boolean yaReservada = reservaRepository.existsByClaseIdClaseAndSocioIdSocioAndEstado(
                clase.getIdClase(),
                socio.getIdSocio(),
                EstadoReserva.CONFIRMADA
        );

        if (yaReservada) {
            throw new BusinessRuleException(
                "El socio ya tiene una reserva confirmada para esta clase. " +
                "No se pueden hacer reservas duplicadas."
            );
        }

        // VALIDACIÓN 1: Socio debe estar activo
        if (!socio.getEsActivo()) {
            throw new BusinessRuleException("Un socio inactivo no puede realizar reservas");
        }

        // VALIDACIÓN 2: Si la clase es grupal (esPrivada = false)
        if (!clase.getEsPrivada()) {
            // 2.1: El socio debe tener plan PREMIUM para reservar clases grupales
            if (socio.getPlan() == null || socio.getPlan().getTipo() != TipoPlan.PREMIUM) {
                throw new BusinessRuleException("Solo los socios con plan PREMIUM pueden reservar clases grupales");
            }

            // 2.2: Verificar que no se haya alcanzado el aforo máximo
            long reservasConfirmadas = reservaRepository.countByClaseIdAndEstado(
                    clase.getIdClase(), EstadoReserva.CONFIRMADA
            );

            if (reservasConfirmadas >= clase.getAforoMaximo()) {
                throw new BusinessRuleException("La clase ha alcanzado su aforo máximo");
            }

            // 2.3: Para clases grupales, NO debe tener bono
            if (createDTO.getIdBono() != null) {
                throw new BusinessRuleException("Las clases grupales no requieren bono");
            }
        }

        // VALIDACIÓN 3: Si la clase es privada (esPrivada = true)
        Bono bono = null;
        if (clase.getEsPrivada()) {
            // 3.1: Debe proporcionar un bono
            if (createDTO.getIdBono() == null) {
                throw new BusinessRuleException("Las clases privadas requieren un bono");
            }

            bono = bonoRepository.findById(createDTO.getIdBono())
                    .orElseThrow(() -> new ResourceNotFoundException("Bono no encontrado con id: " + createDTO.getIdBono()));

            // 3.2: El bono debe pertenecer al socio que está haciendo la reserva
            if (!bono.getSocio().getIdSocio().equals(socio.getIdSocio())) {
                throw new BusinessRuleException(
                    "El bono no pertenece al socio que está realizando la reserva. " +
                    "El bono pertenece al socio con ID: " + bono.getSocio().getIdSocio() +
                    ", pero se está intentando reservar para el socio con ID: " + socio.getIdSocio()
                );
            }

            // 3.3: El entrenador de la clase debe coincidir con el entrenador del bono
            if (!clase.getEntrenador().getIdSocio().equals(bono.getEntrenador().getIdSocio())) {
                throw new BusinessRuleException(
                    "El entrenador de la clase no coincide con el entrenador del bono. " +
                    "La clase es impartida por el entrenador con ID: " + clase.getEntrenador().getIdSocio() +
                    ", pero el bono es para el entrenador con ID: " + bono.getEntrenador().getIdSocio()
                );
            }

            // 3.4: El bono debe estar activo
            if (!bono.getActivo()) {
                throw new BusinessRuleException("El bono no está activo");
            }

            // 3.5: El bono debe tener sesiones restantes
            if (bono.getSesionesRestantes() <= 0) {
                throw new BusinessRuleException("El bono no tiene sesiones restantes");
            }

            // 3.6: Validar que el entrenador asociado al bono esté activo
            if (!bono.getEntrenador().getEsActivo()) {
                throw new BusinessRuleException("El entrenador asociado al bono no está activo");
            }

            // 3.7: Decrementar sesión automáticamente
            bono.decrementarSesion();
            bonoRepository.save(bono);
        }

        // Crear la reserva
        Reserva reserva = new Reserva();
        reserva.setClase(clase);
        reserva.setSocio(socio);
        reserva.setBono(bono);
        reserva.setEstado(EstadoReserva.CONFIRMADA);

        Reserva savedReserva = reservaRepository.save(reserva);
        return convertToDTO(savedReserva);
    }

    public ReservaDTO cancelar(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));

        // VALIDACIÓN: Solo se pueden cancelar reservas confirmadas
        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new BusinessRuleException("Solo se pueden cancelar reservas confirmadas");
        }

        // VALIDACIÓN: Solo se puede cancelar como máximo 1 hora antes de la clase
        LocalDateTime fechaHoraClase = LocalDateTime.of(
                reserva.getClase().getFechaClase(),
                reserva.getClase().getHorario().getHoraInicio()
        );
        LocalDateTime horaLimiteCancelacion = fechaHoraClase.minusHours(1);

        if (LocalDateTime.now().isAfter(horaLimiteCancelacion)) {
            throw new BusinessRuleException("No se puede cancelar la reserva. Debe hacerlo al menos 1 hora antes de la clase");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);

        // Si es una clase privada con bono, devolver la sesión
        if (reserva.getBono() != null) {
            Bono bono = reserva.getBono();
            bono.setSesionesRestantes(bono.getSesionesRestantes() + 1);
            bono.setActivo(true); // Reactivar el bono si estaba inactivo
            bonoRepository.save(bono);
        }

        Reserva updatedReserva = reservaRepository.save(reserva);
        return convertToDTO(updatedReserva);
    }

    public void completarReservasAutomaticamente() {
        // Este método puede ser llamado por un scheduler para marcar como completadas
        // las reservas cuya hora de fin ya ha pasado
        LocalDateTime ahora = LocalDateTime.now();

        reservaRepository.findAll().stream()
                .filter(r -> r.getEstado() == EstadoReserva.CONFIRMADA)
                .filter(r -> {
                    LocalDateTime fechaHoraFin = LocalDateTime.of(
                            r.getClase().getFechaClase(),
                            r.getClase().getHorario().getHoraFin()
                    );
                    return ahora.isAfter(fechaHoraFin);
                })
                .forEach(r -> {
                    r.setEstado(EstadoReserva.COMPLETADA);
                    reservaRepository.save(r);
                });
    }

    public void delete(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reserva no encontrada con id: " + id);
        }
        reservaRepository.deleteById(id);
    }

    private ReservaDTO convertToDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setIdReserva(reserva.getIdReserva());
        dto.setIdClase(reserva.getClase().getIdClase());
        dto.setNombreClase(reserva.getClase().getNombre());
        dto.setIdSocio(reserva.getSocio().getIdSocio());
        dto.setNombreSocio(reserva.getSocio().getNombre() + " " + reserva.getSocio().getApellidos());
        dto.setFechaReservar(reserva.getFechaReservar());
        dto.setEstado(reserva.getEstado());

        if (reserva.getBono() != null) {
            dto.setIdBono(reserva.getBono().getIdBono());
        }

        // Obtener horario desde la clase
        if (reserva.getClase() != null && reserva.getClase().getHorario() != null) {
            HorarioDTO horarioDTO = new HorarioDTO();
            horarioDTO.setIdHorario(reserva.getClase().getHorario().getIdHorario());
            horarioDTO.setHoraInicio(reserva.getClase().getHorario().getHoraInicio());
            horarioDTO.setHoraFin(reserva.getClase().getHorario().getHoraFin());
            dto.setHorario(horarioDTO);
        }

        return dto;
    }
}
