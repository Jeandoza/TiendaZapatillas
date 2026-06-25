package pe.edu.utp.tiendazapatillas.modelo.entidades;

import java.math.BigDecimal;

public class Zapatilla {
    private int id;
    private String modelo;
    private Marca marca;
    private Talla talla;
    private BigDecimal precio;
    private int stock;

    public Zapatilla() {
    }

    public Zapatilla(int id, String modelo, Marca marca, Talla talla, BigDecimal precio, int stock) {
        this.id = id;
        this.modelo = modelo;
        this.marca = marca;
        this.talla = talla;
        this.precio = precio;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Talla getTalla() {
        return talla;
    }

    public void setTalla(Talla talla) {
        this.talla = talla;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Zapatilla{" +
                "id=" + id +
                ", modelo='" + modelo + '\'' +
                ", marca=" + marca +
                ", talla=" + talla +
                ", precio=" + precio +
                ", stock=" + stock +
                '}';
    }
}
