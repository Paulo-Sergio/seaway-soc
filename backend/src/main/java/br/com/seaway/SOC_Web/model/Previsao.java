package br.com.seaway.SOC_Web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade Previsao – todos os atributos (exceto o id) são do tipo String.
 * Caso algum campo precise ser convertido para outro tipo (por exemplo, datas ou
 * números) a conversão deverá ser feita na camada de serviço ou de DTO.
 */
@Entity
@Table(name = "previsoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Previsao {

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
    private String dataSugestao;      // mantida como String (ex.: "dd/MM/yyyy")
    private String sugestaoAnterior;
    private String ocEmAberto;
    private String pedidoAtender;
    private String tipoProduto;
    private String temPromocao;       // "X" ou ""
    private String agrupa;
    private String giro;
    private String bloqueado;         // "B" ou ""
    private String codigoColecao;
    private String nomeColecao;

    // campo de ação
    private String remanejar;
}