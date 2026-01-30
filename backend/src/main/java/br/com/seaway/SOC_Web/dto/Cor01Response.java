package br.com.seaway.SOC_Web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cor01Response {

    private Long id;
    private String referencia;
    private int codigoCor;
    private String nomeCor;
    private int venda;
    private int venda10Dias;
    private int estoque;
    private double indice;
    private double iop;
    private String classe;
}
