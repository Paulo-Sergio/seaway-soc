package br.com.seaway.SOC_Web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cor01Response {

    private Long id;
    private String referencia;
    private String codigoCor;
    private String nomeCor;
    private int venda;
    private int venda10Dias;
    private int estoque;

    // novos campos
    private int vendaEcommerce; // 4 digitos
    private int venda10DiasEcommerce; // 4 digitos
    private int estoqueEcommerce; // 4 digitos
    // fim novos campos

    private double indice;
    private double iop;
    private String classe;
}
