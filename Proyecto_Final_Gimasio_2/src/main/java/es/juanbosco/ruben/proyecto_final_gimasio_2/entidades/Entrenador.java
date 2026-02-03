package es.juanbosco.ruben.proyecto_final_gimasio_2.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entrenadores")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "id_socio")
public class Entrenador extends Socio {

    @Column(nullable = false, length = 100)
    private String especialidad;

    @OneToMany(mappedBy = "entrenador", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("entrenador")
    private List<Bono> bonos = new ArrayList<>();

    @OneToMany(mappedBy = "entrenador", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("entrenador")
    private List<Clase> clases = new ArrayList<>();
}