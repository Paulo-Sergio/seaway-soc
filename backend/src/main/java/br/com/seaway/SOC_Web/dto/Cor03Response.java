package br.com.seaway.SOC_Web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cor03Response {

    private String referencia;
    private String codigoCor;
    private String nomeCor;
    private String codigoLoja;
    private String nomeLoja;
    private String tamanho;
    private int estoque;
}
