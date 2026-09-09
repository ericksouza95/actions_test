package br.org.edu.ifrn.LojaCarro.controllers;

import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.services.CarroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carro")
@CrossOrigin(origins = "*")
public class CarroController {

    @Autowired
    private CarroService carroService;

    // 1. SALVAR
    @GetMapping("/salvar")
    public String salvarCarro(@RequestParam String modelo, @RequestParam int ano, @RequestParam double valor) {
        Carro c = new Carro();
        c.setModelo(modelo);
        c.setAno(ano);
        c.setValor(valor);
        carroService.save(c);
        return "Carro salvo com sucesso! Modelo: " + modelo + ", Ano: " + ano + ", Valor: R$ " + valor;
    }

    // 2. LISTAR TODOS
    @GetMapping("/ListarCarros")
    public List<Carro> listarTodos() {
        return carroService.findAll(); // Certifique-se de que o Service tem o método findAll()
    }

    // 3. PROCURAR POR ID
    @GetMapping("/buscar")
    public Carro buscarPorId(@RequestParam Long id) {
        return carroService.findById(id); // Certifique-se de que o Service tem o método findById(id)
    }

    // 4. ATUALIZAR
    @GetMapping("/atualizar")
    public String atualizarCarro(@RequestParam Long id, @RequestParam String modelo, @RequestParam int ano, @RequestParam double valor) {
        Carro c = carroService.findById(id);
        if (c != null) {
            c.setModelo(modelo);
            c.setAno(ano);
            c.setValor(valor);
            carroService.save(c);
            return "Carro atualizado com sucesso!";
        }
        return "Carro não encontrado.";
    }

    // 5. DELETAR
    @GetMapping("/deletar")
    public String deletarCarro(@RequestParam Long id) {
        carroService.deleteById(id); // Certifique-se de que o Service tem o método deleteById(id)
        return "Carro deletado com sucesso!";
    }
}