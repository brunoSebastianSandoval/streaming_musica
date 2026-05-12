package unlar.edu.ar.streaming_musica.strategy;

import unlar.edu.ar.streaming_musica.model.Cancion;
import java.util.List;
import java.util.stream.Collectors;

public class RecomendacionPorPopularidad implements StrategyRecomendacion {

    @Override
    public List<Cancion> recomendar(List<Cancion> catalogo, Cancion cancionReferencia) {
        return catalogo.stream()
                .filter(c -> !c.getId().equals(cancionReferencia.getId()))
                .sorted((c1, c2) -> Integer.compare(c2.getReproducciones().get(), c1.getReproducciones().get()))
                .limit(5)
                .collect(Collectors.toList());
    }
}