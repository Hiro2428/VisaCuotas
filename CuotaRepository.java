package Repository;

import Model.Cuota;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository: única clase que "sabe" de dónde vienen los datos.
 *
 * Por ahora los datos están hardcodeados aquí mismo.
 * En el futuro, si se conecta una base de datos, solo se modifica
 * este archivo — el Service y el Controller no se tocan.
 *
 * @Repository le dice a Spring que registre esta clase para poder
 * inyectarla automáticamente donde se necesite (ver CuotaService).
 */
@Repository
public class CuotaRepository {

    /**
     * Devuelve la tabla completa de cuotas con sus porcentajes de recargo.
     * Estos valores provienen del Excel original del cliente.
     */
    public List<Cuota> obtenerTodas() {
        return List.of(
            new Cuota(12, 0.15),
            new Cuota(15, 0.15),
            new Cuota(18, 0.18),
            new Cuota(24, 0.25),
            new Cuota(36, 0.30),
            new Cuota(48, 0.30)
        );
    }
}
