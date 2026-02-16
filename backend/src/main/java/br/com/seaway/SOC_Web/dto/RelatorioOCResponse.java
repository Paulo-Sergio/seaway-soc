package br.com.seaway.SOC_Web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class RelatorioOCResponse {

    private String descricaoGrupo;
    private String referencia;
    private int venda;
    private double indice;
    private int estoque;
    private int vendasDezDias;
    private int calculadoLoja;
    private int informadoLoja;
    private int informadoAtacado;
    private int calculadoAtacado;
    private int informadoTotal;
    private int sugestaoOc;
    private String dataSugestao;
    private String remanejar;
    private String prioridade;

    private String coresPreferenciais;
    private String coresNaoPreferenciais;
    private String coresBloqueadas;
}
