package br.insper.prova.ferramenta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ferramenta")
public class FerramentaController {

    @Autowired
    private FerramentaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ferramenta criarFerramenta(@RequestBody Ferramenta ferramenta, @RequestHeader("email") String email) {
        return service.cadastrar(ferramenta, email);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarFerramenta(@PathVariable String id, @RequestHeader("email") String email) {
        service.remover(id, email);
    }

    @GetMapping
    public List<Ferramenta> listarFerramentas() {
        return service.listar();
    }
}
