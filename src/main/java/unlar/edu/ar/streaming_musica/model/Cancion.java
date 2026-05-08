package unlar.edu.ar.streaming_musica.model;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Cancion {

    public enum Genero {
        ROCK, POP, JAZZ, ELECTRONICA, CLASICA
    }

    private String id;
    private String titulo;
    private Artista artista; 
    private Album album;     
    private Genero genero;
    private int duracionSegundos;
    private AtomicInteger reproducciones;
    private double rating;
    private LocalDate fechaLanzamiento;

    public Cancion(String titulo, Artista artista, Album album, Genero genero, int duracionSegundos, LocalDate fechaLanzamiento) {
        this.id = UUID.randomUUID().toString(); 
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.genero = genero;
        this.duracionSegundos = duracionSegundos;
        this.reproducciones = new AtomicInteger(0); 
        this.rating = 0.0;
        this.fechaLanzamiento = fechaLanzamiento;
    }

    // Metodo fundamental para el requisito de concurrencia
    public void registrarReproduccion() {
        this.reproducciones.incrementAndGet();
    }
}