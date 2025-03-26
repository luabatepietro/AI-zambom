package br.insper.prova.ferramenta.controller;

import br.insper.prova.ferramenta.Ferramenta;
import br.insper.prova.ferramenta.FerramentaController;
import br.insper.prova.ferramenta.FerramentaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FerramentaControllerTests {

    @InjectMocks
    private FerramentaController controller;

    @Mock
    private FerramentaService service;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCriarFerramentaComSucesso() throws Exception {
        Ferramenta f = new Ferramenta();
        f.setId("1");
        f.setNome("Furadeira");
        f.setDescricao("furos precisos");
        f.setCategoria("Mecânica");
        f.setEmailUsuario("admin@teste.com");
        f.setNomeUsuario("Admin");

        when(service.cadastrar(any(Ferramenta.class), eq("admin@teste.com"))).thenReturn(f);

        mockMvc.perform(post("/api/ferramenta")
                        .header("email", "admin@teste.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(f)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Furadeira"))
                .andExpect(jsonPath("$.emailUsuario").value("admin@teste.com"));
    }

    @Test
    void testDeletarFerramentaComSucesso() throws Exception {
        doNothing().when(service).remover("1", "admin@teste.com");

        mockMvc.perform(delete("/api/ferramenta/1")
                        .header("email", "admin@teste.com"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListarFerramentas() throws Exception {
        Ferramenta f = new Ferramenta();
        f.setId("1");
        f.setNome("Martelo");
        f.setCategoria("Mecânica");

        List<Ferramenta> lista = List.of(f);

        when(service.listar()).thenReturn(lista);

        mockMvc.perform(get("/api/ferramenta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Martelo"));
    }

    @Test
    void testCriarFerramentaSemPermissao() throws Exception {
        Ferramenta f = new Ferramenta();
        f.setNome("Chave inglesa");

        when(service.cadastrar(any(Ferramenta.class), eq("user@teste.com")))
                .thenThrow(new RuntimeException("403 - usuario nao tem essa permissao."));

        try {
            mockMvc.perform(post("/api/ferramenta")
                            .header("email", "user@teste.com")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(f)))
                    .andReturn();
            Assertions.fail("esperava execção mas deu certo.");
        } catch (Exception ex) {
            Assertions.assertTrue(ex.getCause().getMessage().contains("403 - usuario nao tem essa permissao."));
        }
    }


}
