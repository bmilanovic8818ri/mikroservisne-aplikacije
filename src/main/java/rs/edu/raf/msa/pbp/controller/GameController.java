package rs.edu.raf.msa.pbp.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import rs.edu.raf.msa.pbp.model.Play;
import rs.edu.raf.msa.pbp.model.PlayByPlay;
import rs.edu.raf.msa.pbp.services.GameService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/game/{gameId}")
    public PlayByPlay game(@PathVariable("gameId") String gameId) {
        return gameService.game(gameId);
    }


    @GetMapping("/plays/{gameId}/{fromMin}/{toMin}")
    public List<Play> plays(@PathVariable("gameId") String gameId,
                            @PathVariable("fromMin") String fromMin,
                            @PathVariable("toMin") String toMin) {
        PlayByPlay game = gameService.game(gameId);
        return game.play(fromMin, toMin);
    }

    @GetMapping("/games")
    public List<String> games() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);
        Resource[] games;
        try {
            games = resolver.getResources("classpath:games/*.json");

            List<String> result = new ArrayList<>(games.length);
            for (Resource g : games) {
                result.add(Objects.requireNonNull(g.getFilename()).replace(".json", ""));
            }
            return result;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error opening games!");
        }
    }

}
