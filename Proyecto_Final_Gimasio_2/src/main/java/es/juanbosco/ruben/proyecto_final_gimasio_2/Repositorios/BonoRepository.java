package es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios;

import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Bono;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonoRepository extends JpaRepository<Bono, Long> {

    @Query("SELECT b FROM Bono b WHERE b.socio.idSocio = :idSocio")
    List<Bono> findBySocioId(@Param("idSocio") Long idSocio);

    @Query("SELECT b FROM Bono b WHERE b.entrenador.idSocio = :idEntrenador")
    List<Bono> findByEntrenadorId(@Param("idEntrenador") Long idEntrenador);

    @Query("SELECT b FROM Bono b WHERE " +
            "(:idSocio IS NULL OR b.socio.idSocio = :idSocio) AND " +
            "(:activo IS NULL OR b.activo = :activo)")
    Page<Bono> findByFiltros(
            @Param("idSocio") Long idSocio,
            @Param("activo") Boolean activo,
            Pageable pageable
    );
}