package unlar.edu.ar.streaming_musica.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Productora {
    private String id;
    private String nombre;
    private List<Album> albumes;
    private List<Artista> artistas;

    public Productora(String nombre) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.albumes = new ArrayList<>();
        this.artistas = new ArrayList<>();
    }
}