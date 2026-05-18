package br.org.edu.ifrn.LojaCarro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") 
class CarroFeatureTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testeCadastroCarro() throws Exception {
        
        mockMvc.perform(get("/carro") 
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"modelo\":\"Civic\", \"ano\":2022}"))
                .andExpect(status().isOk());
    }

    @Test
    void testeBuscaCarro() throws Exception {

        mockMvc.perform(get("/teste"))
                .andExpect(status().isOk())
                .andExpect(content().string("bom dia"));
    }

    @Test
    void testeSimularFalhaParaPipeline() throws Exception {
       
        // teste de falha do pipeline bom dia = deu certo, boa noite = deu errado
        mockMvc.perform(get("/teste"))
                .andExpect(content().string("boa noite"));
    }
}