package br.com.seaway.SOC_Web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PrevisaoResponse {

    private Long id;
    private String genero;
    private String tipo;
    private String classificacao;
    private String descricaoGrupo;
    private String referencia;
    private String descricaoProduto;
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
    private LocalDate dataSugestao;
    private int sugestaoAnterior;
    private int ocEmAberto;
    private int pedidoAtender;
    private int tipoProduto;
    private String temPromocao;
    private int agrupa;
    private int giro;
    private String bloqueado;
    private String codigoColecao;
    private String nomeColecao;

    // campo de ação
    private String remanejar;
    private String prioridade;

    // row expanded
    private AnaliseResponse analise;
}
