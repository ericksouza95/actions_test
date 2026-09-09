package br.org.edu.ifrn.LojaCarro.controllers;

import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.services.CarroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Limpa o banco de dados automaticamente após a execução de cada teste
public class CarroIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarroService carroService;

    private Carro carroTeste;

    @BeforeEach
    public void setup() {
        // Cria um carro base no banco para testar o Buscar por ID, Atualizar, Listar e Deletar
        carroTeste = new Carro();
        carroTeste.setModelo("Civic");
        carroTeste.setAno(2022);
        carroTeste.setValor(120000.0);
        carroService.save(carroTeste);
    }

    // 1. TESTE: Salvar Carro via URL
    @Test
    public void deveSalvarCarroComSucesso() throws Exception {
        mockMvc.perform(get("/carro/salvar")
                        .param("modelo", "Fusca")
                        .param("ano", "1978")
                        .param("valor", "25000.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("Carro salvo com sucesso! Modelo: Fusca, Ano: 1978, Valor: R$ 25000.0"));
    }

    // 2. TESTE: Listar Todos os Carros
    @Test
    public void deveListarTodosOsCarros() throws Exception {
        mockMvc.perform(get("/carro/ListarCarros"))
                .andExpect(status().isOk());
    }

    // 3. TESTE: Procurar Carro por ID
    @Test
    public void deveBuscarCarroPorId() throws Exception {
        mockMvc.perform(get("/carro/buscar")
                        .param("id", carroTeste.getId().toString()))
                .andExpect(status().isOk());
    }

    // 4. TESTE: Atualizar dados de um Carro
    @Test
    public void deveAtualizarCarro() throws Exception {
        mockMvc.perform(get("/carro/atualizar")
                        .param("id", carroTeste.getId().toString())
                        .param("modelo", "Civic Atualizado")
                        .param("ano", "2023")
                        .param("valor", "135000.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("Carro atualizado com sucesso!"));
    }

    // 5. TESTE: Deletar um Carro
    @Test
    public void deveDeletarCarro() throws Exception {
        mockMvc.perform(get("/carro/deletar")
                        .param("id", carroTeste.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Carro deletado com sucesso!"));
    }
}