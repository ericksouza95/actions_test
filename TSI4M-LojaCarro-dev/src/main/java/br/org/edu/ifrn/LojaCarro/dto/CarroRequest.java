package br.org.edu.ifrn.LojaCarro.dto;

import br.org.edu.ifrn.LojaCarro.model.Carro;

import javax.validation.constraints.*;

public class CarroRequest {

    public static final String TEXTO_SEGURO = "^[^<>\"'&;]*$";

    @NotBlank(message = "A marca do carro é obrigatória.")
    @Size(max = 100, message = "A marca deve ter no máximo 100 caracteres.")
    @Pattern(regexp = TEXTO_SEGURO, message = "A marca contém caracteres inválidos ou potencialmente perigosos.")
    private String marca;

    @NotBlank(message = "O modelo do carro é obrigatório.")
    @Size(max = 100, message = "O modelo deve ter no máximo 100 caracteres.")
    @Pattern(regexp = TEXTO_SEGURO, message = "O modelo contém caracteres inválidos ou potencialmente perigosos.")
    private String modelo;

    @Min(value = 1900, message = "O ano deve ser maior ou igual a 1900.")
    @Max(value = 2100, message = "O ano deve ser menor ou igual a 2100.")
    private int ano;

    @NotNull(message = "O valor do carro é obrigatório.")
    @DecimalMin(value = "0.0", message = "O valor do carro não pode ser negativo.")
    private Double valor;

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Carro toEntity() {
        Carro carro = new Carro();
        carro.setMarca(marca.trim());
        carro.setModelo(modelo.trim());
        carro.setAno(ano);
        carro.setValor(valor);
        return carro;
    }
}
