package br.org.edu.ifrn.lojacarro.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class MeuController {

    private final AtomicInteger count = new AtomicInteger();

    @GetMapping("/teste")
    public String teste() {
        if (count.incrementAndGet() < 10) {
            return "bom dia";
        }

        throw new IllegalStateException("Excedeu o limite");
    }

    @PostMapping("/login")
    public String login(@RequestBody String credentials) {
        if (credentials == null || credentials.trim().isEmpty()) {
            throw new IllegalArgumentException("Credenciais obrigatorias");
        }

        return "Login realizado com sucesso";
    }
}
