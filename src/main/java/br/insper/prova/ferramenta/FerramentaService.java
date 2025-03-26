package br.insper.prova.ferramenta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FerramentaService {

    private final FerramentaRepository repository;
    private final RestTemplate restTemplate;

    private final String API_USUARIO = "http://56.124.127.89:8080/api/usuario/";

    public FerramentaService(FerramentaRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    private UsuarioResponse validarUsuario(String email, boolean adminRequired) {
        try {
            UsuarioResponse usuario = restTemplate.getForObject(API_USUARIO + email, UsuarioResponse.class);
            if (adminRequired && !"ADMIN".equalsIgnoreCase(usuario.getPapel())) {
                throw new RuntimeException("403 - Usuário sem permissão.");
            }
            return usuario;
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("404 - Usuário não encontrado.");
        }
    }

    public Ferramenta cadastrar(Ferramenta ferramenta, String email) {
        UsuarioResponse usuario = validarUsuario(email, true);
        ferramenta.setEmailUsuario(usuario.getEmail());
        ferramenta.setNomeUsuario(usuario.getNome());
        return repository.save(ferramenta);
    }

    public void remover(String id, String email) {
        validarUsuario(email, true);
        repository.deleteById(id);
    }

    public List<Ferramenta> listar() {
        return repository.findAll();
    }
}
