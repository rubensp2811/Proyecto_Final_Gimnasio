package es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios;

import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Clase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {

    boolean existsByNombre(String nombre);

    @Query("SELECT c FROM Clase c WHERE " +
            "(:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:fechaClase IS NULL OR c.fechaClase = :fechaClase) AND " +
            "(:esPrivada IS NULL OR c.esPrivada = :esPrivada)")
    Page<Clase> findByFiltros(
            @Param("nombre") String nombre,
            @Param("fechaClase") LocalDate fechaClase,
            @Param("esPrivada") Boolean esPrivada,
            Pageable pageable
    );

    @Query("SELECT c FROM Clase c WHERE c.esPrivada = false " +
            "ORDER BY SIZE(c.reservas) DESC")
    List<Clase> findClasesPopulares(Pageable pageable);
}