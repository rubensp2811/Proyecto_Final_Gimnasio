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
@Table(name = "socios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSocio;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 15)
    private String telefono;

    @Column(nullable = false)
    private LocalDate fechaAlta;

    @Column(nullable = false)
    private Boolean esActivo = true;

    @ManyToOne
    @JoinColumn(name = "tipoPlan", referencedColumnName = "tipo")
    @JsonIgnoreProperties("socios")
    private Plan plan;

    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("socio")
    private List<Reserva> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("socio")
    private List<Bono> bonos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (fechaAlta == null) {
            fechaAlta = LocalDate.now();
        }
        if (esActivo == null) {
            esActivo = true;
        }
    }
}