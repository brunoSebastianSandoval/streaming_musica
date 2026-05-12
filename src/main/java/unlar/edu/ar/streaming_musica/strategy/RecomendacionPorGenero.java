package unlar.edu.ar.streaming_musica.strategy;

import unlar.edu.ar.streaming_musica.model.Cancion;
import java.util.List;
import java.util.stream.Collectors;

public class RecomendacionPorGenero implements StrategyRecomendacion {

    @Override
    public List<Cancion> recomendar(List<Cancion> catalogo, Cancion cancionReferencia) {
        return catalogo.stream()
                .filter(c -> c.getGenero() == cancionReferencia.getGenero() && !c.getId().equals(cancionReferencia.getId()))
                .limit(5)
                .collect(Collectors.toList());
    }
}