package es.juanbosco.ruben.proyecto_final_gimasio_2.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.EntrenadorCreateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.EntrenadorDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.EntrenadorUpdateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Excepciones.BusinessRuleException;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Excepciones.ResourceNotFoundException;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.EntrenadorRepository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.SocioRepository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Entrenador;

// Servicio para la gestión de entrenadores
// Incluye validaciones de negocio y conversión a DTO
@Service
@Transactional
public class EntrenadorService {

    // Repositorio de entrenadores
    @Autowired
    private EntrenadorRepository entrenadorRepository;

    // Repositorio de socios
    @Autowired
    private SocioRepository socioRepository;

    // Devuelve todos los entrenadores paginados
    public Page<EntrenadorDTO> findAll(Pageable pageable) {
        return entrenadorRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    // Busca entrenadores por especialidad
    public Page<EntrenadorDTO> findByEspecialidad(String especialidad, Pageable pageable) {
        return entrenadorRepository.findByEspecialidad(especialidad, pageable)
                .map(this::convertToDTO);
    }

    // Busca entrenador por ID
    public EntrenadorDTO findById(Long id) {
        Entrenador entrenador = entrenadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado con id: " + id));
        return convertToDTO(entrenador);
    }

    // Crea un nuevo entrenador con validación de email único
    public EntrenadorDTO create(EntrenadorCreateDTO createDTO) {
        // Validar email único
        if (socioRepository.existsByEmail(createDTO.getEmail())) {
            throw new BusinessRuleException("Ya existe un socio/entrenador con el email: " + createDTO.getEmail());
        }

        Entrenador entrenador = new Entrenador();
        entrenador.setNombre(createDTO.getNombre());
        entrenador.setApellidos(createDTO.getApellidos());
        entrenador.setEmail(createDTO.getEmail());
        entrenador.setTelefono(createDTO.getTelefono());
        entrenador.setEspecialidad(createDTO.getEspecialidad());
        entrenador.setPlan(null); // Los entrenadores no tienen plan

        Entrenador savedEntrenador = entrenadorRepository.save(entrenador);
        return convertToDTO(savedEntrenador);
    }

    public void delete(Long id) {
        if (!entrenadorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Entrenador no encontrado con id: " + id);
        }
        entrenadorRepository.deleteById(id);
    }

    public EntrenadorDTO update(Long id, EntrenadorUpdateDTO dto) {
        Entrenador entrenador = entrenadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado con id: " + id));
        // No permitir cambiar el id ni plan
        entrenador.setNombre(dto.getNombre());
        entrenador.setApellidos(dto.getApellidos());
        entrenador.setEmail(dto.getEmail());
        entrenador.setTelefono(dto.getTelefono());
        entrenador.setEsActivo(dto.getEsActivo());
        entrenador.setEspecialidad(dto.getEspecialidad());
        entrenador.setPlan(null); // tipoPlan siempre null
        Entrenador actualizado = entrenadorRepository.save(entrenador);
        return convertToDTO(actualizado);
    }

    public EntrenadorDTO darDeBaja(Long id) {
        Entrenador entrenador = entrenadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado con id: " + id));
        entrenador.setEsActivo(false);
        Entrenador actualizado = entrenadorRepository.save(entrenador);
        return convertToDTO(actualizado);
    }

    private EntrenadorDTO convertToDTO(Entrenador entrenador) {
        EntrenadorDTO dto = new EntrenadorDTO();
        dto.setIdSocio(entrenador.getIdSocio());
        dto.setNombre(entrenador.getNombre());
        dto.setApellidos(entrenador.getApellidos());
        dto.setEmail(entrenador.getEmail());
        dto.setTelefono(entrenador.getTelefono());
        dto.setFechaAlta(entrenador.getFechaAlta());
        dto.setEsActivo(entrenador.getEsActivo());
        dto.setEspecialidad(entrenador.getEspecialidad());
        return dto;
    }
}
