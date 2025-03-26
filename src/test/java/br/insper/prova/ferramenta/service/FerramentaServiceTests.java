package br.insper.prova.ferramenta.service;

import br.insper.prova.ferramenta.Ferramenta;
import br.insper.prova.ferramenta.FerramentaRepository;
import br.insper.prova.ferramenta.FerramentaService;
import br.insper.prova.ferramenta.UsuarioResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FerramentaServiceTests {

    @InjectMocks
    private FerramentaService service;

    @Mock
    private FerramentaRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCadastrarComAdmin() {
        Ferramenta f = new Ferramenta();
        f.setNome("Chave de fenda");
        f.setDescricao("Parafusos");
        f.setCategoria("Mecânica");

        UsuarioResponse mockUser = new UsuarioResponse();
        mockUser.setEmail("eduardo@teste.com");
        mockUser.setNome("Eduardo");
        mockUser.setPapel("ADMIN");

        doReturn(mockUser).when(restTemplate).getForObject(anyString(), eq(UsuarioResponse.class));
        when(repository.save(any(Ferramenta.class))).thenAnswer(i -> i.getArgument(0));

        Ferramenta result = service.cadastrar(f, "eduardo@teste.com");

        Assertions.assertEquals("Eduardo", result.getNomeUsuario());
        Assertions.assertEquals("eduardo@teste.com", result.getEmailUsuario());
    }

    @Test
    void testCadastrarComUserSemPermissao() {
        Ferramenta f = new Ferramenta();
        f.setNome("Furadeira");

        UsuarioResponse user = new UsuarioResponse();
        user.setEmail("user@teste.com");
        user.setNome("User");
        user.setPapel("USER");

        doReturn(user).when(restTemplate).getForObject(anyString(), eq(UsuarioResponse.class));

        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                service.cadastrar(f, "user@teste.com"));

        Assertions.assertTrue(ex.getMessage().contains("403"));
    }

    @Test
    void testRemoverComUsuarioNaoEncontrado() {
        doThrow(new HttpClientErrorException(
                org.springframework.http.HttpStatus.NOT_FOUND,
                "Usuário não encontrado"
        )).when(restTemplate).getForObject(anyString(), eq(UsuarioResponse.class));


        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                service.remover("1", "inexistente@teste.com"));

        Assertions.assertTrue(ex.getMessage().contains("404"));
    }

    @Test
    void testListarFerramentas() {
        List<Ferramenta> lista = Arrays.asList(new Ferramenta(), new Ferramenta());
        when(repository.findAll()).thenReturn(lista);

        List<Ferramenta> result = service.listar();

        Assertions.assertEquals(2, result.size());
    }

    @Test
    void testRemoverComSucesso() {
        UsuarioResponse mockUser = new UsuarioResponse();
        mockUser.setEmail("admin@teste.com");
        mockUser.setNome("Admin");
        mockUser.setPapel("ADMIN");

        doReturn(mockUser).when(restTemplate).getForObject(anyString(), eq(UsuarioResponse.class));

        doNothing().when(repository).deleteById("1");

        Assertions.assertDoesNotThrow(() -> service.remover("1", "admin@teste.com"));

        verify(repository, times(1)).deleteById("1");
    }

}
