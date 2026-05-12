package unlar.edu.ar.streaming_musica.strategy;
import unlar.edu.ar.streaming_musica.model.Cancion;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
public class RecomendacionDescubrimiento implements StrategyRecomendacion {

   @Override
    public List<Cancion> recomendar(List<Cancion> catalogo, Cancion cancionReferencia) {
        return catalogo.stream()
                .filter(c -> !c.getArtista().getId().equals(cancionReferencia.getArtista().getId()))
                .sorted(Comparator.comparingInt(c -> c.getReproducciones().get()))
                .limit(5)
                .collect(Collectors.toList());
    }

}
