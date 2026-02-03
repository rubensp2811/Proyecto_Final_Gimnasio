package es.juanbosco.ruben.proyecto_final_gimasio_2.entidades;

import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.PlanRepository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.Repositorios.SocioRepository;
import es.juanbosco.ruben.proyecto_final_gimasio_2.enums.TipoPlan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
@Component → Spring detecta y gestiona la clase.
implements ApplicationRunner → Spring ejecuta el método run() al iniciar la app.

Comprobar si existen los dos planes en la bd y si no los crea al iniciar el programa.
* */
@Component
public class PlanDataInitializer implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(PlanDataInitializer.class);
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private SocioRepository socioRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        logger.info("Ejecutando PlanDataInitializer...");
        ensurePlan(TipoPlan.BASICO);
        ensurePlan(TipoPlan.PREMIUM);
        logger.info("Planes iniciales comprobados/creados.");
    }

    private void ensurePlan(TipoPlan tipo) {
        Optional<Plan> planOpt = planRepository.findById(tipo);
        Plan plan;
        if (planOpt.isPresent()) {
            plan = planOpt.get();
            logger.info("Plan {} ya existe.", tipo);
        } else {
            plan = new Plan();
            plan.setTipo(tipo);
            planRepository.save(plan);
            logger.info("Plan {} creado.", tipo);
        }
        // Reasignar los socios que tengan un plan de ese tipo pero no el correcto (por si hay duplicados antiguos)
        List<Socio> socios = socioRepository.findAll();
        for (Socio socio : socios) {
            if (socio.getPlan() != null && socio.getPlan().getTipo() == tipo && socio.getPlan() != plan) {
                socio.setPlan(plan);
                socioRepository.save(socio);
            }
        }
        // Eliminado el bloque que borraba planes duplicados para evitar eliminar los planes recién creados
    }
}
