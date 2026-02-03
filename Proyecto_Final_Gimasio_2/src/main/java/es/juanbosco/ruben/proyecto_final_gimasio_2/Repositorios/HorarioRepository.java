package es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.entidades.Horario;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
}