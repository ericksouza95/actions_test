package br.org.edu.ifrn.LojaCarro.controllers;

import br.org.edu.ifrn.LojaCarro.dto.CarroRequest;
import br.org.edu.ifrn.LojaCarro.model.Carro;
import br.org.edu.ifrn.LojaCarro.services.CarroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/carro")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @PostMapping
    public ResponseEntity<Carro> criar(@Valid @RequestBody CarroRequest request) {
        Carro salvo = carroService.save(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public List<Carro> listarTodos() {
        return carroService.findAll();
    }

    @GetMapping("/{id}")
    public Carro buscarPorId(@PathVariable Long id) {
        return carroService.findById(id);
    }

    @PutMapping("/{id}")
    public Carro atualizar(@PathVariable Long id, @Valid @RequestBody CarroRequest request) {
        return carroService.update(id, request.toEntity());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        carroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
