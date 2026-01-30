package br.com.seaway.SOC_Web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditResponse {

    private Long id;
    private String referencia;
    private int codigoLoja;
    private String nomeLoja;
    private int gradeMinima;
    private int estoqueLoja;
    private int vendas10Dias;
    private int vendaAcumulada;
}
