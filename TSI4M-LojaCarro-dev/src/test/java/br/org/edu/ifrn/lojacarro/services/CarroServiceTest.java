package br.org.edu.ifrn.lojacarro.services;

import br.org.edu.ifrn.lojacarro.model.Carro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class CarroServiceTest {

    private final CarroService carroService;

    @Autowired
    CarroServiceTest(CarroService carroService) {
        this.carroService = carroService;
    }

    // 1. TESTE DE SUCESSO: Dados corretos devem salvar normalmente
    @Test
    void deveSalvarCarroComDadosValidos() {
        Carro carro = new Carro();
        carro.setModelo("Corolla");
        carro.setAno(2024);
        carro.setValor(150000.0);

        Carro salvo = carroService.save(carro);

        assertNotNull(salvo.getId());
        assertEquals("Corolla", salvo.getModelo());
    }

    // 2. TESTE DA REGRA DE NEGÓCIO: Não pode valor negativo
    @Test
    void deveLancarExcecaoQuandoValorForNegativo() {
        Carro carroInvalido = new Carro();
        carroInvalido.setModelo("Fusca");
        carroInvalido.setAno(1980);
        carroInvalido.setValor(-5000.0); // Valor negativo ilegal!

        // O JUnit espera que o método save jogue uma IllegalArgumentException
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> carroService.save(carroInvalido));

        // Verifica se a mensagem de erro da regra de negócio está correta
        assertEquals("O valor do carro não pode ser negativo.", excecao.getMessage());
    }

    // 3. TESTE DA REGRA DE NEGÓCIO: Modelo não pode ser nulo ou vazio
    @Test
    void deveLancarExcecaoQuandoModeloForVazio() {
        Carro carroSemNome = new Carro();
        carroSemNome.setModelo(""); // Modelo vazio ilegal!
        carroSemNome.setAno(2023);
        carroSemNome.setValor(60000.0);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> carroService.save(carroSemNome));

        assertEquals("O modelo do carro é obrigatório.", excecao.getMessage());
    }

    // 4. TESTE DA REGRA DE NEGÓCIO: ID pesquisado inexistente
    @Test
    void deveLancarExcecaoQuandoIdNaoExistir() {
        Long idInexistente = 99999L;

        // O Service agora deve jogar um erro avisando que o ID não existe
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> carroService.findById(idInexistente));

        assertEquals("Carro com ID 99999 não foi encontrado.", excecao.getMessage());
    }
}
