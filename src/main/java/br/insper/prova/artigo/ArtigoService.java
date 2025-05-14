package br.insper.prova.artigo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtigoService {

    private final ArtigoRepository repository;

    public ArtigoService(ArtigoRepository repository) {
        this.repository = repository;
    }

    public void create(Artigo artigo) {
        repository.save(artigo);
    }

    public List<Artigo> read() {
        return repository.findAll();
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
