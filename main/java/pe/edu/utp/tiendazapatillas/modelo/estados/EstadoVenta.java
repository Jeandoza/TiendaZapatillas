package pe.edu.utp.tiendazapatillas.modelo.estados;

/**
 * [STATE] Interfaz State.
 * Define los métodos que representan las transiciones de estado.
 */
public interface EstadoVenta {
    void registrar(ContextoVenta contexto);
    void pagar(ContextoVenta contexto);
    void anular(ContextoVenta contexto);
}
