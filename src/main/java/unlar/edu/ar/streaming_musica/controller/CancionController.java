package unlar.edu.ar.streaming_musica.controller;

import org.springframework.web.bind.annotation.*;
import unlar.edu.ar.streaming_musica.model.Cancion;
import unlar.edu.ar.streaming_musica.model.Cancion.Genero;
import unlar.edu.ar.streaming_musica.service.CancionService;
import unlar.edu.ar.streaming_musica.strategy.*;

import java.util.List;

@RestController
@RequestMapping("/api/canciones")
public class CancionController {

    private final CancionService cancionService;

    public CancionController(CancionService cancionService) {
        this.cancionService = cancionService;
    }

    @GetMapping("/top10")
    public List<Cancion> getTop10() {
        return cancionService.obtenerTop10MasReproducidas();
    }

    @GetMapping("/filtrar")
    public List<Cancion> filtrar(
            @RequestParam Genero genero,
            @RequestParam String artista,
            @RequestParam int anioInicio,
            @RequestParam int anioFin,
            @RequestParam double ratingMinimo) {
        return cancionService.filtrarCanciones(genero, artista, anioInicio, anioFin, ratingMinimo);
    }

    @GetMapping("/buscar")
    public Cancion buscarPorTitulo(@RequestParam String titulo) {
        return cancionService.busquedaBinariaPorTitulo(titulo);
    }

    @PostMapping("/{titulo}/reproducir")
    public String reproducirCancion(@PathVariable String titulo) {
        Cancion cancion = cancionService.busquedaBinariaPorTitulo(titulo);
        if (cancion != null) {
            cancion.registrarReproduccion();
            return "Reproduciendo: " + cancion.getTitulo() + " | Reproducciones totales: " + cancion.getReproducciones().get();
        }
        return "Cancion no encontrada";
    }
    @GetMapping("/{titulo}/recomendar")
    public List<Cancion> recomendar(@PathVariable String titulo, @RequestParam String tipo) {
        Cancion base = cancionService.busquedaBinariaPorTitulo(titulo);
        if (base == null) return null;

        StrategyRecomendacion estrategia;
        switch (tipo.toLowerCase()) {
            case "popularidad":
                estrategia = new RecomendacionPorPopularidad();
                break;
            case "descubrimiento":
                estrategia = new RecomendacionDescubrimiento();
                break;
            default:
                estrategia = new RecomendacionPorGenero();
                break;
        }
        return cancionService.obtenerRecomendaciones(base, estrategia);
    }
}