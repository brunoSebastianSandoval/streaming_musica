package unlar.edu.ar.streaming_musica.service;

import unlar.edu.ar.streaming_musica.model.Album;
import org.springframework.stereotype.Service;
import unlar.edu.ar.streaming_musica.model.Artista;
import unlar.edu.ar.streaming_musica.model.Cancion;
import unlar.edu.ar.streaming_musica.model.Cancion.Genero;
import unlar.edu.ar.streaming_musica.strategy.StrategyRecomendacion;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;

@Service 
public class CancionService {
    // creamos un catálogo de canciones en memoria para simular la base de datos
    private List<Cancion> catalogo = new ArrayList<>();

    public CancionService() {
        // Creamos artistas
        Artista artista1 = new Artista("Gustavo Cerati");
        Artista artista2 = new Artista("Daft Punk");
        Artista artista3 = new Artista("Charly Garcia");

        // Creamos álbumes
        Album album1 = new Album("Bocanada", 1999);
        Album album2 = new Album("Discovery", 2001);
        Album album3 = new Album("Clics Modernos", 1983);

        // Creamos canciones
        Cancion c1 = new Cancion("Puente", artista1, album1, Genero.ROCK, 253, LocalDate.of(1999, 6, 28));
        c1.getReproducciones().set(15000);
        c1.setRating(4.9);

        Cancion c2 = new Cancion("One More Time", artista2, album2, Genero.ELECTRONICA, 320, LocalDate.of(2000, 11, 13));
        c2.getReproducciones().set(25000);
        c2.setRating(4.8);

        Cancion c3 = new Cancion("Nos siguen pegando abajo", artista3, album3, Genero.ROCK, 205, LocalDate.of(1983, 11, 5));
        c3.getReproducciones().set(18000);
        c3.setRating(5.0);

        // Agregamos al catálogo
        catalogo.add(c1);
        catalogo.add(c2);
        catalogo.add(c3);
    }
    public List<Cancion> filtrarCanciones(Genero genero, String nombreArtista, int anioInicio, int anioFin, double ratingMinimo) {
        return catalogo.stream()
                .filter(c -> c.getGenero() == genero)
                .filter(c -> c.getArtista().getNombre().equalsIgnoreCase(nombreArtista))
                .filter(c -> c.getFechaLanzamiento().getYear() >= anioInicio && c.getFechaLanzamiento().getYear() <= anioFin)
                .filter(c -> c.getRating() >= ratingMinimo)
                .collect(Collectors.toList());
    }
 // top 10 canciones más reproducidas
    public List<Cancion> obtenerTop10MasReproducidas() {
        return catalogo.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getReproducciones().get(), c1.getReproducciones().get()))
                .limit(10)
                .collect(Collectors.toList());
    }

    public double obtenerPromedioDuracionPorGenero(Genero genero) {
        return catalogo.stream()
                .filter(c -> c.getGenero() == genero)
                .collect(Collectors.averagingInt(Cancion::getDuracionSegundos));
    }

    public Artista obtenerArtistaMasPopular() {
        return catalogo.stream()
                .max(Comparator.comparingInt(c -> c.getReproducciones().get()))
                .map(Cancion::getArtista)
                .orElse(null);
    }

    public Map<Integer, List<Cancion>> obtenerDistribucionPorDecadas() {
        return catalogo.stream()
                .collect(Collectors.groupingBy(c -> (c.getFechaLanzamiento().getYear() / 10) * 10));
    }

    public List<Cancion> generarPlaylistAutomatica(int minutosObjetivo) {
        int segundosObjetivo = minutosObjetivo * 60;
        List<Cancion> mejorCombinacion = new ArrayList<>();
        buscarCombinacion(catalogo, segundosObjetivo, new ArrayList<>(), mejorCombinacion, 0, 0);
        return mejorCombinacion;
    }

    private void buscarCombinacion(List<Cancion> disponibles, int objetivo, List<Cancion> actual, List<Cancion> mejor, int sumaActual, int indice) {
        if (sumaActual == objetivo) {
            mejor.clear();
            mejor.addAll(actual);
            return;
        }

        if (sumaActual > objetivo || indice >= disponibles.size() || !mejor.isEmpty()) {
            return;
        }

        Cancion cancionActual = disponibles.get(indice);

        actual.add(cancionActual);
        buscarCombinacion(disponibles, objetivo, actual, mejor, sumaActual + cancionActual.getDuracionSegundos(), indice + 1);
        
        actual.remove(actual.size() - 1);
        buscarCombinacion(disponibles, objetivo, actual, mejor, sumaActual, indice + 1);
        
    }
    public Cancion busquedaBinariaPorTitulo(String titulo) {
        List<Cancion> listaOrdenada = catalogo.stream()
                .sorted(Comparator.comparing(Cancion::getTitulo))
                .collect(Collectors.toList());

        int inicio = 0;
        int fin = listaOrdenada.size() - 1;

        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            Cancion cancionMedio = listaOrdenada.get(medio);
            int comparacion = cancionMedio.getTitulo().compareToIgnoreCase(titulo);

            if (comparacion == 0) {
                return cancionMedio;
            }
            if (comparacion < 0) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        return null;
    }

    public List<Cancion> ordenamientoPersonalizado() {
        return catalogo.stream()
                .sorted(Comparator.comparing((Cancion c) -> c.getArtista().getNombre())
                        .thenComparing(Cancion::getFechaLanzamiento)
                        .reversed())
                .collect(Collectors.toList());
    }

    public List<Cancion> busquedaLinealMultiple(Genero genero, int anioMinimo, double ratingMinimo) {
        return catalogo.stream()
                .filter(c -> c.getGenero() == genero && 
                             c.getFechaLanzamiento().getYear() > anioMinimo && 
                             c.getRating() > ratingMinimo)
                .collect(Collectors.toList());
    }
    public List<Cancion> obtenerRecomendaciones(Cancion cancionReferencia, StrategyRecomendacion estrategia) {
        return estrategia.recomendar(this.catalogo, cancionReferencia);
    }
}