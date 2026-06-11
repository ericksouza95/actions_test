package br.org.edu.ifrn.LojaCarro.controllers;

import br.org.edu.ifrn.LojaCarro.JwtTestHelper;
import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.services.CarroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CarroIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarroService carroService;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;
    private Carro carroTeste;

    @BeforeEach
    public void setup() throws Exception {
        token = JwtTestHelper.obterToken(mockMvc, objectMapper);

        carroTeste = new Carro();
        carroTeste.setMarca("Honda");
        carroTeste.setModelo("Civic");
        carroTeste.setAno(2022);
        carroTeste.setValor(120000.0);
        carroService.save(carroTeste);
    }

    @Test
    public void deveSalvarCarroComSucesso() throws Exception {
        String payload = "{\"marca\":\"Volkswagen\",\"modelo\":\"Fusca\",\"ano\":1978,\"valor\":25000.0}";

        mockMvc.perform(post("/carro")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.modelo").value("Fusca"))
                .andExpect(jsonPath("$.marca").value("Volkswagen"));
    }

    @Test
    public void deveListarTodosOsCarros() throws Exception {
        mockMvc.perform(get("/carro")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void deveBuscarCarroPorId() throws Exception {
        mockMvc.perform(get("/carro/" + carroTeste.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo").value("Civic"));
    }

    @Test
    public void deveAtualizarCarro() throws Exception {
        String payload = "{\"marca\":\"Honda\",\"modelo\":\"Civic Atualizado\",\"ano\":2023,\"valor\":135000.0}";

        mockMvc.perform(put("/carro/" + carroTeste.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo").value("Civic Atualizado"));
    }

    @Test
    public void deveDeletarCarro() throws Exception {
        mockMvc.perform(delete("/carro/" + carroTeste.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}
