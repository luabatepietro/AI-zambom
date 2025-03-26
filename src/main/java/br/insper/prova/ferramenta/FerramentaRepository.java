package br.insper.prova.ferramenta;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FerramentaRepository extends MongoRepository<Ferramenta, String> {
}
