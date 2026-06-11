package br.org.edu.ifrn.LojaCarro.services;

import br.org.edu.ifrn.LojaCarro.model.Carro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CarroServiceTest {

    @Autowired
    private CarroService carroService;

    @Test
    public void deveSalvarCarroComDadosValidos() {
        Carro carro = new Carro();
        carro.setMarca("Toyota");
        carro.setModelo("Corolla");
        carro.setAno(2024);
        carro.setValor(150000.0);

        Carro salvo = carroService.save(carro);

        assertNotNull(salvo.getId());
        assertEquals("Corolla", salvo.getModelo());
        assertEquals("Toyota", salvo.getMarca());
    }

    @Test
    public void deveLancarExcecaoQuandoValorForNegativo() {
        Carro carroInvalido = new Carro();
        carroInvalido.setMarca("Volkswagen");
        carroInvalido.setModelo("Fusca");
        carroInvalido.setAno(1980);
        carroInvalido.setValor(-5000.0);

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
            carroService.save(carroInvalido);
        });

        assertEquals("O valor do carro não pode ser negativo.", excecao.getMessage());
    }

    @Test
    public void deveLancarExcecaoQuandoModeloForVazio() {
        Carro carroSemNome = new Carro();
        carroSemNome.setMarca("Toyota");
        carroSemNome.setModelo("");
        carroSemNome.setAno(2023);
        carroSemNome.setValor(60000.0);

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
            carroService.save(carroSemNome);
        });

        assertEquals("O modelo do carro é obrigatório.", excecao.getMessage());
    }

    @Test
    public void deveLancarExcecaoQuandoMarcaForVazia() {
        Carro carroSemMarca = new Carro();
        carroSemMarca.setMarca("   ");
        carroSemMarca.setModelo("Corolla");
        carroSemMarca.setAno(2023);
        carroSemMarca.setValor(60000.0);

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
            carroService.save(carroSemMarca);
        });

        assertEquals("A marca do carro é obrigatória.", excecao.getMessage());
    }

    @Test
    public void deveLancarExcecaoQuandoIdNaoExistir() {
        Long idInexistente = 99999L;

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
            carroService.findById(idInexistente);
        });

        assertEquals("Carro com ID 99999 não foi encontrado.", excecao.getMessage());
    }
}
