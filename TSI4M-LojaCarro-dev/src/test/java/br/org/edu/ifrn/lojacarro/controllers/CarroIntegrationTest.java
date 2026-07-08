package br.org.edu.ifrn.lojacarro.controllers;

import br.org.edu.ifrn.lojacarro.model.Carro;
import br.org.edu.ifrn.lojacarro.services.CarroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Limpa o banco de dados automaticamente após a execução de cada teste
class CarroIntegrationTest {

    private final MockMvc mockMvc;

    private final CarroService carroService;

    private Carro carroTeste;

    @Autowired
    CarroIntegrationTest(MockMvc mockMvc, CarroService carroService) {
        this.mockMvc = mockMvc;
        this.carroService = carroService;
    }

    @BeforeEach
    void setup() {
        // Cria um carro base no banco para testar o Buscar por ID, Atualizar, Listar e Deletar
        carroTeste = new Carro();
        carroTeste.setModelo("Civic");
        carroTeste.setAno(2022);
        carroTeste.setValor(120000.0);
        carroService.save(carroTeste);
    }

    // 1. TESTE: Salvar Carro via URL
    @Test
    void deveSalvarCarroComSucesso() {
        assertDoesNotThrow(() -> mockMvc.perform(get("/carro/salvar")
                        .param("modelo", "Fusca")
                        .param("ano", "1978")
                        .param("valor", "25000.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("Carro salvo com sucesso! Modelo: Fusca, Ano: 1978, Valor: R$ 25000.0")));
    }

    // 2. TESTE: Listar Todos os Carros
    @Test
    void deveListarTodosOsCarros() {
        assertDoesNotThrow(() -> mockMvc.perform(get("/carro/listar"))
                .andExpect(status().isOk()));
    }

    // 3. TESTE: Procurar Carro por ID
    @Test
    void deveBuscarCarroPorId() {
        assertDoesNotThrow(() -> mockMvc.perform(get("/carro/buscar")
                        .param("id", carroTeste.getId().toString()))
                .andExpect(status().isOk()));
    }

    // 4. TESTE: Atualizar dados de um Carro
    @Test
    void deveAtualizarCarro() {
        assertDoesNotThrow(() -> mockMvc.perform(get("/carro/atualizar")
                        .param("id", carroTeste.getId().toString())
                        .param("modelo", "Civic Atualizado")
                        .param("ano", "2023")
                        .param("valor", "135000.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("Carro atualizado com sucesso!")));
    }

    // 5. TESTE: Deletar um Carro
    @Test
    void deveDeletarCarro() {
        assertDoesNotThrow(() -> mockMvc.perform(get("/carro/deletar")
                        .param("id", carroTeste.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Carro deletado com sucesso!")));
    }
}
