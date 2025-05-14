package br.insper.prova.artigo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArtigoRepository extends MongoRepository<Artigo, String> {
}
