package unlar.edu.ar.streaming_musica.strategy;

import unlar.edu.ar.streaming_musica.model.Cancion;
import java.util.List;

public interface StrategyRecomendacion {
    List<Cancion> recomendar(List<Cancion> catalogo, Cancion cancionReferencia);
}