package es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios;

import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Socio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Long> {

    Optional<Socio> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT s FROM Socio s WHERE s.esActivo = true")
    Page<Socio> findAllActivos(Pageable pageable);

    @Query("SELECT s FROM Socio s WHERE " +
            "(:nombre IS NULL OR LOWER(s.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:email IS NULL OR LOWER(s.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:esActivo IS NULL OR s.esActivo = :esActivo)")
    Page<Socio> findByFiltros(
            @Param("nombre") String nombre,
            @Param("email") String email,
            @Param("esActivo") Boolean esActivo,
            Pageable pageable
    );

    @Query("SELECT s FROM Socio s WHERE TYPE(s) = Socio")
    Page<Socio> findOnlySocios(Pageable pageable);
}