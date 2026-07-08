package br.org.edu.ifrn.lojacarro.services;

import br.org.edu.ifrn.lojacarro.model.Carro;
import br.org.edu.ifrn.lojacarro.repository.CarroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarroService {

    private final CarroRepository carroRepository;

    public CarroService(CarroRepository carroRepository) {
        this.carroRepository = carroRepository;
    }

    // Método Salvar com REGRAS DE NEGÓCIO de verdade
    public Carro save(Carro c) {
        // Regra 1: Validação do Modelo
        if (c.getModelo() == null || c.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("O modelo do carro é obrigatório.");
        }

        // Regra 2: Validação do Valor Negativo
        if (c.getValor() != null && c.getValor() < 0) {
            throw new IllegalArgumentException("O valor do carro não pode ser negativo.");
        }

        return carroRepository.save(c);
    }

    public List<Carro> findAll() {
        return carroRepository.findAll();
    }

    // Se o ID não existir, lançamos uma exceção clara em vez de retornar null
    public Carro findById(Long id) {
        return carroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Carro com ID " + id + " não foi encontrado."));
    }

    public void deleteById(Long id) {
        // Garante que só deleta se existir, usando a regra acima
        findById(id);
        carroRepository.deleteById(id);
    }
}
