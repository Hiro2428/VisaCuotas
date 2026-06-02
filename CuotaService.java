package Service;

import Model.Cuota;
import Repository.CuotaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * Service: contiene toda la lógica de negocio.
 *
 * Aquí se realizan los cálculos financieros. No sabe nada de HTTP
 * (eso es trabajo del Controller), ni de dónde vienen los datos
 * (eso es trabajo del Repository).
 *
 * IMPORTANTE — Por qué usamos BigDecimal y no double:
 * Con double, Java representa 0.15 internamente como
 * 0.14999999999999999..., lo que produce errores de redondeo.
 * En cálculos financieros eso es inaceptable.
 * BigDecimal garantiza precisión exacta en decimales.
 *
 * @Service le dice a Spring que registre esta clase para inyección.
 */
@Service
public class CuotaService {

    // Spring inyecta automáticamente el Repository aquí.
    // No necesitamos hacer "new CuotaRepository()" manualmente.
    private final CuotaRepository cuotaRepository;

    public CuotaService(CuotaRepository cuotaRepository) {
        this.cuotaRepository = cuotaRepository;
    }

    /**
     * Devuelve la tabla de cuotas disponibles.
     * El frontend la usa para mostrar las opciones de pago.
     * Los cálculos de recargo/total/cuota mensual los hace el frontend
     * con estos datos.
     */
    public List<Cuota> obtenerCuotas() {
        return cuotaRepository.obtenerTodas();
    }

    /**
     * Calcula el desglose completo para un valor y una cantidad de meses.
     * El frontend llama a esto cuando el vendedor selecciona una cuota.
     *
     * Fórmulas (extraídas del Excel original del cliente):
     *   recargo  = valor × porcentaje
     *   total    = valor + recargo
     *   cuotaMensual = total ÷ meses
     *
     * @param valor  Precio base del producto
     * @param meses  Cantidad de cuotas seleccionadas
     * @return       Map con los campos: recargo, total, cuotaMensual, meses, porcentaje
     */
    public Map<String, Object> calcularDesglose(double valor, int meses) {

        // Buscar el porcentaje correspondiente a los meses solicitados
        List<Cuota> cuotas = cuotaRepository.obtenerTodas();

        Cuota cuotaSeleccionada = cuotas.stream()
                .filter(c -> c.getMeses() == meses)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    "No existe una opción de " + meses + " meses."
                ));

        // Usamos BigDecimal para precisión financiera exacta
        BigDecimal bdValor      = BigDecimal.valueOf(valor);
        BigDecimal bdPorcentaje = BigDecimal.valueOf(cuotaSeleccionada.getPorcentaje());
        BigDecimal bdMeses      = BigDecimal.valueOf(meses);

        BigDecimal recargo      = bdValor.multiply(bdPorcentaje)
                                         .setScale(2, RoundingMode.HALF_UP);

        BigDecimal total        = bdValor.add(recargo)
                                         .setScale(2, RoundingMode.HALF_UP);

        BigDecimal cuotaMensual = total.divide(bdMeses, 2, RoundingMode.HALF_UP);

        // Devolvemos un Map para que Spring lo serialice como JSON
        return Map.of(
            "meses",        meses,
            "porcentaje",   cuotaSeleccionada.getPorcentaje(),
            "recargo",      recargo,
            "total",        total,
            "cuotaMensual", cuotaMensual
        );
    }
}
