package br.org.edu.ifrn.LojaCarro.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeuController {

    private int count = 0;

    @RequestMapping("/teste")
    public String teste() throws Exception {
        count++;
        if (count < 10) {
            return "bom dia";
        }
        throw new Exception("Excedeu o limite");
    }
}
