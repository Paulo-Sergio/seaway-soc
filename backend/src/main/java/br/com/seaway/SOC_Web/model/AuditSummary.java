package br.com.seaway.SOC_Web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa os campos finais de cada linha do arquivo AUDIT.TXT.
 * Estes campos estão associados a uma referência específica.
 */
@Entity
@Table(name = "audits_summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Referência (4 dígitos) - Chave para associação com registros Audit */
    @Column(unique = true)
    private String referencia;

    /** Previsão Total (4 dígitos) */
    private String previsaoTotal;

    /** OC em aberto (4 dígitos) */
    private String ocEmAberto;

    /** Estoque Fábrica (4 dígitos) */
    private String estoqueFabrica;

    /** Estoque Loja (4 dígitos) */
    private String estoqueLojaFinal;

    /** Pedidos Expedido/Faturado/Despachado (4 dígitos) */
    private String pedidosExpFacDesp;

    /** Saldo de Pedido Anterior (4 dígitos) */
    private String saldoPedidoAnterior;
}