package es.juanbosco.ruben.proyecto_final_gimasio_2.servicios;

import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.SocioCreateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.SocioDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.SocioUpdateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Excepciones.BusinessRuleException;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Excepciones.ResourceNotFoundException;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.PlanRepository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.SocioRepository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Plan;
import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Socio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SocioService {

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private PlanRepository planRepository;

    public Page<SocioDTO> findAll(Pageable pageable) {
        return socioRepository.findOnlySocios(pageable)
                .map(this::convertToDTO);
    }

    public Page<SocioDTO> findAllActivos(Pageable pageable) {
        return socioRepository.findAllActivos(pageable)
                .map(this::convertToDTO);
    }

    public Page<SocioDTO> findByFiltros(String nombre, String email, Boolean esActivo, Pageable pageable) {
        return socioRepository.findByFiltros(nombre, email, esActivo, pageable)
                .map(this::convertToDTO);
    }

    public SocioDTO findById(Long id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));
        return convertToDTO(socio);
    }

    public SocioDTO create(SocioCreateDTO createDTO) {
        // Validar email único
        if (socioRepository.existsByEmail(createDTO.getEmail())) {
            throw new BusinessRuleException("Ya existe un socio con el email: " + createDTO.getEmail());
        }

        Plan plan = planRepository.findByTipo(createDTO.getTipoPlan())
                .orElseThrow(() -> new ResourceNotFoundException("Plan no encontrado con tipo: " + createDTO.getTipoPlan()));

        Socio socio = new Socio();
        socio.setNombre(createDTO.getNombre());
        socio.setApellidos(createDTO.getApellidos());
        socio.setEmail(createDTO.getEmail());
        socio.setTelefono(createDTO.getTelefono());
        socio.setPlan(plan);

        Socio savedSocio = socioRepository.saveAndFlush(socio);
        return convertToDTO(savedSocio);
    }

    public SocioDTO update(Long id, SocioUpdateDTO updateDTO) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));

        // Si se quiere actualizar el email, verificar que sea único
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(socio.getEmail())) {
            if (socioRepository.existsByEmail(updateDTO.getEmail())) {
                throw new BusinessRuleException("Ya existe un socio con el email: " + updateDTO.getEmail());
            }
            socio.setEmail(updateDTO.getEmail());
        }

        if (updateDTO.getNombre() != null) {
            socio.setNombre(updateDTO.getNombre());
        }
        if (updateDTO.getApellidos() != null) {
            socio.setApellidos(updateDTO.getApellidos());
        }
        if (updateDTO.getTelefono() != null) {
            socio.setTelefono(updateDTO.getTelefono());
        }

        // Cambio de plan por tipo: solo si el socio está activo
        if (updateDTO.getTipoPlan() != null) {
            if (!socio.getEsActivo()) {
                throw new BusinessRuleException("Solo los socios activos pueden cambiar de plan");
            }
            Plan plan = planRepository.findByTipo(updateDTO.getTipoPlan())
                    .orElseThrow(() -> new ResourceNotFoundException("Plan no encontrado con tipo: " + updateDTO.getTipoPlan()));
            socio.setPlan(plan);
        }

        if (updateDTO.getEsActivo() != null) {
            socio.setEsActivo(updateDTO.getEsActivo());
        }

        Socio updatedSocio = socioRepository.save(socio);
        return convertToDTO(updatedSocio);
    }

    public void delete(Long id) {
        if (!socioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Socio no encontrado con id: " + id);
        }
        socioRepository.deleteById(id);
    }

    public SocioDTO darDeBaja(Long id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));

        socio.setEsActivo(false);
        Socio updatedSocio = socioRepository.save(socio);
        return convertToDTO(updatedSocio);
    }

    private SocioDTO convertToDTO(Socio socio) {
        SocioDTO dto = new SocioDTO();
        dto.setIdSocio(socio.getIdSocio());
        dto.setNombre(socio.getNombre());
        dto.setApellidos(socio.getApellidos());
        dto.setEmail(socio.getEmail());
        dto.setTelefono(socio.getTelefono());
        dto.setFechaAlta(socio.getFechaAlta());
        dto.setEsActivo(socio.getEsActivo());
        if (socio.getPlan() != null) {
            dto.setTipoPlan(socio.getPlan().getTipo().name());
        }
        return dto;
    }
}