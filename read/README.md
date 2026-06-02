# Visa Cuotas

Herramienta web para calcular cuotas de financiamiento con tarjeta Visa. Reemplaza la hoja de Excel del vendedor con una interfaz rápida, clara y con exportación a PDF.

---

## Estructura del repositorio

```
visacuotas/
├── frontend/
│   └── index.html
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/visacuotas/api/
│   │       │   ├── ApiApplication.java          ← generado por Spring Initializr
│   │       │   ├── model/Cuota.java
│   │       │   ├── repository/CuotaRepository.java
│   │       │   ├── service/CuotaService.java
│   │       │   └── controller/CuotaController.java
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml                                  ← generado por Spring Initializr
└── README.md
```

---

## Requisitos

- Java 17 o 21
- Maven (incluido en el proyecto como `mvnw`)
- Navegador moderno para el frontend

---

## Cómo correr el backend

```bash
cd backend
./mvnw spring-boot:run
```

El servidor arranca en `http://localhost:8080`.

### Endpoints disponibles

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/cuotas` | Devuelve la tabla de cuotas y porcentajes |
| GET | `/api/calcular?valor=10000&meses=12` | Devuelve el desglose calculado |

### Ejemplo de respuesta — `/api/cuotas`

```json
[
  { "meses": 12, "porcentaje": 0.15 },
  { "meses": 15, "porcentaje": 0.15 },
  { "meses": 18, "porcentaje": 0.18 },
  { "meses": 24, "porcentaje": 0.25 },
  { "meses": 36, "porcentaje": 0.30 },
  { "meses": 48, "porcentaje": 0.30 }
]
```

### Ejemplo de respuesta — `/api/calcular?valor=10000&meses=12`

```json
{
  "meses": 12,
  "porcentaje": 0.15,
  "recargo": 1500.00,
  "total": 11500.00,
  "cuotaMensual": 958.33
}
```

---

## Cómo correr el frontend

1. Abre `frontend/index.html` directamente en el navegador
2. Para conectarlo al backend, edita esta línea en el `<script>` del archivo:

```js
// Cambiar null por la URL del backend
const API_URL = 'http://localhost:8080/api/cuotas';
```

Sin backend activo, la app funciona con valores locales (fallback automático).

---

## Funcionalidades

- Ingreso del valor del producto
- Cálculo automático de recargo, total y cuota mensual para 6 planes de pago
- Selección de cuota con resumen detallado
- Exportación a PDF de la cuota seleccionada

---

## Tabla de recargos

| Cuotas | Recargo |
|--------|---------|
| 12 meses | 15% |
| 15 meses | 15% |
| 18 meses | 18% |
| 24 meses | 25% |
| 36 meses | 30% |
| 48 meses | 30% |
