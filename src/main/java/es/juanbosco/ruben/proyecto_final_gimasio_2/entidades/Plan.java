package es.juanbosco.ruben.proyecto_final_gimasio_2.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import es.juanbosco.ruben.proyecto_final_gimasio_2.enums.TipoPlan;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "planes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plan {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, unique = true, columnDefinition = "ENUM('BASICO','PREMIUM')")
    private TipoPlan tipo;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<Socio> socios = new ArrayList<>();
}