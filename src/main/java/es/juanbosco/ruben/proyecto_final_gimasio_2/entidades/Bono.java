package es.juanbosco.ruben.proyecto_final_gimasio_2.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bonos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBono;

    @ManyToOne
    @JoinColumn(name = "id_socio", nullable = false)
    @JsonIgnoreProperties({"bonos", "reservas"})
    private Socio socio;

    @Column(nullable = false)
    private Integer sesionesTotales;

    @Column(nullable = false)
    private Integer sesionesRestantes;

    @ManyToOne
    @JoinColumn(name = "id_entrenador", nullable = false)
    @JsonIgnoreProperties("bonos")
    private Entrenador entrenador;

    @Column(nullable = false)
    private LocalDate fechaCompra;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "bono", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("bono")
    private List<Reserva> reservas = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (fechaCompra == null) {
            fechaCompra = LocalDate.now();
        }
        if (activo == null) {
            activo = true;
        }
        if (sesionesRestantes == null) {
            sesionesRestantes = sesionesTotales;
        }
    }

    public void decrementarSesion() {
        if (sesionesRestantes > 0) {
            sesionesRestantes--;
            if (sesionesRestantes == 0) {
                activo = false;
            }
        }
    }
}