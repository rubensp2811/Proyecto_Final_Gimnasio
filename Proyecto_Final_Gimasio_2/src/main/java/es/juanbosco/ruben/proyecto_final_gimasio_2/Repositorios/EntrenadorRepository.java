package es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Entrenador;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {

    @Query("SELECT e FROM Entrenador e WHERE " +
            "(:especialidad IS NULL OR LOWER(e.especialidad) LIKE LOWER(CONCAT('%', :especialidad, '%')))")
    Page<Entrenador> findByEspecialidad(
            @Param("especialidad") String especialidad,
            Pageable pageable
    );
}