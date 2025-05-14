package br.insper.prova.artigo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artigos")
public class ArtigoController {

    private final ArtigoService service;

    public ArtigoController(ArtigoService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public void create(@RequestBody Artigo artigo) {
        service.create(artigo);
    }

    @GetMapping
    public List<Artigo> read() {
        return service.read();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
