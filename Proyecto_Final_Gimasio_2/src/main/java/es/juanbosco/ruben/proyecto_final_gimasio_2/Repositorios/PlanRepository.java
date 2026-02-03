package es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios;

import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Plan;
import es.juanbosco.ruben.proyecto_final_gimasio_2.enums.TipoPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, TipoPlan> {
    Optional<Plan> findByTipo(TipoPlan tipo);
    boolean existsByTipo(TipoPlan tipo);
}