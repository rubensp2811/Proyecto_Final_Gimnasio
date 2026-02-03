package es.juanbosco.ruben.proyecto_final_gimasio_2.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.ClaseCreateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.ClaseDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.HorarioDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Excepciones.BusinessRuleException;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Excepciones.ResourceNotFoundException;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.ClaseRepository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.HorarioRepository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.EntrenadorRepository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.ReservaRepository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Clase;
import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Horario;
import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Entrenador;
import es.juanbosco.ruben.proyecto_final_gimasio_2.enums.EstadoReserva;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// Servicio para la gestión de clases del gimnasio
// Incluye validaciones de negocio y conversión a DTO
@Service
@Transactional
public class ClaseService {
    // Repositorio de clases
    @Autowired
    private ClaseRepository claseRepository;
    // Repositorio de horarios
    @Autowired
    private HorarioRepository horarioRepository;
    // Repositorio de entrenadores
    @Autowired
    private EntrenadorRepository entrenadorRepository;
    // Repositorio de reservas
    @Autowired
    private ReservaRepository reservaRepository;

    // Devuelve todas las clases sin paginación
    public List<ClaseDTO> findAll() {
        return claseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Devuelve todas las clases paginadas
    public Page<ClaseDTO> findAll(Pageable pageable) {
        return claseRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    // Busca clases por filtros (nombre, fecha, tipo)
    public Page<ClaseDTO> findByFiltros(String nombre, LocalDate fechaClase, Boolean esPrivada, Pageable pageable) {
        return claseRepository.findByFiltros(nombre, fechaClase, esPrivada, pageable)
                .map(this::convertToDTO);
    }

    // Devuelve las clases más populares
    public List<ClaseDTO> findClasesPopulares(int limit) {
        return claseRepository.findClasesPopulares(PageRequest.of(0, limit)).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Busca clase por ID
    public ClaseDTO findById(Long id) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clase no encontrada con id: " + id));
        return convertToDTO(clase);
    }

    // Crea una nueva clase con validaciones
    public ClaseDTO create(ClaseCreateDTO createDTO) {
        // Validación: nombre único
        if (claseRepository.existsByNombre(createDTO.getNombre())) {
            throw new BusinessRuleException("Ya existe una clase con el nombre: " + createDTO.getNombre());
        }
        // Obtener horario y entrenador
        Horario horario = horarioRepository.findById(createDTO.getIdHorario())
                .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado con id: " + createDTO.getIdHorario()));

        Entrenador entrenador = entrenadorRepository.findById(createDTO.getIdEntrenador())
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado con id: " + createDTO.getIdEntrenador()));

        // Validar aforo para clases grupales
        if (!createDTO.getEsPrivada() && (createDTO.getAforoMaximo() == null || createDTO.getAforoMaximo() <= 0)) {
            throw new BusinessRuleException("Las clases grupales deben tener un aforo máximo válido");
        }

        Clase clase = new Clase();
        clase.setNombre(createDTO.getNombre());
        clase.setDescripcion(createDTO.getDescripcion());
        clase.setFechaClase(createDTO.getFechaClase());
        clase.setEsPrivada(createDTO.getEsPrivada());
        clase.setHorario(horario);
        clase.setEntrenador(entrenador);

        // Si es privada, aforoMaximo será 1 (se establece automáticamente en @PrePersist)
        // Si es grupal, establecer el aforoMaximo del DTO
        if (!createDTO.getEsPrivada()) {
            clase.setAforoMaximo(createDTO.getAforoMaximo());
        }

        Clase savedClase = claseRepository.save(clase);
        return convertToDTO(savedClase);
    }

    public void delete(Long id) {
        if (!claseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Clase no encontrada con id: " + id);
        }
        claseRepository.deleteById(id);
    }

    private ClaseDTO convertToDTO(Clase clase) {
        ClaseDTO dto = new ClaseDTO();
        dto.setIdClase(clase.getIdClase());
        dto.setNombre(clase.getNombre());
        dto.setDescripcion(clase.getDescripcion());
        dto.setFechaClase(clase.getFechaClase());
        dto.setDuracion(clase.getDuracion()); // Calculada automáticamente desde el horario
        dto.setEsPrivada(clase.getEsPrivada());
        dto.setAforoMaximo(clase.getAforoMaximo());

        if (clase.getHorario() != null) {
            HorarioDTO horarioDTO = new HorarioDTO();
            horarioDTO.setIdHorario(clase.getHorario().getIdHorario());
            horarioDTO.setHoraInicio(clase.getHorario().getHoraInicio());
            horarioDTO.setHoraFin(clase.getHorario().getHoraFin());
            dto.setHorario(horarioDTO);
        }

        if (clase.getEntrenador() != null) {
            dto.setIdEntrenador(clase.getEntrenador().getIdSocio());
            dto.setNombreEntrenador(clase.getEntrenador().getNombre());
        }

        // Contar reservas confirmadas
        long reservasConfirmadas = reservaRepository.countByClaseIdAndEstado(
                clase.getIdClase(), EstadoReserva.CONFIRMADA
        );
        dto.setReservasConfirmadas((int) reservasConfirmadas);

        return dto;
    }
}