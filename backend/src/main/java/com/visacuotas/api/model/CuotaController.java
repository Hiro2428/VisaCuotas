package Controller;

import Model.Cuota;
import Service.CuotaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller: única clase que habla HTTP.
 *
 * Recibe peticiones del frontend, las delega al Service,
 * y devuelve la respuesta en JSON. No contiene lógica de negocio.
 *
 * @RestController  = @Controller + @ResponseBody combinados.
 *                   Le dice a Spring que este clase maneja
 *                   peticiones REST y que las respuestas
 *                   se serializan automáticamente a JSON.
 *
 * @RequestMapping  define el prefijo de todas las rutas de esta clase.
 *
 * @CrossOrigin     permite que el frontend (corriendo en otro puerto
 *                  o dominio) pueda hacer peticiones a esta API.
 *                  Sin esto, el navegador bloquea las peticiones
 *                  por política CORS.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CuotaController {

    // Spring inyecta el Service automáticamente.
    private final CuotaService cuotaService;

    public CuotaController(CuotaService cuotaService) {
        this.cuotaService = cuotaService;
    }

    /**
     * GET /api/cuotas
     *
     * Devuelve la tabla de opciones de cuotas disponibles.
     * El frontend la consume al cargar la página para mostrar
     * las tarjetas de opciones de pago.
     *
     * Respuesta esperada:
     * [
     *   { "meses": 12, "porcentaje": 0.15 },
     *   { "meses": 15, "porcentaje": 0.15 },
     *   ...
     * ]
     */
    @GetMapping("/cuotas")
    public ResponseEntity<List<Cuota>> obtenerCuotas() {
        List<Cuota> cuotas = cuotaService.obtenerCuotas();
        return ResponseEntity.ok(cuotas);
    }

    /**
     * GET /api/calcular?valor=10000&meses=12
     *
     * Calcula el desglose completo para un valor y cuotas dados.
     * El frontend puede llamar a esto como alternativa a calcular en JS,
     * útil cuando se quiera centralizar 100% la lógica en el backend.
     *
     * Respuesta esperada:
     * {
     *   "meses": 12,
     *   "porcentaje": 0.15,
     *   "recargo": 1500.00,
     *   "total": 11500.00,
     *   "cuotaMensual": 958.33
     * }
     *
     * @RequestParam lee los parámetros de la URL (?valor=...&meses=...)
     */
    @GetMapping("/calcular")
    public ResponseEntity<?> calcular(
            @RequestParam double valor,
            @RequestParam int meses) {

        // Validación básica antes de pasar al Service
        if (valor <= 0) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("error", "El valor debe ser mayor a 0."));
        }

        if (meses <= 0) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("error", "Los meses deben ser mayor a 0."));
        }

        try {
            Map<String, Object> resultado = cuotaService.calcularDesglose(valor, meses);
            return ResponseEntity.ok(resultado);

        } catch (IllegalArgumentException e) {
            // El Service lanza esta excepción si los meses no existen en la tabla
            return ResponseEntity
                .badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}
