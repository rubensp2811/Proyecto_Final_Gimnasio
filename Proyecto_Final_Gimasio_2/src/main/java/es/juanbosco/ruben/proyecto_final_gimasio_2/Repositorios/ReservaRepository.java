package es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Reserva;
import es.juanbosco.ruben.proyecto_final_gimasio_2.enums.EstadoReserva;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.clase.idClase = :idClase AND r.estado = :estado")
    long countByClaseIdAndEstado(@Param("idClase") Long idClase, @Param("estado") EstadoReserva estado);

    @Query("SELECT r FROM Reserva r WHERE r.socio.idSocio = :idSocio")
    List<Reserva> findBySocioId(@Param("idSocio") Long idSocio);

    @Query("SELECT r FROM Reserva r WHERE r.clase.idClase = :idClase")
    List<Reserva> findByClaseId(@Param("idClase") Long idClase);

    @Query("SELECT r FROM Reserva r WHERE " +
            "(:idSocio IS NULL OR r.socio.idSocio = :idSocio) AND " +
            "(:estado IS NULL OR r.estado = :estado)")
    Page<Reserva> findByFiltros(
            @Param("idSocio") Long idSocio,
            @Param("estado") EstadoReserva estado,
            Pageable pageable
    );

    // Verifica si ya existe una reserva confirmada para un socio en una clase específica
    boolean existsByClaseIdClaseAndSocioIdSocioAndEstado(Long idClase, Long idSocio, EstadoReserva estado);
}