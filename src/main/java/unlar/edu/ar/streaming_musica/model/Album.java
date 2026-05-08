package unlar.edu.ar.streaming_musica.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Album {
    private String id;
    private String titulo;
    private int anioLanzamiento;
    private List<Cancion> canciones;

    public Album(String titulo, int anioLanzamiento) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.anioLanzamiento = anioLanzamiento;
        this.canciones = new ArrayList<>();
    }
}