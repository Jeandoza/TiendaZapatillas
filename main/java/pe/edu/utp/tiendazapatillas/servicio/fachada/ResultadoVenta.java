package pe.edu.utp.tiendazapatillas.servicio.fachada;

import pe.edu.utp.tiendazapatillas.modelo.entidades.VentaHistorial;

// Un registro para devolver un resultado compuesto desde la fachada.
public record ResultadoVenta(VentaHistorial historial, String contenidoComprobante) {}
