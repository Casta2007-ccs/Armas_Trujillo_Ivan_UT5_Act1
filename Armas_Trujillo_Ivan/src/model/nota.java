package model;

public class nota {
    private String titulo;
    private String contenido;

    public nota(String titulo, String contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }

    // Formato exacto que pide el ejercicio: titulo;contenido
    @Override
    public String toString() {
        return titulo + ";" + contenido;
    }
}