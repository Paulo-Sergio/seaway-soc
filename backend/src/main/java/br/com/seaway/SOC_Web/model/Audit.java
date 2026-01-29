package br.com.seaway.SOC_Web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa um registro do arquivo AUDIT.TXT.
 */
@Entity
@Table(name = "audits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Referência (4 dígitos) */
    private String referencia;

    /** Código da Loja (3 dígitos) */
    private String codigoLoja;

    /** Nome da Loja (15 caracteres) */
    private String nomeLoja;

    /** Grade Mínima (3 dígitos) */
    private String gradeMinima;

    /** Estoque da Loja (4 dígitos) */
    private String estoqueLoja;

    /** Vendas dos 10 dias (4 dígitos) */
    private String vendas10Dias;

    /** Venda acumulada (4 dígitos) */
    private String vendaAcumulada;
}