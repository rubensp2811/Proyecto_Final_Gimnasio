package es.juanbosco.ruben.proyecto_final_gimasio_2.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.HorarioCreateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.HorarioDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Excepciones.BusinessRuleException;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Excepciones.ResourceNotFoundException;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.HorarioRepository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Horario;

import java.util.List;
import java.util.stream.Collectors;

// Servicio para la gestión de horarios de clases
// Incluye validaciones de negocio y conversión a DTO
@Service
@Transactional
public class HorarioService {
    // Repositorio de horarios
    @Autowired
    private HorarioRepository horarioRepository;

    // Devuelve todos los horarios sin paginación
    public List<HorarioDTO> findAll() {
        return horarioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Devuelve todos los horarios paginados
    public Page<HorarioDTO> findAll(Pageable pageable) {
        return horarioRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    // Busca horario por ID
    public HorarioDTO findById(Long id) {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado con id: " + id));
        return convertToDTO(horario);
    }

    // Crea un nuevo horario con validación de horas
    public HorarioDTO create(HorarioCreateDTO createDTO) {
        // Validación: hora de fin debe ser posterior a la de inicio
        if (createDTO.getHoraFin().isBefore(createDTO.getHoraInicio()) ||
                createDTO.getHoraFin().equals(createDTO.getHoraInicio())) {
            throw new BusinessRuleException("La hora de fin debe ser posterior a la hora de inicio");
        }
        Horario horario = new Horario();
        horario.setHoraInicio(createDTO.getHoraInicio());
        horario.setHoraFin(createDTO.getHoraFin());
        Horario savedHorario = horarioRepository.save(horario);
        return convertToDTO(savedHorario);
    }

    public void delete(Long id) {
        if (!horarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Horario no encontrado con id: " + id);
        }
        horarioRepository.deleteById(id);
    }

    private HorarioDTO convertToDTO(Horario horario) {
        HorarioDTO dto = new HorarioDTO();
        dto.setIdHorario(horario.getIdHorario());
        dto.setHoraInicio(horario.getHoraInicio());
        dto.setHoraFin(horario.getHoraFin());
        return dto;
    }
}
