package Model;

/**
 * Modelo que representa una opción de cuota.
 * Solo define la estructura del objeto — nada más.
 * Spring usa los getters para convertir este objeto a JSON automáticamente.
 */
public class Cuota {

    private int meses;
    private double porcentaje;

    // Constructor: la única forma de crear una Cuota es dando ambos valores.
    // Esto evita que exista un objeto Cuota incompleto.
    public Cuota(int meses, double porcentaje) {
        this.meses = meses;
        this.porcentaje = porcentaje;
    }

    // Getters — Spring los necesita para serializar el objeto a JSON.
    // Sin ellos, la respuesta sería un objeto vacío {}.
    public int getMeses() {
        return meses;
    }

    public double getPorcentaje() {
        return porcentaje;
    }
}
