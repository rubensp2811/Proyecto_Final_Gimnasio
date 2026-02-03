package es.juanbosco.ruben.proyecto_final_gimasio_2.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClase;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private LocalDate fechaClase;

    @Column(nullable = false)
    private Boolean esPrivada = false;

    @Column
    private Integer aforoMaximo;

    @Column(nullable = false)
    private Integer duracion;

    @ManyToOne
    @JoinColumn(name = "id_horario", nullable = false)
    @JsonIgnoreProperties("clases")
    private Horario horario;

    @ManyToOne
    @JoinColumn(name = "id_entrenador", nullable = false)
    @JsonIgnoreProperties("clases")
    private Entrenador entrenador;

    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("clase")
    private List<Reserva> reservas = new ArrayList<>();

    @PrePersist
    @PreUpdate
    protected void validate() {
        // Calcular y asignar la duración automáticamente desde el horario
        if (horario != null && horario.getHoraInicio() != null && horario.getHoraFin() != null) {
            Duration duration = Duration.between(horario.getHoraInicio(), horario.getHoraFin());
            this.duracion = (int) duration.toMinutes();
        }

        // Validar que la duración calculada esté entre 15 y 180 minutos
        if (duracion < 15 || duracion > 180) {
            throw new IllegalArgumentException("La duración calculada debe estar entre 15 y 180 minutos");
        }

        // Si es clase privada, el aforo máximo es siempre 1
        if (esPrivada) {
            aforoMaximo = 1;
        } else {
            // Si es clase grupal, debe tener aforo máximo válido
            if (aforoMaximo == null || aforoMaximo <= 0) {
                throw new IllegalArgumentException("Las clases grupales deben tener aforo máximo válido");
            }
        }
    }
}