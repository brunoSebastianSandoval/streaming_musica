package unlar.edu.ar.streaming_musica.model;

import lombok.Data;
import java.util.UUID;

@Data
public class Artista {
    private String id;
    private String nombre;

    public Artista(String nombre) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
    }
}