package br.com.seaway.SOC_Web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "analises")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Analise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String genero;
    private String tipo;
    private String classificacao;

    @Column(length = 100)
    private String descricaoGrupo;

    private String referencia;

    @Column(length = 100)
    private String descricaoProduto;

    private String venda;
    private String indice;
    private String estoque;
    private String vendasDezDias;
    private String calculadoLoja;
    private String informadoLoja;
    private String informadoAtacado;
    private String calculadoAtacado;
    private String informadoTotal;
    private String sugestaoOc;
    private String dataSugestao;
    private String sugestaoAnterior;
    private String ocEmAberto;
    private String pedidoAtender;
    private String tipoProduto;
    private String temPromocao;
    private String agrupa;
    private String giro;
    private String bloqueado;
    private String estoqueFabrica;
}