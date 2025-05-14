package br.insper.prova.artigo;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/artigos")
public class ArtigoController {

    private final ArtigoService artigoService;

    public ArtigoController(ArtigoService artigoService) {
        this.artigoService = artigoService;
    }

    @PostMapping
    public Artigo saveArtigo(@AuthenticationPrincipal Jwt jwt, @RequestBody Artigo artigo) {
        String email = jwt.getClaimAsString("https://musica-insper.com/email");
        List<String> roles = jwt.getClaimAsStringList("https://musica-insper.com/roles");

        if (!roles.contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        artigo.setEmail(email);
        return artigoService.save(artigo);
    }

    @GetMapping
    public List<Artigo> listArtigos() {
        return artigoService.list();
    }

    @DeleteMapping("/{id}")
    public void deleteArtigo(@AuthenticationPrincipal Jwt jwt, @PathVariable String id) {
        List<String> roles = jwt.getClaimAsStringList("https://musica-insper.com/roles");
        if (!roles.contains("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        artigoService.delete(id);
    }
}
