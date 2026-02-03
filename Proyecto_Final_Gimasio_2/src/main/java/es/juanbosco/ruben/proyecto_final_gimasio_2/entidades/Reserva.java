package es.juanbosco.ruben.proyecto_final_gimasio_2.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import es.juanbosco.ruben.proyecto_final_gimasio_2.enums.EstadoReserva;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    @ManyToOne
    @JoinColumn(name = "id_clase", nullable = false)
    @JsonIgnoreProperties("reservas")
    private Clase clase;

    @ManyToOne
    @JoinColumn(name = "id_socio", nullable = false)
    @JsonIgnoreProperties({"reservas", "bonos"})
    private Socio socio;

    @Column(nullable = false)
    private LocalDateTime fechaReservar;

    @ManyToOne
    @JoinColumn(name = "id_bono", nullable = true)
    @JsonIgnoreProperties("reservas")
    private Bono bono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado = EstadoReserva.CONFIRMADA;


    @PrePersist
    protected void onCreate() {
        if (fechaReservar == null) {
            fechaReservar = LocalDateTime.now();
        }
        if (estado == null) {
            estado = EstadoReserva.CONFIRMADA;
        }
    }
}