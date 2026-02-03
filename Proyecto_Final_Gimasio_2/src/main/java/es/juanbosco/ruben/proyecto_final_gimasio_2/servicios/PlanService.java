package es.juanbosco.ruben.proyecto_final_gimasio_2.servicios;

import es.juanbosco.ruben.proyecto_final_gimasio_2.DTO.PlanDTO;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Excepciones.ResourceNotFoundException;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.PlanRepository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Plan;
import es.juanbosco.ruben.proyecto_final_gimasio_2.enums.TipoPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Servicio para la gestión de planes de membresía
// Incluye conversión a DTO y búsqueda por tipo
@Service
@Transactional
public class PlanService {
    // Repositorio de planes
    @Autowired
    private PlanRepository planRepository;

    // Devuelve todos los planes
    public List<PlanDTO> findAll() {
        return planRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Busca plan por tipo (clave primaria)
    public PlanDTO findById(TipoPlan tipo) {
        Plan plan = planRepository.findById(tipo)
                .orElseThrow(() -> new ResourceNotFoundException("Plan no encontrado con tipo: " + tipo));
        return convertToDTO(plan);
    }

    // Busca plan por tipo (campo único)
    public PlanDTO findByTipo(TipoPlan tipo) {
        Plan plan = planRepository.findByTipo(tipo)
                .orElseThrow(() -> new ResourceNotFoundException("Plan no encontrado con tipo: " + tipo));
        return convertToDTO(plan);
    }

    private PlanDTO convertToDTO(Plan plan) {
        PlanDTO dto = new PlanDTO();
        dto.setTipo(plan.getTipo());
        return dto;
    }
}