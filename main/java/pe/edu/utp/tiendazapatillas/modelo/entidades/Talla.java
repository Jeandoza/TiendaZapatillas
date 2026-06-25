package pe.edu.utp.tiendazapatillas.modelo.entidades;

public class Talla {
    private int id;
    private String descripcion;

    public Talla() {
    }

    public Talla(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Talla{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
