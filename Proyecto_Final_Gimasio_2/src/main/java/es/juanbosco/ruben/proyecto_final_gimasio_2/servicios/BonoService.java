package es.juanbosco.ruben.proyecto_final_gimasio_2.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.BonoCreateDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.BonoDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Excepciones.BusinessRuleException;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Excepciones.ResourceNotFoundException;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.BonoRepository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.EntrenadorRepository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.SocioRepository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Bono;
import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Entrenador;
import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Socio;

import java.util.List;
import java.util.stream.Collectors;

// Servicio para la gestión de bonos de socios y entrenadores
// Incluye validaciones de negocio y conversión a DTO
@Service
@Transactional
public class BonoService {

    // Repositorio de bonos
    @Autowired
    private BonoRepository bonoRepository;

    // Repositorio de socios
    @Autowired
    private SocioRepository socioRepository;

    // Repositorio de entrenadores
    @Autowired
    private EntrenadorRepository entrenadorRepository;

    // Devuelve todos los bonos paginados
    public Page<BonoDTO> findAll(Pageable pageable) {
        return bonoRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    // Busca bonos por filtros (socio, activo)
    public Page<BonoDTO> findByFiltros(Long idSocio, Boolean activo, Pageable pageable) {
        return bonoRepository.findByFiltros(idSocio, activo, pageable)
                .map(this::convertToDTO);
    }

    // Devuelve todos los bonos de un socio
    public List<BonoDTO> findBySocioId(Long idSocio) {
        return bonoRepository.findBySocioId(idSocio).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Busca bono por ID
    public BonoDTO findById(Long id) {
        Bono bono = bonoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bono no encontrado con id: " + id));
        return convertToDTO(bono);
    }

    // Crea un nuevo bono para un socio y entrenador
    public BonoDTO create(BonoCreateDTO createDTO) {
        Socio socio = socioRepository.findById(createDTO.getIdSocio())
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + createDTO.getIdSocio()));

        // Validar que el socio esté activo para comprar bonos
        if (!socio.getEsActivo()) {
            throw new BusinessRuleException("Un socio inactivo no puede comprar bonos");
        }

        Entrenador entrenador = entrenadorRepository.findById(createDTO.getIdEntrenador())
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado con id: " + createDTO.getIdEntrenador()));

        // Validar que el entrenador esté activo para asignar bonos
        if (!entrenador.getEsActivo()) {
            throw new BusinessRuleException("No se puede crear un bono con un entrenador inactivo");
        }

        Bono bono = new Bono();
        bono.setSocio(socio);
        bono.setSesionesTotales(10);
        bono.setSesionesRestantes(10);
        bono.setEntrenador(entrenador);

        Bono savedBono = bonoRepository.save(bono);
        return convertToDTO(savedBono);
    }

    public void delete(Long id) {
        if (!bonoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bono no encontrado con id: " + id);
        }
        bonoRepository.deleteById(id);
    }

    private BonoDTO convertToDTO(Bono bono) {
        BonoDTO dto = new BonoDTO();
        dto.setIdBono(bono.getIdBono());
        dto.setIdSocio(bono.getSocio().getIdSocio());
        dto.setNombreSocio(bono.getSocio().getNombre() + " " + bono.getSocio().getApellidos());
        dto.setSesionesTotales(bono.getSesionesTotales());
        dto.setSesionesRestantes(bono.getSesionesRestantes());
        dto.setIdEntrenador(bono.getEntrenador().getIdSocio());
        dto.setNombreEntrenador(bono.getEntrenador().getNombre() + " " + bono.getEntrenador().getApellidos());
        dto.setFechaCompra(bono.getFechaCompra());
        dto.setActivo(bono.getActivo());
        return dto;
    }
}
