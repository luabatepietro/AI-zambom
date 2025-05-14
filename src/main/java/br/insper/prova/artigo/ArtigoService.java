package br.insper.prova.artigo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtigoService {

    private final ArtigoRepository artigoRepository;

    public ArtigoService(ArtigoRepository artigoRepository) {
        this.artigoRepository = artigoRepository;
    }

    public Artigo save(Artigo artigo) {
        return artigoRepository.save(artigo);
    }

    public List<Artigo> list() {
        return artigoRepository.findAll();
    }

    public void delete(String id) {
        artigoRepository.deleteById(id);
    }
}
