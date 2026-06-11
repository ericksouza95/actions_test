package br.org.edu.ifrn.LojaCarro.services;

import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.repository.CarroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class CarroService {

    private static final Pattern PADRAO_INSEGURO = Pattern.compile("(?i)<script|javascript:|onerror=|onload=");

    @Autowired
    private CarroRepository carroRepository;

    public Carro save(Carro c) {
        validarMarca(c.getMarca());
        validarModelo(c.getModelo());
        validarValor(c.getValor());
        validarAno(c.getAno());

        return carroRepository.save(c);
    }

    public Carro update(Long id, Carro dados) {
        Carro existente = findById(id);
        validarMarca(dados.getMarca());
        validarModelo(dados.getModelo());
        validarValor(dados.getValor());
        validarAno(dados.getAno());

        existente.setMarca(dados.getMarca().trim());
        existente.setModelo(dados.getModelo().trim());
        existente.setAno(dados.getAno());
        existente.setValor(dados.getValor());
        return carroRepository.save(existente);
    }

    public List<Carro> findAll() {
        return carroRepository.findAll();
    }

    public Carro findById(Long id) {
        return carroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Carro com ID " + id + " não foi encontrado."));
    }

    public void deleteById(Long id) {
        this.findById(id);
        carroRepository.deleteById(id);
    }

    private void validarMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("A marca do carro é obrigatória.");
        }
        validarTextoSeguro(marca, "marca");
    }

    private void validarModelo(String modelo) {
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("O modelo do carro é obrigatório.");
        }
        validarTextoSeguro(modelo, "modelo");
    }

    private void validarValor(Double valor) {
        if (valor == null) {
            throw new IllegalArgumentException("O valor do carro é obrigatório.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("O valor do carro não pode ser negativo.");
        }
    }

    private void validarAno(int ano) {
        if (ano < 1900 || ano > 2100) {
            throw new IllegalArgumentException("O ano informado é inválido.");
        }
    }

    private void validarTextoSeguro(String texto, String campo) {
        if (texto.length() > 100) {
            throw new IllegalArgumentException("O campo " + campo + " excede o tamanho máximo permitido.");
        }
        if (PADRAO_INSEGURO.matcher(texto).find() || texto.contains("<") || texto.contains(">")) {
            throw new IllegalArgumentException("O campo " + campo + " contém conteúdo potencialmente perigoso.");
        }
    }
}
