package br.org.edu.ifrn.LojaCarro;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SegurancaTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        token = JwtTestHelper.obterToken(mockMvc, objectMapper);
    }

    // 1 - Validação de Entrada (XSS)
    @Test
    void deveRejeitarCadastroComScriptMaliciosoNaMarca() throws Exception {
        String payload = "{"
                + "\"marca\":\"<script>alert('XSS')</script>\","
                + "\"modelo\":\"Corolla\","
                + "\"ano\":2024,"
                + "\"valor\":150000.0"
                + "}";

        mockMvc.perform(post("/carro")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.marca").exists());
    }

    // 2 - Acesso Não Autorizado
    @Test
    void deveRetornar401AoListarCarrosSemToken() throws Exception {
        mockMvc.perform(get("/carro"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornar401AoCadastrarCarroSemToken() throws Exception {
        String payload = "{\"marca\":\"Toyota\",\"modelo\":\"Corolla\",\"ano\":2024,\"valor\":150000.0}";

        mockMvc.perform(post("/carro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized());
    }

    // 3 - Manipulação de Dados
    @Test
    void deveRejeitarStringMuitoGrande() throws Exception {
        String marcaGrande = repeat("A", 101);
        String payload = "{"
                + "\"marca\":\"" + marcaGrande + "\","
                + "\"modelo\":\"Corolla\","
                + "\"ano\":2024,"
                + "\"valor\":150000.0"
                + "}";

        mockMvc.perform(post("/carro")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.marca").exists());
    }

    @Test
    void deveRejeitarCamposObrigatoriosVazios() throws Exception {
        String payload = "{\"marca\":\"\",\"modelo\":\"\",\"ano\":2024,\"valor\":150000.0}";

        mockMvc.perform(post("/carro")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.marca").exists())
                .andExpect(jsonPath("$.errors.modelo").exists());
    }

    @Test
    void deveRejeitarValoresInvalidos() throws Exception {
        String payload = "{\"marca\":\"Toyota\",\"modelo\":\"Corolla\",\"ano\":1800,\"valor\":-1000.0}";

        mockMvc.perform(post("/carro")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.ano").exists())
                .andExpect(jsonPath("$.errors.valor").exists());
    }

    // 4 - SQL Injection
    @Test
    void deveBloquearSqlInjectionNoIdDoEndpoint() throws Exception {
        mockMvc.perform(get("/carro/1' OR '1'='1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Parâmetro inválido")));
    }

    @Test
    void deveBloquearSqlInjectionComUnionSelect() throws Exception {
        mockMvc.perform(get("/carro/{id}", "1 UNION SELECT")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    private String repeat(String value, int times) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < times; i++) {
            builder.append(value);
        }
        return builder.toString();
    }
}
